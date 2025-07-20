package com.example.artillery;



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
    public static final int DEFAULT_MUZZLE_VEL = 300; // m/s
    public static final double BLAST_RADIUS = 10.0;    // block destroy radius
    public static final double CASUALTY_RADIUS = 100.0; // entity damage radius
    public static final double BASE_DAMAGE = 100.0;     // base damage scalar

    public static void register() {
        // no-op placeholder for compatibility
    }

    /** truncate to 5 decimals; guard -0.0 */
    public static double trunc5deg(double deg){
        double f = Math.floor(deg * 1e5) / 1e5;
        if (Math.abs(f) < 1e-10) f = 0.0;
        return f;
    }
}
