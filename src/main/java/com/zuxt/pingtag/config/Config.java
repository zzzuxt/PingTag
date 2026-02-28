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
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("pingtag.json"))
                    .build())
            .build();

    public enum LabelPosition {
        ABOVE,
        BELOW
    }

    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public boolean showWhenSneaking = true;

    @SerialEntry
    public boolean hideWhenZero = true;

    @SerialEntry
    public double offset = 0.28;

    @SerialEntry
    public String prefix = "";
    @SerialEntry
    public boolean overridePrefixColor = false;
    @SerialEntry
    public Color prefixColor = Color.WHITE;

    @SerialEntry
    public String suffix = " ms";
    @SerialEntry
    public boolean overrideSuffixColor = false;
    @SerialEntry
    public Color prefixColor = Color.WHITE;

    @SerialEntry
    public Color suffixColor = Color.WHITE;

    @SerialEntry
    public LabelPosition labelPosition = LabelPosition.ABOVE;

    public static Config get() {
        return HANDLER.instance();
    }
}