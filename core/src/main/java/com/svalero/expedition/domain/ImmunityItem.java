package com.svalero.expedition.domain;

public class ImmunityItem {

    private float x;
    private float y;
    private float width;
    private float height;
    private boolean collected;

    public ImmunityItem(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collected = false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public boolean isCollected() { return collected; }
    public void setCollected(boolean collected) { this.collected = collected; }
}
