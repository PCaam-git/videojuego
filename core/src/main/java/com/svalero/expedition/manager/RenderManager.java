package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class RenderManager {

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    public RenderManager(SpriteBatch batch, LogicManager logicManager) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.font = new BitmapFont();
    }

    public void render() {
        batch.begin();
        font.draw(batch, "Pantalla de juego", 50, 430);
        font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 390);
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy(), 50, 350);
        font.draw(batch, "Pulsa ESC para volver al menú", 50, 310);
        batch.end();
    }

    public void dispose() {
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
