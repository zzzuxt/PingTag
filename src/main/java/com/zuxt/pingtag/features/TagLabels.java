package com.zuxt.pingtag.features;

import com.zuxt.pingtag.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TagLabels {
    private static final Set<UUID> submitted = new HashSet<>();

    private TagLabels() {}

    public static void beginFrame() {
        submitted.clear();
    }

    public static boolean claim(Player player) {
        return submitted.add(player.getUUID());
    }

    public static Component label(Player player, boolean discrete) {
        Config config = Config.get();
        Minecraft mc = Minecraft.getInstance();
        if (!config.enabled || mc.options.hideGui || mc.getConnection() == null || (config.hideWhenSneaking && discrete)) return null;

        PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(player.getUUID());
        if (playerInfo == null) return null;

        int ping = playerInfo.getLatency();
        if (config.hideIfZero && ping == 0) return null;

        int pingColor = colorForPing(ping, config);
        MutableComponent label = Component.empty();

        if (!config.prefix.isEmpty()) {
            label.append(Component.literal(config.prefix).withStyle(
                    Style.EMPTY.withColor(TextColor.fromRgb(config.overridePrefixColor ? config.prefixColor.getRGB() & 0xFFFFFF : pingColor & 0xFFFFFF))));
        }

        label.append(Component.literal(String.valueOf(ping)).withStyle(
                Style.EMPTY.withColor(TextColor.fromRgb(pingColor & 0xFFFFFF))));

        if (!config.suffix.isEmpty()) {
            label.append(Component.literal(config.suffix).withStyle(
                    Style.EMPTY.withColor(TextColor.fromRgb(config.overrideSuffixColor ? config.suffixColor.getRGB() & 0xFFFFFF : pingColor & 0xFFFFFF))));
        }

        return label;
    }

    private static int colorForPing(int ping, Config config) {
        if (ping <= 50) return config.pingColorLow.getRGB();
        if (ping <= 100) return config.pingColorMedium.getRGB();
        if (ping <= 150) return config.pingColorHigh.getRGB();
        if (ping <= 200) return config.pingColorVeryHigh.getRGB();
        return config.pingColorExtreme.getRGB();
    }
}
