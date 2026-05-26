package com.alowois.transmutated;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration class for the Transmutated mod.
 * Uses NeoForge ModConfigSpec to define and build common configurations.
 */
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /**
     * Ticks required for transmutation at 256 RPM.
     * The actual time taken depends on the rotation speed and follows an inverse square curve.
     * Time (ticks) = (transmutationTimer * 65536) / speed^2
     * Default: 8 (0.4 seconds at 256 RPM)
     */
    public static final ModConfigSpec.IntValue TRANSMUTATION_TIMER = BUILDER
            .comment(" Ticks required for transmutation at 256 RPM")
            .comment(" The actual time taken depends on the rotation speed and follows an inverse square curve.")
            .comment(" Time (ticks) = (transmutationTimer * 65536) / speed^2")
            .comment(" Default 8 results in 524288 / speed^2")
            .translation("transmutated.config.transmutation_timer")
            .defineInRange("transmutationTimer", 8, 1, Integer.MAX_VALUE);

    /**
     * Stress applied by the transmutation casing.
     * Calculated as value * rotation speed.
     */
    public static final ModConfigSpec.IntValue TRANSMUTATION_STRESS = BUILDER
            .comment(" Stress applied by the transmutation casing")
            .comment(" Careful, stress must be multiplied by the rotation speed.")
            .comment(" So with a stress at 256su, the minimum stress requirement of the block is 16,384su at 64rpm.")
            .translation("transmutated.config.transmutation_stress")
            .defineInRange("transmutationStress", 256, 0, Integer.MAX_VALUE);


    /**
     * The built configuration specification.
     */
    static final ModConfigSpec SPEC = BUILDER.build();
}