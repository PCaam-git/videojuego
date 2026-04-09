package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.ScoreItem;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Deer;

public class RenderManager {

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    private final Texture playerTexture;
    private final Texture scoreItemTexture;
    private final Texture supplyTexture;
    private final Texture guardianTexture;
    private final Texture deerTexture;

    public RenderManager(SpriteBatch batch, LogicManager logicManager) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.font = new BitmapFont();
        this.playerTexture = ResourceManager.getTexture("player/player_idle.png");
        this.scoreItemTexture = ResourceManager.getTexture("items/egg_item.png");
        this.supplyTexture = ResourceManager.getTexture("dog/dog_idle.png");
        this.guardianTexture = ResourceManager.getTexture("bear/bear_idle.png");
        this.deerTexture = ResourceManager.getTexture("deer/deer_walk_4.png");
    }

    public void render() {
        Player player = logicManager.getPlayer();

        batch.begin();
        // Información básica de la pantalla de juego
        font.draw(batch, "Pulsa ESC para volver al menú", 50, 460);
        font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 410);
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy() + "/" + logicManager.getPlayer().getMaxEnergy(), 50, 380);
        font.draw(batch, "Vidas: " + logicManager.getPlayer().getLives(), 50, 350);
        font.draw(batch, "Nivel: 1", 50, 320);


        if (logicManager.getGuardianDamageTimer() > 0) {
            font.draw(batch, "¡El oso te ha atacado!", 50, 70);
        }

        if (logicManager.getScoreMessageTimer() > 0) {
            font.draw(batch, "+25 puntos", 50, 40);
        }

        // Representación visual del jugador
        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Relic relic = logicManager.getRelic();

        if (!relic.isCollected()) {
            batch.draw(playerTexture, relic.getX(), relic.getY(), relic.getWidth(), relic.getHeight());
        }

        ScoreItem scoreItem = logicManager.getScoreItem();
        if(!scoreItem.isCollected()) {
            batch.draw(scoreItemTexture, scoreItem.getX(), scoreItem.getY(), scoreItem.getWidth(), scoreItem.getHeight());
        }

        Supply supply = logicManager.getSupply();
        batch.draw(supplyTexture, supply.getX(), supply.getY(), supply.getWidth(), supply.getHeight());

        Guardian guardian = logicManager.getGuardian();
        batch.draw(guardianTexture, guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight());

        Deer deer = logicManager.getDeer();
        if (deer.isActive()) {
            batch.draw(deerTexture, deer.getX(), deer.getY(), deer.getWidth(), deer.getHeight());
        }
        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
