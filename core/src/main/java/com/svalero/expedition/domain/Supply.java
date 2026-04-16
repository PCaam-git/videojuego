package com.svalero.expedition.domain;

public class Supply extends NPC {

    public enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }

    private final float width;
    private final float height;
    private boolean called;
    private float followDistance;

    // Estado simple de animación
    private Direction lastDirection;
    private boolean moving;
    private float stateTime;

    public Supply(float x, float y, float speed) {
        super(x, y, speed);
        this.width = 64;
        this.height = 64;
        this.called = false;
        this.followDistance = 64; // distancia de acompañamiento entre Frodo y la niña

        this.lastDirection = Direction.DOWN;
        this.moving = false;
        this.stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        // El movimiento se controla desde LogicManager
    }

    public void updateAnimationState(float delta, float directionX, float directionY) {
        moving = directionX != 0 || directionY != 0;

        float horizontalWeight = Math.abs(directionX);
        float verticalWeight = Math.abs(directionY);

        if (horizontalWeight > verticalWeight) {
            if (directionX < 0) {
                lastDirection = Direction.LEFT;
            } else if (directionX > 0) {
                lastDirection = Direction.RIGHT;
            }
        } else {
            if (directionY > 0) {
                lastDirection = Direction.UP;
            } else if (directionY < 0) {
                lastDirection = Direction.DOWN;
            }
        }

        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public float getFollowDistance() {
        return followDistance;
    }

    public void setFollowDistance(float followDistance) {
        this.followDistance = followDistance;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void reset(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.called = false;
        this.moving = false;
        this.stateTime = 0f;
        this.lastDirection = Direction.DOWN;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public boolean isMoving() {
        return moving;
    }

    public float getStateTime() {
        return stateTime;
    }
}
