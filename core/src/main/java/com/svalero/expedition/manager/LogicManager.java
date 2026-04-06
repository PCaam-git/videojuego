package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.svalero.expedition.domain.Player;

public class LogicManager {

    private Player player;

    // Ubicación del jugador
    public LogicManager() {
        player = new Player(100,100,150,100,0);
    }

    public void update(float delta) {
        handleInput();
        player.update(delta);
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

    public Player getPlayer() {
        return player;
    }
}
