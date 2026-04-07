package com.svalero.expedition.domain;

public class Player extends Character{

    private int energy;
    private int maxEnergy;
    private int score;
    private float directionX;
    private float directionY;
    private float width;
    private float height;

    public Player(float x, float y, float speed, int energy, int score) {
        super(x, y, speed);
        this.energy = energy;
        this.maxEnergy = energy;
        this.score = score;
        this.directionX = 0;
        this.directionY = 0;
        this.width = 32;
        this.height = 48;
    }

    @Override
    public void update(float delta) {
        // Actualiza la posición del jugador en función de la dirección marcada
        x += directionX * speed * delta;
        y += directionY * speed * delta;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getScore() {
        return score;
    }

    public void setEnergy(int energy) {
        if (energy < 0) {
            this.energy = 0;
        } else if (energy > maxEnergy) {
            this.energy = maxEnergy;
        } else {
            this.energy = energy;
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public void setDirectionY(float directionY) {
        this.directionY = directionY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
