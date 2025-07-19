package com.example.artillery.physics;

import com.example.artillery.ArtilleryConfig;

/**
 * Drag + 변중력 탄도 솔버.
 * 결과: 저각, 고각 두 해를 각각 반환할 수 있음(없으면 null).
 */
public final class BallisticsSolver {
    private BallisticsSolver(){}

    public record Solution(double angleRad, double flightTime, double rangeAtTargetAlt, boolean converged){}

    public record Pair(Solution low, Solution high){}

    private static final double DT = ArtilleryConfig.DT;

    /**
     * @param elev0    포구 고도
     * @param elevT    목표 고도
     * @param horizDist 수평 거리
     * @param v0       포구 속도 (m/s)
     */
    public static Pair solvePair(double elev0, double elevT, double horizDist, double v0) {
        // 스캔 후 부호변화 구간 찾아 각각 이분법
        double[] scan = scanAngles(v0, elev0, elevT, horizDist);
        Solution low = null, high = null;
        if (!Double.isNaN(scan[0])) low = solveBisection(scan[0], scan[1], elev0, elevT, horizDist, v0);
        if (!Double.isNaN(scan[2])) high = solveBisection(scan[2], scan[3], elev0, elevT, horizDist, v0);
        return new Pair(low, high);
    }

    // 단일 해법(고각 우선) – 호환용
    public static Solution solve(double elev0, double elevT, double horizDist, double v0) {
        Pair p = solvePair(elev0, elevT, horizDist, v0);
        return p.high != null ? p.high : p.low;
    }

    // --- 내부 수치 적분 ---------------------------------------------------

    private static class Res { double x; double t; boolean valid; Res(double x,double t,boolean v){this.x=x;this.t=t;this.valid=v;}}

    private static Res integrate(double angle, double elev0, double elevT, double horiz, double v0) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double x=0,y=elev0,z=0;
        double vx=v0*cos;
        double vy=v0*sin;
        double t=0;

        double targetY=elevT;
        double targetX=horiz;
        boolean crossed=false;

        final double Cd=ArtilleryConfig.CD;
        final double d=ArtilleryConfig.DIAMETER;
        final double m=ArtilleryConfig.MASS;
        final double rho0=ArtilleryConfig.RHO0;
        final double H=ArtilleryConfig.SCALE_H;
        final double g0=ArtilleryConfig.G0;
        final double Re=ArtilleryConfig.RE;
        final double A=Math.PI*(d*d)*0.25;

        double dt=DT;
        double tmax=ArtilleryConfig.TMAX;

        while (t < tmax) {
            // check crossing
            if (!crossed && x >= targetX) {
                // linear interpolate between last and current step for better range
                crossed=true;
                return new Res(x, t, Math.abs(y-targetY) < 5_000); // allow wide error; refined in bisection
            }
            if (y < -1000) return new Res(x,t,false); // out of world

            // env
            double h=y;
            double rho = rho0 * Math.exp(-h/H);
            if (rho < 0) rho = 0;
            double g = g0 * (Re/(Re+h)) * (Re/(Re+h)); // 변중력

            // speed
            double v = Math.sqrt(vx*vx + vy*vy);
            if (v <= 0) return new Res(x,t,false);
            double drag = 0.5 * rho * Cd * A * v * v / m; // m/s^2

            // accel components
            double ax = -drag * (vx / v);
            double ay = -g - drag * (vy / v);

            // integrate
            vx += ax * dt;
            vy += ay * dt;
            x  += vx * dt;
            y  += vy * dt;
            t  += dt;
        }
        return new Res(x,t,false);
    }

    /**
     * coarse scan across 0..89 deg to locate low/high sign change intervals.
     * return array {loLow, hiLow, loHigh, hiHigh} in radians; NaN if none.
     */
    private static double[] scanAngles(double v0, double elev0, double elevT, double horiz) {
        double[] out = {Double.NaN, Double.NaN, Double.NaN, Double.NaN};
        double prevAng = Math.toRadians(0.1);
        double prevRange = integrate(prevAng, elev0, elevT, horiz, v0).x;
        boolean prevValid=true;
        boolean gotLow=false;
        for (double deg=1; deg<=89; deg+=1) {
            double ang=Math.toRadians(deg);
            double range = integrate(ang, elev0, elevT, horiz, v0).x;
            // sign change when crossing target distance
            if (!gotLow && prevRange < horiz && range >= horiz) {
                out[0]=prevAng; out[1]=ang; gotLow=true;
            } else if (gotLow && prevRange >= horiz && range < horiz) {
                out[2]=prevAng; out[3]=ang; break;
            }
            prevAng=ang; prevRange=range; prevValid=true;
        }
        // if only one interval found treat it as high
        if (!Double.isNaN(out[0]) && Double.isNaN(out[2])) { out[2]=out[0]; out[3]=out[1]; }
        return out;
    }

    private static Solution solveBisection(double lo, double hi, double elev0, double elevT, double horiz, double v0) {
        double target=horiz;
        double mid=0;
        double t=0;
        boolean conv=false;
        for (int i=0;i<40;i++){
            mid=0.5*(lo+hi);
            var r=integrate(mid,elev0,elevT,horiz,v0);
            t=r.t;
            if (Double.isNaN(r.x)) {{ lo=mid; continue; }}
            if (r.x < target) lo=mid; else hi=mid;
        }
        var fin=integrate(mid,elev0,elevT,horiz,v0);
        conv=!Double.isNaN(fin.x);
        return new Solution(mid, fin.t, fin.x, conv);
    }
}
