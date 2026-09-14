package com.zuxt.pingtag.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zuxt.pingtag.features.TagLabels;
import com.zuxt.pingtag.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitNameTag(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/phys/Vec3;ILnet/minecraft/network/chat/Component;ZIDLnet/minecraft/client/renderer/state/level/CameraRenderState;)V", ordinal = 1))
    private void pingtag$renderPingLabel(EntityRenderState entityState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, int offset, CallbackInfo ci) {
        if (!(entityState instanceof AvatarRenderState state)) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || state.nameTag == null || state.nameTagAttachment == null || !(mc.level.getEntity(state.id) instanceof Player player)) return;

        Component label = TagLabels.label(player, state.isDiscrete);
        if (label == null || !TagLabels.claim(player)) return;

        float scale = (float) Config.get().nametagScale;
        Vec3 anchor = state.nameTagAttachment;

        poseStack.pushPose();
        try {
            poseStack.translate(anchor.x, anchor.y + 0.5 + 9.0 * 1.15 * 0.025 * scale + Config.get().offset + 0.022, anchor.z);
            poseStack.scale(scale, scale, scale);
            submitNodeCollector.submitNameTag(poseStack, new Vec3(0, -0.5, 0), offset, label, !state.isDiscrete, state.lightCoords, state.distanceToCameraSq, camera);
        } finally {
            poseStack.popPose();
        }
    }
}
