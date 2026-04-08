package com.svalero.expedition.domain;

public class Supply extends NPC {

    private float width;
    private float height;
    private boolean called;
    private float followDistance;


    public Supply(float x, float y, float speed) {
        super (x, y, speed);
        this.width = 64;
        this.height = 64;
        this.called = false;
        this.followDistance = 64; // distancia de acompañamiento entre Frodo y la niña
    }

    @Override
    public void update(float delta) {
        // el movimiento se controla desde LogicManager
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
}
