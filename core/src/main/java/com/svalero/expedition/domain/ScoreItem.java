package com.svalero.expedition.domain;

public class ScoreItem {

    private float x;
    private float y;
    private float width;
    private float height;
    private boolean collected;
    private int scoreValue;

    public ScoreItem(float x, float y, float width, float height, int scoreValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scoreValue = scoreValue;
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

    public int getScoreValue() {
        return scoreValue;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void reset() {
        this.collected = false;
    }
}
