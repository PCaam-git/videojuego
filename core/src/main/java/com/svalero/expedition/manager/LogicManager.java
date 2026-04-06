package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;

public class LogicManager {

    private final Player player;
    private final Relic relic;

    // Ubicación del jugador
    public LogicManager() {
        player = new Player(100,100,150,100,0);
        relic = new Relic(500, 250);
    }

    public void update(float delta) {
        handleInput(); // handleInput decide la dirección
        player.update(delta); // mueve al personaje (niña)
        keepPlayerInsideScreen(); // corrige la posición para que no se salga de los bordes
        checkRelicCollision(); // comprueba si se ha llegado al premio
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

    private void checkRelicCollision() {
        if (relic.isCollected()) {
            return;
        }

        // Comprueba si hay solapamiento horizontal entre el jugador y el premio
        // El jugador no ha pasado por la derecha del premio (player.getX() < relic.getX() + relic.getWidth())
            // y el jugador no está todavía por la izquierda del premio (player.getX() + player.getWidth() > relic.getX()).
        boolean collisionX = player.getX() < relic.getX() + relic.getWidth()
            && player.getX() + player.getWidth() > relic.getX();

        // Comprueba si hay solapamiento vertical entre el jugador y el premio
        boolean collisionY = player.getY() < relic.getY() + relic.getHeight()
            && player.getY() + player.getHeight() > relic.getY();

        if (collisionX && collisionY) {
            relic.setCollected(true);
            player.setScore(player.getScore()+ 100);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Relic getRelic() {
        return relic;
    }
}
