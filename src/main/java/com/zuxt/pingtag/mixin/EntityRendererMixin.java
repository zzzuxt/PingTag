package com.zuxt.pingtag.mixin;

import com.zuxt.pingtag.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Shadow
    protected abstract void renderLabelIfPresent(S state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "render", at = @At("TAIL"))
    private void renderPingLabel(S state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        Config config = Config.get();

        if (!config.enabled) return;
        if (!(state instanceof PlayerEntityRenderState playerState)) return;
        if (playerState.nameLabelPos == null) return;
        if (playerState.sneaking && config.hideWhenSneaking) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return;

        PlayerListEntry entry = findEntryByName(client, playerState);
        if (entry == null) return;

        int ping = entry.getLatency();
        if (config.hideIfZero && ping == 0) return;

        int pingColor = colorForPing(ping, config);

        Text prefixText = config.prefix.isEmpty() ? Text.empty() : config.overridePrefixColor
                        ? Text.literal(config.prefix).withColor(config.prefixColor.getRGB())
                        : Text.literal(config.prefix).withColor(pingColor);

        Text suffixText = config.suffix.isEmpty() ? Text.empty() : config.overrideSuffixColor
                        ? Text.literal(config.suffix).withColor(config.suffixColor.getRGB())
                        : Text.literal(config.suffix).withColor(pingColor);

        Text pingText = Text.literal(String.valueOf(ping)).withColor(pingColor);

        Text label = Text.empty().append(prefixText).append(pingText).append(suffixText);

        Vec3d original = playerState.nameLabelPos;
        Text originalDisplayName = playerState.displayName;
        float scale = (float) config.nametagScale;

        double labelY = original.y + config.offset;
        double compensatedY = labelY / scale + (1.0 / scale - 1.0) * 0.33;
        playerState.nameLabelPos = new Vec3d(original.x, compensatedY, original.z);
        playerState.displayName = label;

        matrices.push();
        matrices.scale(scale, scale, scale);

        renderLabelIfPresent(state, label, matrices, vertexConsumers, light);

        matrices.pop();

        playerState.nameLabelPos = original;
        playerState.displayName = originalDisplayName;
    }

    @Unique
    private PlayerListEntry findEntryByName(MinecraftClient client, PlayerEntityRenderState playerState) {
        if (playerState.name == null) return null;

        var networkHandler = client.getNetworkHandler();
        if (networkHandler == null) return null;

        var playerList = networkHandler.getPlayerList();
        if (playerList == null) return null;

        String targetName = playerState.name;

        for (PlayerListEntry entry : playerList) {
            if (entry.getProfile().getName().equals(targetName)) {
                return entry;
            }
        }
        return null;
    }

    @Unique
    private int colorForPing(int ping, Config config) {
        if (ping <= 50)  return config.pingColorLow.getRGB();
        if (ping <= 100) return config.pingColorMedium.getRGB();
        if (ping <= 150) return config.pingColorHigh.getRGB();
        if (ping <= 200) return config.pingColorVeryHigh.getRGB();
        return config.pingColorExtreme.getRGB();
    }
}