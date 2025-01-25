package edu.school21.sockets.models;

public class Explosion {
    public double x, y;
    private long startTime;
    private static final long DURATION_MS = 1500; // 1 секунда

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < DURATION_MS;
    }
}
