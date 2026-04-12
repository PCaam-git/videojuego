package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.ScoreItem;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Deer;
import com.svalero.expedition.domain.ImmunityItem;
import com.svalero.expedition.domain.PoisonItem;

public class RenderManager {

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    private final CameraManager cameraManager;
    private final Matrix4 hudMatrix;
    private final Texture playerTexture;
    private final Texture scoreItemTexture;
    private final Texture supplyTexture;
    private final Texture guardianTexture;
    private final Texture deerTexture;
    private final Texture immunityItemTexture;
    private final Texture poisonItemTexture;

    public RenderManager(SpriteBatch batch, LogicManager logicManager, CameraManager cameraManager) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.font = new BitmapFont();
        this.cameraManager = cameraManager;
        this.hudMatrix = new Matrix4();
        this.playerTexture = ResourceManager.getTexture("player/player_idle.png");
        this.scoreItemTexture = ResourceManager.getTexture("items/egg_item.png");
        this.supplyTexture = ResourceManager.getTexture("dog/dog_idle.png");
        this.guardianTexture = ResourceManager.getTexture("bear/bear_idle.png");
        this.deerTexture = ResourceManager.getTexture("deer/deer_walk_4.png");
        this.immunityItemTexture = ResourceManager.getTexture("items/apple_item.png");
        this.poisonItemTexture = ResourceManager.getTexture("items/poison_item.png");
    }

    public void render() {
        Player player = logicManager.getPlayer();

        hudMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        batch.begin();

        batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Relic relic = logicManager.getRelic();
        if (!relic.isCollected()) {
            batch.draw(playerTexture, relic.getX(), relic.getY(), relic.getWidth(), relic.getHeight());
        }

        ScoreItem scoreItem = logicManager.getScoreItem();
        if (!scoreItem.isCollected()) {
            batch.draw(scoreItemTexture, scoreItem.getX(), scoreItem.getY(), scoreItem.getWidth(), scoreItem.getHeight());
        }

        ImmunityItem immunityItem = logicManager.getImmunityItem();
        if (!immunityItem.isCollected()) {
            batch.draw(immunityItemTexture, immunityItem.getX(), immunityItem.getY(), immunityItem.getWidth(), immunityItem.getHeight());
        }

        PoisonItem poisonItem = logicManager.getPoisonItem();
        if (!poisonItem.isCollected()) {
            batch.draw(poisonItemTexture, poisonItem.getX(), poisonItem.getY(), poisonItem.getWidth(), poisonItem.getHeight());
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

        batch.setProjectionMatrix(hudMatrix);
        batch.begin();

        font.draw(batch, "Pulsa ESC para volver al menú", 50, 460);
        font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 410);
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy() + "/" + logicManager.getPlayer().getMaxEnergy(), 50, 380);
        font.draw(batch, "Vidas: " + logicManager.getPlayer().getLives(), 50, 350);
        font.draw(batch, "Nivel: 1", 50, 320);

        if (logicManager.getGuardianDamageTimer() > 0) {
            font.draw(batch, "¡El oso te ha atacado!", 50, 100);
        }

        if (logicManager.getScoreMessageTimer() > 0) {
            font.draw(batch, "+25 puntos", 50, 40);
        }

        if (logicManager.getSupplyUnavailableMessageTimer() > 0) {
            font.draw(batch, "Todavía no puedes pedir ayuda a Frodo", 50, 100);
        }

        if (logicManager.getSupplyHealMessageTimer() > 0) {
            font.draw(batch, "¡Frodo ha restaurado " + logicManager.getLastSupplyHealAmount() + " puntos de energía!", 50, 40);
        }

        if (logicManager.getDeerHitMessageTimer() > 0) {
            font.draw(batch, "¡Te has chocado con el asustado ciervo!", 50, 100);
        }

        if (logicManager.getImmunityMessageTimer() > 0) {
            font.draw(batch, "¡SUERTE! Tienes 5s de inmunidad", 50, 100);
        }

        if (logicManager.getPoisonMessageTimer() > 0) {
            font.draw(batch, "Oh, oh! Serás más lenta durante los próximos 5s", 50, 100);
        }

        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
