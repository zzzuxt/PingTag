package com.zuxt.pingtag;

import com.zuxt.pingtag.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class PingTag implements ModInitializer {

    public static final KeyBinding TOGGLE_KEYBINDING = new KeyBinding(
            "key.zuxt.pingtag.toggle",
            GLFW.GLFW_KEY_MINUS,
            "key.categories.pingtag"
    );

    @Override
    public void onInitialize() {
        Config.HANDLER.load();

        KeyBindingHelper.registerKeyBinding(TOGGLE_KEYBINDING);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_KEYBINDING.wasPressed()) {
                Config.get().enabled = !Config.get().enabled;
                Config.HANDLER.save();

                if (client.player == null) return;

                Text message = Config.get().enabled
                        ? Text.literal("PingTag toggled ").append(Text.literal("ON").formatted(Formatting.GREEN))
                        : Text.literal("PingTag toggled ").append(Text.literal("OFF").formatted(Formatting.RED));

                client.player.sendMessage(message, true);
            }
        });
    }
}