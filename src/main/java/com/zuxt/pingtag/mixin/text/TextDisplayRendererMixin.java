package com.zuxt.pingtag.mixin.text;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zuxt.pingtag.features.TagLabels;
import com.zuxt.pingtag.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(DisplayRenderer.TextDisplayRenderer.class)
public class TextDisplayRendererMixin {
    @Inject(method = "submitInner(Lnet/minecraft/client/renderer/entity/state/TextDisplayEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IF)V", at = @At("RETURN"))
    private void pingtag$submit(TextDisplayEntityRenderState textDisplayEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, float f, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (!Config.get().enabled || mc.level == null || mc.options.hideGui || textDisplayEntityRenderState.cachedInfo == null) return;

        int opacity = textDisplayEntityRenderState.textRenderState != null ? textDisplayEntityRenderState.textRenderState.textOpacity().get(f) & 255 : 0;
        if (opacity < 4) return;

        Player owner = null;

        for (Player player : mc.level.players()) {
            double dx = player.getX() - textDisplayEntityRenderState.x;
            double dz = player.getZ() - textDisplayEntityRenderState.z;
            double dy = textDisplayEntityRenderState.y - player.getY();
            if (dx * dx + dz * dz > 2.25 || dy < 0 || dy > 4 || player.isSpectator() || (player == mc.player && mc.options.getCameraType().isFirstPerson())) continue;
            Pattern name = Pattern.compile("(?<![A-Za-z0-9_])" + Pattern.quote(player.getGameProfile().name()) + "(?![A-Za-z0-9_])");
            for (int index = 0; index < textDisplayEntityRenderState.cachedInfo.lines().size(); index++) {
                StringBuilder text = new StringBuilder();
                textDisplayEntityRenderState.cachedInfo.lines().get(index).contents().accept((i, style, codePoint) -> {
                    text.appendCodePoint(codePoint);
                    return true;
                });
                if (name.matcher(text).find()) {
                    if (owner != null && owner != player) return;
                    owner = player;
                    break;
                }
            }
        }
        if (owner == null) return;

        Component label = TagLabels.label(owner, owner.isDiscrete());
        if (label == null || !TagLabels.claim(owner)) return;

        float scale = (float) Config.get().nametagScale;
        poseStack.pushPose();
        try {
            float y = -10.35f * scale;
            poseStack.translate(textDisplayEntityRenderState.cachedInfo.width() / 2.0f, y - Config.get().offset / 0.025 - 0.8, 0);
            poseStack.scale(scale, scale, scale);
            byte flags = textDisplayEntityRenderState.textRenderState.flags();
            submitNodeCollector.order(1).submitText(poseStack, -mc.font.width(label) / 2.0f, 0,
                    label.getVisualOrderText(), (flags & 1) != 0,
                    (flags & 2) != 0 ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.POLYGON_OFFSET,
                    light, (opacity << 24) | 0xFFFFFF, 0, 0);
        } finally {
            poseStack.popPose();
        }
    }
}
