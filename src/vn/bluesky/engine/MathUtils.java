package vn.bluesky.engine;

import java.util.Random;

/**
 * Created by DuThien on 27/12/2016.
 */

public class MathUtils {
    private static Random random = new Random();

    public static float random() {
        return random.nextFloat();
    }

    public static float random(float low, float high) {
        return (float) (random.nextDouble() * (high - low) + low);
    }


    public static float randomGaussian(float center, float r) {
        return (float) (center + random.nextGaussian() * r);
    }

    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

    public static float sin(float degree) {
        return (float) Math.sin(radianOf(degree));
    }

    public static float cos(float degree) {
        return (float) Math.cos(radianOf(degree));
    }

    public static float cot(float degree) {
        return (float) (1.0f / Math.tan(radianOf(degree)));
    }

    public static float tan(float degree) {
        return (float) Math.tan(radianOf(degree));
    }


    public static float radianOf(float degree) {
        return (float) Math.toRadians(degree);
    }

    public static float dotProduct(float ux, float uy, float vx, float vy) {
        return ux * vx + uy * vy;
    }

    public static float distance(float ux, float uy, float vx, float vy) {
        float dx = ux - vx;
        float dy = uy - vy;
        return sqrt(dx * dx + dy * dy);
    }

    public static double distance(double ux, double uy, double vx, double vy) {
        double dx = ux - vx;
        double dy = uy - vy;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
