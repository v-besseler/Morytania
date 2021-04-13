package com.arlania.world.content;

import java.security.SecureRandom;

public class RNG {
    private static final SecureRandom gen = new SecureRandom();

    // 1/denom chance of this returning true
    public static boolean hit(int denom) {
        return gen.nextInt(denom) == 0;
    }

    // hitPercent(50) has 50% chance of returning true
    public static boolean hitPercent(int percent) {
        return gen.nextInt(100) < percent;
    }

    // Returns a random element of a int[] array
    public static int randElement(int[] array) {
        return array[gen.nextInt(array.length)];
    }

    // Returns a random number between 0 and bound
    public static int randInt(int bound) {
        return gen.nextInt(bound);
    }







}
