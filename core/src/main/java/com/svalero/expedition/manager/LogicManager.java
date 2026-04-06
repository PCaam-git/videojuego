package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics;

import com.svalero.expedition.domain.Player;

public class LogicManager {

    private Player player;

    // Ubicación del jugador
    public LogicManager() {
        player = new Player(100,100,150,100,0);
    }

    public void update(float delta) {
        handleInput(); // handleInput decide la dirección
        player.update(delta); // mueve al personaje (niña)
        keepPlayerInsideScreen(); // corrige la posición para que no se salga de los bordes
    }


    private void handleInput() {
        // Se reinicia la dirección en cada frame
        player.setDirectionX(0);
        player.setDirectionY(0);

        // Movimiento horizontal
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setDirectionX(-1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setDirectionX(1);
        }

        // Movimiento vertical
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.setDirectionY(1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.setDirectionY(-1);
        }
    }

    private void keepPlayerInsideScreen() {
        Graphics graphics = Gdx.graphics;

        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getY() < 0) {
            player.setY(0);
        }

        if (player.getX() + player.getWidth() > graphics.getWidth()) {
            player.setX(graphics.getWidth() - player.getWidth());
        }

        if (player.getY() + player.getHeight() > graphics.getHeight()) {
            player.setY(graphics.getHeight() - player.getHeight());
        }
    }

    public Player getPlayer() {
        return player;
    }
}
