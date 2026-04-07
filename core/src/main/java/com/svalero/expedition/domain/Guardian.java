package com.svalero.expedition.domain;

public class Guardian extends NPC{

    private float width;
    private float height;
    private float minY;
    private float maxY;
    private boolean movingUp;

    public Guardian(float x, float y, float speed, float minY, float maxY) {
        super (x, y, speed);
        this.width = 40;
        this.height = 56;
        this.minY = minY;
        this.maxY = maxY;
        this.movingUp = true;
    }

    @Override
    public void update(float delta) {
        // Movimiento vertical continuo entre dos límites
        if (movingUp) {
            y += speed * delta;

            if (y >= maxY) {
                y = maxY;
                movingUp = false;
            }
        } else {
            y -= speed * delta;

            if (y <= minY) {
                y = minY;
                movingUp = true;
            }
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }
}
