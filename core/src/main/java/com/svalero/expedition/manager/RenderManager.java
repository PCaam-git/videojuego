package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Deer;

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
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy() + "/" + logicManager.getPlayer().getMaxEnergy(), 50, 350);
        font.draw(batch, "Pulsa ESC para volver al menú", 50, 310);

        font.draw(batch, "Frodo te acompaña durante la aventura", 50, 150);
        font.draw(batch, "Pulsa P para llamar a Frodo y que te cure", 50, 160);
        font.draw(batch, "Evita al oso que protege la entrada", 50, 100);
        font.draw(batch, "¡Cuidado con el ciervo!", 50, 70);

        if (logicManager.getGuardianDamageTimer() > 0) {
            font.draw(batch, "¡El oso te ha atacado!", 50, 70);
        }

        // Se muestra la posición del jugador para comprobar el movimiento
        font.draw(batch, "Posicion X: " + player.getX(), 50, 270);
        font.draw(batch, "Posicion Y: " + player.getY(), 50, 230);

        // Representación visual del jugador
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Relic relic = logicManager.getRelic();

        if (!relic.isCollected()) {
            batch.draw(playerTexture, relic.getX(), relic.getY(), relic.getWidth(), relic.getHeight());
        }

        Supply supply = logicManager.getSupply();
        batch.draw(playerTexture, supply.getX(), supply.getY(), supply.getWidth(), supply.getHeight());

        Guardian guardian = logicManager.getGuardian();
        batch.draw(playerTexture, guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight());

        Deer deer = logicManager.getDeer();
        if (deer.isActive()) {
            batch.draw(playerTexture, deer.getX(), deer.getY(), deer.getWidth(), deer.getHeight());
        }
        batch.end();
    }

    public void dispose() {
        font.dispose();
        playerTexture.dispose();
    }
}
