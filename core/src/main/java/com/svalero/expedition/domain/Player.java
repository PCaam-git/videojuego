package com.svalero.expedition.domain;

public class Player extends Character {

    public enum Direction {
        DOWN,
        UP,
        LEFT,
        RIGHT
    }

    private int energy;
    private int maxEnergy;
    private int lives;
    private int score;
    private int immunityCollected;
    private int poisonCollected;
    private float directionX;
    private float directionY;
    private float speedMultiplier;
    private float width;
    private float height;

    // Estado simple de animación
    private Direction lastDirection;
    private boolean moving;
    private float stateTime;

    public Player(float x, float y, float speed, int energy, int lives, int score) {
        super(x, y, speed);
        this.energy = energy;
        this.maxEnergy = energy;
        this.lives = lives;
        this.score = score;
        this.immunityCollected = 0;
        this.poisonCollected = 0;
        this.directionX = 0;
        this.directionY = 0;
        this.width = 32;
        this.height = 48;
        this.speedMultiplier = 1f;

        this.lastDirection = Direction.DOWN;
        this.moving = false;
        this.stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        // Actualiza la posición del jugador en función de la dirección marcada
        x += directionX * speed * speedMultiplier * delta;
        y += directionY * speed * speedMultiplier * delta;
    }

    public void updateAnimationState(float delta) {
        moving = directionX !=0 || directionY !=0;

        if (directionY > 0) {
            lastDirection = Direction.UP;
        } else if (directionY < 0) {
            lastDirection = Direction.DOWN;
        } else if (directionX > 0) {
            lastDirection = Direction.RIGHT;
        } else if (directionX < 0) {
            lastDirection = Direction.LEFT;
        }

        if (moving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getLives() {
        return lives;
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

    public void setLives(int lives) {
        if (lives < 0) {
            this.lives = 0;
        } else {
            this.lives = lives;
        }
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public void addImmunity() {
        this.immunityCollected++;
    }

    public void addPoison() {
        this.poisonCollected++;
    }

    public int getImmunityCollected() {
        return immunityCollected;
    }

    public int getPoisonCollected() {
        return poisonCollected;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float directionX) {
        this.directionX = directionX;
    }

    public float getDirectionY() {
        return directionY;
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

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
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
