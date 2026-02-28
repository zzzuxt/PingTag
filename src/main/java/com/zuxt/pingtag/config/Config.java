package com.zuxt.pingtag.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Config {

    public static final ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(Identifier.of("pingtag", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("pingtag_config.json"))
                    .build())
            .build();

    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public boolean hideWhenSneaking = false;

    @SerialEntry
    public boolean hideIfZero = true;

    @SerialEntry
    public double offset = 0.28;

    @SerialEntry
    public String prefix = "";

    @SerialEntry
    public boolean overridePrefixColor = false;

    @SerialEntry
    public Color prefixColor = new Color(0xFFFFFF);

    @SerialEntry
    public String suffix = " ms";

    @SerialEntry
    public boolean overrideSuffixColor = false;

    @SerialEntry
    public Color suffixColor = new Color(0xFFFFFF);

    @SerialEntry
    public Color pingColorLow = new Color(0x55FF55);

    @SerialEntry
    public Color pingColorMedium = new Color(0xFFFF55);

    @SerialEntry
    public Color pingColorHigh = new Color(0xFFAA00);

    @SerialEntry
    public Color pingColorVeryHigh = new Color(0xFF5555);

    @SerialEntry
    public Color pingColorExtreme = new Color(0xAA0000);

    public static Config get() {
        return HANDLER.instance();
    }
}