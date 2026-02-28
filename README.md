## PingTag Fabric Mod
A lightweight Fabric mod, adding a second nametag above the username that displays a player's ping value, akin to Lunar Client's ping display.

## Mod Showcase
> <img width="1920" height="1080" alt="java_3layUFJiMg" src="https://github.com/user-attachments/assets/fb1a3390-b123-4a83-9c60-8310b089a621" />
> `Color-coded ping from green (low) to dark red (high)`
> `Fully customizable prefix, suffix, colors, and offset`

## Mod Features:
- **`Ping display:`** shows each player's ping in ms as a second nametag above their name
- **`Color-coded ping:`** ping is automatically colored based on latency range
- **`Toggle keybind:`** press **-** (editable) to quickly toggle the mod on and off with an action bar notification
- **`NPC filtering:`** players not present in the server's tab list (NPCs, bots, fake players) are automatically skipped
- **`Customizable prefix & suffix:`** add any text before or after the ping value
- **`Custom text colors:`** optionally override the prefix and suffix colors independently
- **`Custom ping colors:`** fully customize the color for each ping range in the config screen
- **`Adjustable offset:`** control the vertical distance between the nametag and the ping label
- **`Hide when sneaking:`** optionally hide the ping label when a player is crouching
- **`Hide if zero:`** optionally hide the ping label if the value is zero

## Config/Settings (ModMenu/YACL):
- **`Enable`** - Toggles the ping nametag display.
- **`Hide When Sneaking`** - Hides the ping nametag when the player is sneaking (still retains vanilla functionality when sneaking).
- **`Hide If Zero`** - Hides the ping nametag when the value is 0.
- **`Prefix Text`** - Text to display before the ping value.
- **`Suffix Text`** - Text to display after the ping value.
- **`Y Offset`** - Vertical distance between the nametag and the ping nametag.
- **`Override Prefix Color`** - Use a custom color for the prefix instead of the ping color.
- **`Prefix Color`** - Color of the prefix text.
- **`Override Suffix Color`** - Use a custom color for the suffix instead of the ping color.
- **`Suffix Color`** - Color of the suffix text.
- **`Ping Range Colors`** - Customize the color for each of the five ping ranges.
- - **`0 – 50ms`** - Green
- - **`51 – 100ms`** - Yellow
- - **`101 – 150ms`** - Gold
- - **`151 – 200ms`** - Red
- - **`200ms+`** - Dark Red

Settings are saved in your `.minecraft\config` folder as `pingtag_config.json`.
