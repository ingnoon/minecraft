package com.example.artillery.util;
public final class MathUtil {
    private MathUtil(){}
    public static double clamp(double v, double lo, double hi) {
        return v < lo ? lo : v > hi ? hi : v;
    }
}
