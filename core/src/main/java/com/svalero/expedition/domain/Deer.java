package com.svalero.expedition.domain;

public class Deer extends NPC {

    private float width;
    private float height;
    private boolean isActive; // indica si está en carrera
    private boolean movingRight; // guarda el sentido del cruce
    private float velocityX; //movimiento diagonal
    private float velocityY; // movimiento diagonal
    private float triggerX; // posición X donde el personaje lo activa

    public Deer(float x, float y, float speed, float triggerX) {

        super(x, y, speed);
        this.width = 36;
        this.height = 36;
        this.isActive = false;
        this.movingRight = true;
        this.velocityX = 0;
        this.velocityY = 0;
        this.triggerX = triggerX;
    }

    @Override
    public void update(float delta) {
        if (!isActive) {
            return;
        }

        x += velocityX * delta;
        y += velocityY *delta;
    }

    public void activateTowards(float targetX, float targetY) {
        isActive = true;

        float distanceX = targetX - x;
        float distanceY = targetY - y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance == 0) {
            velocityX = 0;
            velocityY = 0;
            return;
        }

        velocityX = (distanceX / distance) * speed;
        velocityY = (distanceY / distance) * speed;
        movingRight = velocityX > 0;
    }

    public void resetPosition (float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = false;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public float getTriggerX() {
        return triggerX;
    }

    public void setTriggerX(float triggerX) {
        this.triggerX = triggerX;
    }
}
