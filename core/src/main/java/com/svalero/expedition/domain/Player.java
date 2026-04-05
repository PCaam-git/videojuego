package com.svalero.expedition.domain;

public class Player extends Character{

    private int energy;
    private int score;

    public Player(float x, float y, float speed, int energy, int score) {
        super(x, y, speed);
        this.energy = energy;
        this.score = score;
    }

    @Override
    public void update(float delta) {
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
}
