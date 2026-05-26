# Transmutated

Transmutated is a Create Addon for Minecraft 1.21.1 (NeoForge) that introduces resource transmutation within the Create kinetic ecosystem. It allows you to transform items into other resources using the power of rotation.

## Features

- **Transmutation Encased Shaft**: A new kinetic block that can transmute items in a 5x5x5 area around it.
- **Filter-Based Transmutation**: Use filters on the block to determine which transmutation recipe to trigger.
- **Redstone Integration**: The block emits a redstone pulse whenever its internal 'clock' completes a cycle.
- **Create Integration**: Fully compatible with Create's stress and speed systems.
- **JEI Support**: View transmutation recipes directly in Just Enough Items.
- **Ponder Scenes**: Includes in-game Ponder tutorials to help you understand how to use the mod.

## Getting Started

1.  **Craft the Transmutation Encased Shaft**: Combine a Shaft with a Transmutation Casing (check JEI for the exact recipe).
2.  **Provide Rotational Power**: Connect the shaft to your kinetic network. The rotation speed determines how frequently the block performs its transmutation check.
3.  **Set a Filter**: Right-click the block with an item to set it as a filter. This filter determines which transmutation will occur.
4.  **Place Items**: Drop items or place them on Depots within a 2-block radius (5x5x5 area) of the shaft.
5.  **Wait for Transmutation**: The block acts as a 'clock'. Higher speeds result in faster cycles (using a rewarding inverse square curve). When the cycle completes, it will transmute all valid items in range and emit a redstone pulse.

## Configuration

You can adjust several parameters in the `transmutated-common.toml` config file:

- `transmutationTimer`: Base ticks required for one cycle at 256 RPM (default: 8 ticks). The total time is calculated as `(transmutationTimer * 65536) / speed²`. With the default value, this matches `524288 / speed²`.
- `transmutationStress`: The base stress impact of the block (default: 256su).

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.227+
- **Create**: 0.6.0.10+

## License

This mod is licensed under the MIT License.
