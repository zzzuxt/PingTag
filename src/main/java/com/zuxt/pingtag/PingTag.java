package com.zuxt.pingtag;

import com.zuxt.pingtag.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class PingTag implements ModInitializer {
    private static KeyMapping toggleKey;

    @Override
    public void onInitialize() {
        Config.HANDLER.load();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.zuxt.pingtag.toggle",
                GLFW.GLFW_KEY_P,
                KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                Config.get().enabled = !Config.get().enabled;
                Config.HANDLER.save();

                if (client.player == null) return;

                Component message = Config.get().enabled
                        ? Component.literal("[PingTag] ")
                        .append(Component.literal("ON").withStyle(ChatFormatting.GREEN))
                        : Component.literal("[PingTag] ")
                        .append(Component.literal("OFF").withStyle(ChatFormatting.RED));

                client.player.displayClientMessage(message, true);
            }
        });
    }
}