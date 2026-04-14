package com.svalero.expedition.domain;

public class Bird extends NPC {

    private float width;
    private float height;
    private boolean isActive; // indica si está en carrera
    private boolean movingRight; // guarda el sentido del cruce
    private float velocityX; //movimiento diagonal
    private float velocityY; // movimiento diagonal
    private float triggerX; // posición X donde el personaje lo activa
    private float targetX;
    private float targetY;

    public Bird(float x, float y, float speed, float triggerX) {

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

        // Se recalcula la distancia hasta el jugador
        float distanceX = targetX - x;
        float distanceY = targetY - y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);


        if (distance <= speed * delta) {
            x = targetX;
            y = targetY;
            velocityX = 0;
            velocityY = 0;
            isActive = false;
            return;
        }

        if (distance > 0) {
            velocityX = (distanceX / distance) * speed;
            velocityY = (distanceY / distance) * speed;
        }

        movingRight = velocityX > 0;

        x += velocityX * delta;
        y += velocityY * delta;
    }

    public void activate() {
        this.isActive = true;
    }

    public void resetPosition (float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = false;
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = 0;
        this.targetY = 0;
    }

    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
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
