package com.azry.sps.console.server;

public class ApplicationTimestamp {

    private static long startupTime = System.currentTimeMillis();

    public static long getStartupTime() {
        return startupTime;
    }
}