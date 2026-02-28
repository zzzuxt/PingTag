package com.zuxt.pingtag.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.world.tick.Tick;

import java.awt.*;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.create(Config.HANDLER, (defaults, config, builder) -> builder
                .title(Text.literal("PingTag Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable"))
                                .description(OptionDescription.of(Text.literal("Toggle the ping nametag display.")))
                                .binding(defaults.enabled, () -> config.enabled, val -> config.enabled = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Show When Sneaking"))
                                .description(OptionDescription.of(Text.literal("Show ping nametag when the player is sneaking.")))
                                .binding(defaults.showWhenSneaking, () -> config.showWhenSneaking, val -> config.showWhenSneaking = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Hide When Zero"))
                                .description(OptionDescription.of(Text.literal("Hide ping nametag if the ping value is zero.")))
                                .binding(defaults.hideWhenZero, () -> config.hideWhenZero, val -> config.hideWhenZero = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Prefix"))
                                .description(OptionDescription.of(Text.literal("Text to display before the ping value.")))
                                .binding(defaults.prefix, () -> config.prefix, val -> config.prefix = val)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.literal("Suffix"))
                                .description(OptionDescription.of(Text.literal("Text to display after the ping value.")))
                                .binding(defaults.suffix, () -> config.suffix, val -> config.suffix = val)
                                .controller(StringControllerBuilder::create)
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.literal("Offset"))
                                .description(OptionDescription.of(Text.literal("Vertical distance between the nametag and the ping label.")))
                                .binding(defaults.offset, () -> config.offset, val -> config.offset = val)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.28, 1.0)
                                        .step(0.01))
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Colors"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Override Prefix Color"))
                                .description(OptionDescription.of(Text.literal("Override the prefix's color on the ping nametag.")))
                                .binding(defaults.overridePrefixColor, () -> config.overridePrefixColor, val -> config.overridePrefixColor = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.literal("Prefix Color"))
                                .description(OptionDescription.of(Text.literal("Color of the prefix text.")))
                                .binding(defaults.prefixColor, () -> config.prefixColor, val -> config.prefixColor = val)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Override Suffix Color"))
                                .description(OptionDescription.of(Text.literal("Override the prefix's color on the ping nametag.")))
                                .binding(defaults.overrideSuffixColor, () -> config.overrideSuffixColor, val -> config.overrideSuffixColor = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.literal("Suffix Color"))
                                .description(OptionDescription.of(Text.literal("Color of the prefix text.")))
                                .binding(defaults.suffixColor, () -> config.suffixColor, val -> config.suffixColor = val)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(false))
                                .build())
                        .build())

        ).generateScreen(parent);
    }
}