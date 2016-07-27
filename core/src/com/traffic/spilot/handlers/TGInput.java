package com.traffic.spilot.handlers;

public class TGInput {

    public static int x;
    public static int y;
    public static boolean down;
    public static boolean pdown;

    public static boolean[] keys;
    public static boolean[] pkeys;
    private static final int NUM_KEYS = 2;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        pdown = down;
        for(int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static boolean isDown() { return down; }
    public static boolean isPressed() { return down && !pdown; }
    public static boolean isReleased() { return !down && pdown; }

}

