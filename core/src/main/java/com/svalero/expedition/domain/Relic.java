package com.svalero.expedition.domain;

public class Relic {

    private float x; // posición
    private float y; // posición
    private float width;
    private float height;
    private boolean collected; //estado: recogido o no

    public Relic(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = 24;
        this.height = 24;
        this.collected = false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
