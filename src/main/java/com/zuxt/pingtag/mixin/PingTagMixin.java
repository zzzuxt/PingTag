package com.zuxt.pingtag.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zuxt.pingtag.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class PingTagMixin {
    @Unique
    private static final float LINE_HEIGHT = 9.0f * 1.15f * 0.025f;

    @Inject(method = "submitNameTag(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("RETURN"))
    private void pingtag$renderPingLabel(AvatarRenderState renderState, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
        Config config = Config.get();

        if (!config.enabled) return;
        if (renderState.nameTag == null || renderState.nameTagAttachment == null) return;
        if (config.hideWhenSneaking && renderState.isDiscrete) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.getConnection() == null) return;

        Entity entity = mc.level.getEntity(renderState.id);
        if (!(entity instanceof Player player)) return;

        PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(player.getUUID());
        if (playerInfo == null) return;

        int ping = playerInfo.getLatency();
        if (config.hideIfZero && ping == 0) return;

        int pingColor = colorForPing(ping, config);
        MutableComponent label = Component.empty();

        // Prefix
        if (!config.prefix.isEmpty()) {
            label.append(Component.literal(config.prefix).withStyle(
                    Style.EMPTY.withColor(TextColor.fromRgb(
                            config.overridePrefixColor ? config.prefixColor.getRGB() & 0xFFFFFF : pingColor & 0xFFFFFF)))
            );
        }

        label.append(Component.literal(String.valueOf(ping)).withStyle(
                Style.EMPTY.withColor(TextColor.fromRgb(
                        pingColor & 0xFFFFFF)))
        );

        // Suffix
        if (!config.suffix.isEmpty()) {
            label.append(Component.literal(config.suffix).withStyle(
                    Style.EMPTY.withColor(TextColor.fromRgb(
                            config.overrideSuffixColor ? config.suffixColor.getRGB() & 0xFFFFFF : pingColor & 0xFFFFFF)))
            );
        }

        float scale = (float) config.nametagScale;
        double labelY = LINE_HEIGHT + config.offset + 0.02f;
        double compensatedY = labelY + (1.0 - scale) * 2.1;

        poseStack.pushPose();
        poseStack.translate(0.0D, compensatedY, 0.0D);
        poseStack.scale(scale, scale, scale);

        collector.submitNameTag(
                poseStack, renderState.nameTagAttachment, 0, label, !renderState.isDiscrete, renderState.lightCoords, renderState.distanceToCameraSq, cameraState
        );

        poseStack.popPose();
    }

    @Unique
    private static int colorForPing(int ping, Config config) {
        if (ping <= 50) return config.pingColorLow.getRGB();
        if (ping <= 100) return config.pingColorMedium.getRGB();
        if (ping <= 150) return config.pingColorHigh.getRGB();
        if (ping <= 200) return config.pingColorVeryHigh.getRGB();
        return config.pingColorExtreme.getRGB();
    }
}