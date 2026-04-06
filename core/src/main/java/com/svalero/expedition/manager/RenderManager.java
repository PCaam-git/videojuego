package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;

import com.svalero.expedition.domain.Player;

public class RenderManager {

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    private final Texture playerTexture;

    public RenderManager(SpriteBatch batch, LogicManager logicManager) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.font = new BitmapFont();
        this.playerTexture = new Texture("white.png");
    }

    public void render() {
        Player player = logicManager.getPlayer();

        batch.begin();
        // Información básica de la pantalla de juego
        font.draw(batch, "Pantalla de juego", 50, 430);
        font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 390);
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy(), 50, 350);
        font.draw(batch, "Pulsa ESC para volver al menú", 50, 310);

        // Se muestra la posición del jugador para comprobar el movimiento
        font.draw(batch, "Posicion X: " + player.getX(), 50, 270);
        font.draw(batch, "Posicion Y: " + player.getY(), 50, 230);

        // Representación visual del jugador
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        batch.end();
    }

    public void dispose() {
        font.dispose();
        playerTexture.dispose();
    }
}
