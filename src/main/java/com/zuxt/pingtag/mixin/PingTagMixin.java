package com.zuxt.pingtag.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zuxt.pingtag.PingTagLabels;
import com.zuxt.pingtag.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class PingTagMixin {
    @Inject(method = "submitNameTag(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZIDLnet/minecraft/client/renderer/state/CameraRenderState;)V", ordinal = 1))
    private void pingtag$renderPingLabel(AvatarRenderState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || state.nameTag == null || state.nameTagAttachment == null || !(mc.level.getEntity(state.id) instanceof Player player)) return;

        Component label = PingTagLabels.label(player, state.isDiscrete);
        if (label == null || !PingTagLabels.claim(player)) return;

        float scale = (float) Config.get().nametagScale;
        Vec3 anchor = state.nameTagAttachment;

        poses.pushPose();
        try {
            poses.translate(anchor.x, anchor.y + 0.5 + 9.0 * 1.15 * 0.025 + Config.get().offset + 0.02, anchor.z);
            poses.scale(scale, scale, scale);
            collector.submitNameTag(poses, new Vec3(0, -0.5, 0), state.showExtraEars ? -10 : 0, label, !state.isDiscrete, state.lightCoords, state.distanceToCameraSq, camera);
        } finally {
            poses.popPose();
        }
    }
}
