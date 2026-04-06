package com.svalero.expedition.domain;

public class Player extends Character{

    private int energy;
    private int score;
    private float directionX;
    private float directionY;

    public Player(float x, float y, float speed, int energy, int score) {
        super(x, y, speed);
        this.energy = energy;
        this.score = score;
        this.directionX = 0;
        this.directionY = 0;
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

    public int getScore() {
        return score;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
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
}
