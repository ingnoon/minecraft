package com.example.artillery;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Physics + damage tuning knobs.
 * 블록 단위 = m.
 */
public final class ArtilleryConfig {
    private ArtilleryConfig(){}

    // --- immutable simulation constants (SI) ---
    public static final double DT = 0.05;           // s (1 tick)
    public static final double CD = 0.295;          // drag coefficient
    public static final double DIAMETER = 0.155;    // m
    public static final double MASS = 43.0;         // kg
    public static final double RHO0 = 1.225;        // kg/m^3 at sea level
    public static final double SCALE_H = 8500.0;    // m scale height
    public static final double G0 = 9.80665;        // m/s^2 surface
    public static final double RE = 6_371_000.0;    // m Earth radius approx
    public static final double TMAX = 300.0;        // s maximum sim time

    // --- configurable gameplay tuning ---
    public static ForgeConfigSpec COMMON_SPEC;
    public static ForgeConfigSpec.IntValue DEFAULT_MUZZLE_VEL; // m/s
    public static ForgeConfigSpec.DoubleValue BLAST_RADIUS;    // block destroy radius
    public static ForgeConfigSpec.DoubleValue CASUALTY_RADIUS; // entity damage radius
    public static ForgeConfigSpec.DoubleValue BASE_DAMAGE;     // base damage scalar

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        DEFAULT_MUZZLE_VEL = b.comment("Default muzzle velocity (m/s) assigned to a new gun.")
            .defineInRange("default_muzzle_velocity", 300, 50, 5000);

        BLAST_RADIUS = b.comment("Block destruction radius on shell detonation (blocks==m).")
            .defineInRange("blast_radius", 10.0, 0.0, 256.0);

        CASUALTY_RADIUS = b.comment("Entity casualty radius (damage scales by distance).")
            .defineInRange("casualty_radius", 100.0, 0.0, 1024.0);

        BASE_DAMAGE = b.comment("Base damage multiplier at zero distance.")
            .defineInRange("base_damage", 100.0, 0.0, 1_000_000.0);

        COMMON_SPEC = b.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
    }

    /** truncate to 5 decimals; guard -0.0 */
    public static double trunc5deg(double deg){
        double f = Math.floor(deg * 1e5) / 1e5;
        if (Math.abs(f) < 1e-10) f = 0.0;
        return f;
    }
}
