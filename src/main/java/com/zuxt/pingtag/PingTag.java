package com.zuxt.pingtag;

import com.mojang.blaze3d.platform.InputConstants;
import com.zuxt.pingtag.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class PingTag implements ModInitializer {
    public static final KeyMapping.Category PINGTAG_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath("pingtag", "general"));

    public static final KeyMapping TOGGLE_KEY =
            KeyMappingHelper.registerKeyMapping(new KeyMapping(
                    "key.zuxt.pingtag.toggle",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_P,
                    PINGTAG_CATEGORY)
            );

    @Override
    public void onInitialize() {
        Config.HANDLER.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.consumeClick()) {
                Config.get().enabled = !Config.get().enabled;
                Config.HANDLER.save();

                if (client.player == null) return;

                Component message = Config.get().enabled
                        ? Component.literal("[PingTag] ")
                        .append(Component.literal("ON").withStyle(ChatFormatting.GREEN))
                        : Component.literal("[PingTag] ")
                        .append(Component.literal("OFF").withStyle(ChatFormatting.RED));

                client.player.sendOverlayMessage(message);
            }
        });
    }
}
