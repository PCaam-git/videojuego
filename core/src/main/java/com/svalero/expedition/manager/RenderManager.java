package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Matrix4;

import com.svalero.expedition.domain.Player;
import com.svalero.expedition.domain.Relic;
import com.svalero.expedition.domain.ScoreItem;
import com.svalero.expedition.domain.Supply;
import com.svalero.expedition.domain.Guardian;
import com.svalero.expedition.domain.Bird;
import com.svalero.expedition.domain.ImmunityItem;
import com.svalero.expedition.domain.PoisonItem;

public class RenderManager {

    private static final float PLAYER_ANIMATION_FRAME_DURATION = 0.12f;
    private static final String PLAYER_ATLAS_PATH = "player/player.atlas";

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    private final CameraManager cameraManager;
    private final Matrix4 hudMatrix;

    private final Texture scoreItemTexture;
    private final Texture supplyTexture;
    private final Texture guardianTexture;
    private final Texture relicTexture;
    private final Texture birdTexture;
    private final Texture immunityItemTexture;
    private final Texture poisonItemTexture;

    // Idle del player
    private final TextureRegion playerIdleDown;
    private final TextureRegion playerIdleUp;
    private final TextureRegion playerIdleLeft;
    private final TextureRegion playerIdleRight;

    // Run del player
    private final Animation<TextureRegion> playerRunDownAnimation;
    private final Animation<TextureRegion> playerRunUpAnimation;
    private final Animation<TextureRegion> playerRunLeftAnimation;
    private final Animation<TextureRegion> playerRunRightAnimation;

    public RenderManager(SpriteBatch batch, LogicManager logicManager, CameraManager cameraManager) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.font = new BitmapFont();
        this.cameraManager = cameraManager;
        this.hudMatrix = new Matrix4();

        this.scoreItemTexture = ResourceManager.getTexture("items/egg_item.png");
        this.supplyTexture = ResourceManager.getTexture("dog/dog_idle.png");
        this.guardianTexture = ResourceManager.getTexture("bear/bear_idle.png");
        this.relicTexture = ResourceManager.getTexture("relic/bone.png");
        this.birdTexture = ResourceManager.getTexture("bird/bird_idle.png");
        this.immunityItemTexture = ResourceManager.getTexture("items/apple_item.png");
        this.poisonItemTexture = ResourceManager.getTexture("items/apple_item.png");

        this.playerIdleDown = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_down");
        this.playerIdleUp = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_up");
        this.playerIdleLeft = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_left");
        this.playerIdleRight = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_right");

        this.playerRunDownAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(PLAYER_ATLAS_PATH, "player_run_down")
        );
        this.playerRunUpAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(PLAYER_ATLAS_PATH, "player_run_up")
        );
        this.playerRunLeftAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(PLAYER_ATLAS_PATH, "player_run_left")
        );
        this.playerRunRightAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(PLAYER_ATLAS_PATH, "player_run_right")
        );
    }

    private TextureRegion getPlayerCurrentFrame(Player player) {
        if (!player.isMoving()) {
            return switch (player.getLastDirection()) {
                case UP -> playerIdleUp;
                case LEFT -> playerIdleLeft;
                case RIGHT -> playerIdleRight;
                case DOWN -> playerIdleDown;
            };
        }

        return switch (player.getLastDirection()) {
            case UP -> playerRunUpAnimation.getKeyFrame(player.getStateTime(), true);
            case LEFT -> playerRunLeftAnimation.getKeyFrame(player.getStateTime(), true);
            case RIGHT -> playerRunRightAnimation.getKeyFrame(player.getStateTime(), true);
            case DOWN -> playerRunDownAnimation.getKeyFrame(player.getStateTime(), true);
        };
    }

    public void render() {
        Player player = logicManager.getPlayer();

        hudMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        batch.begin();

        batch.draw(getPlayerCurrentFrame(player), player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Relic relic = logicManager.getRelic();
        if (!relic.isCollected()) {
            batch.draw(relicTexture, relic.getX(), relic.getY(), relic.getWidth(), relic.getHeight());
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

        Bird bird = logicManager.getBird();
        if (bird.isVisible()) {
            batch.draw(birdTexture, bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight());
        }

        batch.end();

        batch.setProjectionMatrix(hudMatrix);
        batch.begin();

        font.draw(batch, "Pulsa ESC para volver al menú", 50, 460);
        font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 410);
        font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy() + "/" + logicManager.getPlayer().getMaxEnergy(), 50, 380);
        font.draw(batch, "Vidas: " + logicManager.getPlayer().getLives(), 50, 350);
        font.draw(batch, "Nivel: " + logicManager.getCurrentLevel(), 50, 320);

        if (logicManager.getGuardianDeathMessageTimer() > 0) {
            if (logicManager.getCurrentLevel() == 1) {
                font.draw(batch, "¡El oso te ha atacado! Quizás si le llevas comida..", 50, 100);
            } else {
                font.draw(batch, "¡El conejo te ha golpeado en pleno salto!", 50, 100);
            }
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

        if (logicManager.getBirdHitMessageTimer() > 0) {
            if (logicManager.getCurrentLevel() == 1) {
                font.draw(batch, "¡Has asustado al pájaro y te ha atacado!", 50, 100);
            } else {
                font.draw(batch, "¡Oh, oh! La avispa ha irrumpido en la zona y te ha atacado!", 50, 100);
            }
        }

        if (logicManager.getImmunityMessageTimer() > 0) {
            if (logicManager.getCurrentLevel() == 1) {
                font.draw(batch, "¡SUERTE! Con esta manzana podrás acceder a la cueva sin que te ataque el oso", 50, 100);
            } else {
                font.draw(batch, "¡IMPULSO! Esta fruta aumenta tu velocidad durante unos segundos", 50, 100);
            }
        }

        if (logicManager.getFriendMessageTimer() > 0) {
            font.draw(batch, "¡Ya casi has llegado! Busca tu recompensa en una de las casas", 50, 70);
        }

        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
