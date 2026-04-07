package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.Supply;

public class LogicManager {

    private final Player player;
    private final Relic relic;
    private final Supply supply;

    // Ubicación del jugador
    public LogicManager() {
        player = new Player(100,100,150,100,0);
        relic = new Relic(500, 250);
        supply = new Supply(250, 180, 0);
    }

    public void update(float delta) {
        handleInput(); // handleInput decide la dirección
        player.update(delta); // mueve al personaje (niña)
        supply.update(delta); // actualiza al acompañante (Frodo)
        moveSupply(delta);
        keepPlayerInsideScreen(); // corrige la posición para que no se salga de los bordes
        checkRelicCollision(); // comprueba si se ha llegado al premio
        checkSupplyCollision();
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

        // Llamada supply
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            supply.setCalled(true);
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

    private void moveSupply(float delta) {
        float distanciaX = player.getX() - supply.getX();
        float distanciaY = player.getY() - supply.getY();

        float distance = (float) Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);

        if (distance == 0) {
            return;
        }

        float directionX = distanciaX / distance;
        float directionY = distanciaY / distance;

        float speed = 120f;

        // Si Frodo ha sido llamado, se acerca hasta la niña para curarla
        if (supply.isCalled()) {
            if (distance < 5f) {
                return;
            }

            supply.setX(supply.getX() + directionX * speed * delta);
            supply.setY(supply.getY() + directionY * speed * delta);
            return;
        }

        // En comportamiento normal, Frodo acompaña a la niña
        // manteniendo aproximadamente 2 casillas de distancia
        if (distance > supply.getFollowDistance()) {
            supply.setX(supply.getX() + directionX * speed * delta);
            supply.setY(supply.getY() + directionY * speed * delta);
        }
    }

    private void checkSupplyCollision() {
        if (!supply.isCalled()) {
            return;
        }

        boolean collisionX = player.getX() < supply.getX() + supply.getWidth()
            && player.getX() + player.getWidth() > supply.getX();

        boolean collisionY = player.getY() < supply.getY() + supply.getHeight()
            && player.getY() + player.getHeight() > supply.getY();

        if (collisionX && collisionY) {
            player.setEnergy(player.getEnergy() + 25);

            // Frodo deja de estar en modo llamada
            supply.setCalled(false);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Relic getRelic() {
        return relic;
    }

    public Supply getSupply() {
        return supply;
    }
}
