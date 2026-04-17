package com.svalero.expedition.domain;

public class Bird extends NPC {

    private enum BirdState {
        IDLE,
        ATTACKING,
        RETURNING
    }

    private float width;
    private float height;
    private boolean movingRight;
    private float velocityX;
    private float velocityY;
    private float targetX;
    private float targetY;

    private float homeX;
    private float homeY;

    private boolean alwaysVisible;
    private BirdState state;
    private float stateTime;

    public Bird(float x, float y, float speed, float triggerX) {
        super(x, y, speed);
        this.width = 36;
        this.height = 36;
        this.movingRight = true;
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = 0;
        this.targetY = 0;
        this.homeX = x;
        this.homeY = y;
        this.alwaysVisible = false;
        this.state = BirdState.IDLE;
        this.stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        if (state == BirdState.IDLE) {
            return;
        }

        stateTime += delta;

        float distanceX = targetX - x;
        float distanceY = targetY - y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= speed * delta) {
            x = targetX;
            y = targetY;
            velocityX = 0;
            velocityY = 0;

            if (state == BirdState.ATTACKING) {
                state = BirdState.RETURNING;
                targetX = homeX;
                targetY = homeY;
            } else {
                state = BirdState.IDLE;
                stateTime = 0f;
            }
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

    public void activateAttack(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.state = BirdState.ATTACKING;
        this.stateTime = 0f;
    }

    public void resetPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.homeX = x;
        this.homeY = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = x;
        this.targetY = y;
        this.alwaysVisible = false;
        this.state = BirdState.IDLE;
        this.stateTime = 0f;
    }

    public void setIdlePosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.homeX = x;
        this.homeY = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.targetX = x;
        this.targetY = y;
        this.state = BirdState.IDLE;
        this.stateTime = 0f;
    }

    public void setAlwaysVisible(boolean alwaysVisible) {
        this.alwaysVisible = alwaysVisible;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return state == BirdState.ATTACKING || state == BirdState.RETURNING;
    }

    public boolean isAttacking() {
        return state == BirdState.ATTACKING;
    }

    public boolean isIdle() {
        return state == BirdState.IDLE;
    }

    public boolean isVisible() {
        return alwaysVisible || state != BirdState.IDLE;
    }

    public boolean isAtHome() {
        return x == homeX && y == homeY && state == BirdState.IDLE;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getStateTime() {
        return stateTime;
    }
}
