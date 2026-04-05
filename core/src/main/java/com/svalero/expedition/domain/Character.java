package com.svalero.expedition.domain;

public abstract class Character {

    // Atributos
    protected float x; //posicion
    protected float y; // posicion
    protected float speed; // movimiento

    public Character(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // Métodos. Abstract es la base de herencia
        // Delta: tiempo transcurrido desde el último frame, expresado en segundos.
    public abstract void update(float delta);

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }
}
