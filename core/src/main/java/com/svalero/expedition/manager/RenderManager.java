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

import java.util.List;

public class RenderManager {

    private static final float PLAYER_ANIMATION_FRAME_DURATION = 0.12f;

    private static final String PLAYER_ATLAS_PATH = "player/player.atlas";
    private static final String SUPPLY_ATLAS_PATH = "supply/supply.atlas";
    private static final String BOAR_ATLAS_PATH = "boar/boar.atlas";
    private static final String RABBIT_ATLAS_PATH = "rabbit/rabbit.atlas";
    private static final String BIRD_ATLAS_PATH = "bird/bird.atlas";
    private static final String WASP_ATLAS_PATH = "wasp/wasp.atlas";
    private static final String FRIEND_ATLAS_PATH = "friend/friend.atlas";
    private static final String PRESENT_ATLAS_PATH = "present/present.atlas";

    private final SpriteBatch batch;
    private final LogicManager logicManager;
    private final BitmapFont font;
    private final CameraManager cameraManager;
    private final Matrix4 hudMatrix;

    private final Texture scoreItemTexture;
    private final Texture relicTexture;
    private final Texture immunityItemTexture;
    private final Texture poisonItemTexture;

    // Player
    private final TextureRegion playerIdleDown;
    private final TextureRegion playerIdleUp;
    private final TextureRegion playerIdleLeft;
    private final TextureRegion playerIdleRight;
    private final Animation<TextureRegion> playerRunDownAnimation;
    private final Animation<TextureRegion> playerRunUpAnimation;
    private final Animation<TextureRegion> playerRunLeftAnimation;
    private final Animation<TextureRegion> playerRunRightAnimation;

    // Supply
    private final TextureRegion supplyIdleDown;
    private final TextureRegion supplyIdleUp;
    private final TextureRegion supplyIdleLeft;
    private final TextureRegion supplyIdleRight;
    private final Animation<TextureRegion> supplyWalkDownAnimation;
    private final Animation<TextureRegion> supplyWalkUpAnimation;
    private final Animation<TextureRegion> supplyWalkLeftAnimation;
    private final Animation<TextureRegion> supplyWalkRightAnimation;

    // Boar
    private final TextureRegion boarIdleDown;
    private final TextureRegion boarIdleUp;
    private final TextureRegion boarIdleLeft;
    private final TextureRegion boarIdleRight;
    private final Animation<TextureRegion> boarRunDownAnimation;
    private final Animation<TextureRegion> boarRunUpAnimation;
    private final Animation<TextureRegion> boarRunLeftAnimation;
    private final Animation<TextureRegion> boarRunRightAnimation;

    // Rabbit
    private final TextureRegion rabbitIdleDown;
    private final TextureRegion rabbitIdleUp;
    private final TextureRegion rabbitIdleLeft;
    private final TextureRegion rabbitIdleRight;
    private final Animation<TextureRegion> rabbitJumpDownAnimation;
    private final Animation<TextureRegion> rabbitJumpUpAnimation;
    private final Animation<TextureRegion> rabbitJumpLeftAnimation;
    private final Animation<TextureRegion> rabbitJumpRightAnimation;

    // Bird
    private final Animation<TextureRegion> birdFlyAnimation;

    // Wasp
    private final TextureRegion waspIdle;
    private final Animation<TextureRegion> waspFlyAnimation;

    // Friend
    private final TextureRegion friendIdleDown;

    // Present
    private final TextureRegion presentIdle;
    private final Animation<TextureRegion> presentOpenAnimation;
    private final String relicMode;

    public RenderManager(SpriteBatch batch, LogicManager logicManager, CameraManager cameraManager, String scoreItemTexturePath, String poisonItemTexturePath, String relicMode, String relicTexturePath) {
        this.batch = batch;
        this.logicManager = logicManager;
        this.relicMode = relicMode;
        this.font = new BitmapFont();
        this.font.setColor(0.2f, 0.2f, 0.2f, 1f);
        this.font.getData().setScale(1.0f);
        this.cameraManager = cameraManager;
        this.hudMatrix = new Matrix4();

        this.scoreItemTexture = ResourceManager.getTexture(scoreItemTexturePath);
        this.relicTexture = ResourceManager.getTexture(relicTexturePath);
        this.immunityItemTexture = ResourceManager.getTexture("items/immunity_item_apple.png");
        this.poisonItemTexture = ResourceManager.getTexture(poisonItemTexturePath);

        // Player
        this.playerIdleDown = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_down");
        this.playerIdleUp = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_up");
        this.playerIdleLeft = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_left");
        this.playerIdleRight = ResourceManager.getRegion(PLAYER_ATLAS_PATH, "player_idle_right");

        // Friend
        this.friendIdleDown = ResourceManager.getRegion(FRIEND_ATLAS_PATH, "friend_idle_down");

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

        // Supply
        this.supplyIdleDown = ResourceManager.getRegion(SUPPLY_ATLAS_PATH, "dog_idle_down");
        this.supplyIdleUp = ResourceManager.getRegion(SUPPLY_ATLAS_PATH, "dog_idle_up");
        this.supplyIdleLeft = ResourceManager.getRegion(SUPPLY_ATLAS_PATH, "dog_idle_left");
        this.supplyIdleRight = ResourceManager.getRegion(SUPPLY_ATLAS_PATH, "dog_idle_right");

        this.supplyWalkDownAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(SUPPLY_ATLAS_PATH, "dog_walk_down")
        );
        this.supplyWalkUpAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(SUPPLY_ATLAS_PATH, "dog_walk_up")
        );
        this.supplyWalkLeftAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(SUPPLY_ATLAS_PATH, "dog_walk_left")
        );
        this.supplyWalkRightAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(SUPPLY_ATLAS_PATH, "dog_walk_right")
        );

        // Boar
        this.boarIdleDown = ResourceManager.getRegion(BOAR_ATLAS_PATH, "boar_idle_down");
        this.boarIdleUp = ResourceManager.getRegion(BOAR_ATLAS_PATH, "boar_idle_up");
        this.boarIdleLeft = ResourceManager.getRegion(BOAR_ATLAS_PATH, "boar_idle_left");
        this.boarIdleRight = ResourceManager.getRegion(BOAR_ATLAS_PATH, "boar_idle_right");

        this.boarRunDownAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(BOAR_ATLAS_PATH, "boar_run_down")
        );
        this.boarRunUpAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(BOAR_ATLAS_PATH, "boar_run_up")
        );
        this.boarRunLeftAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(BOAR_ATLAS_PATH, "boar_run_left")
        );
        this.boarRunRightAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(BOAR_ATLAS_PATH, "boar_run_right")
        );

        // Rabbit
        this.rabbitIdleDown = ResourceManager.getRegion(RABBIT_ATLAS_PATH, "rabbit_idle_down");
        this.rabbitIdleUp = ResourceManager.getRegion(RABBIT_ATLAS_PATH, "rabbit_idle_up");
        this.rabbitIdleLeft = ResourceManager.getRegion(RABBIT_ATLAS_PATH, "rabbit_idle_left");
        this.rabbitIdleRight = ResourceManager.getRegion(RABBIT_ATLAS_PATH, "rabbit_idle_right");

        this.rabbitJumpDownAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(RABBIT_ATLAS_PATH, "rabbit_jump_down")
        );
        this.rabbitJumpUpAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(RABBIT_ATLAS_PATH, "rabbit_jump_up")
        );
        this.rabbitJumpLeftAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(RABBIT_ATLAS_PATH, "rabbit_jump_left")
        );
        this.rabbitJumpRightAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(RABBIT_ATLAS_PATH, "rabbit_jump_right")
        );

        // Bird
        this.birdFlyAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(BIRD_ATLAS_PATH, "bird_fly")
        );

        // Wasp
        this.waspIdle = ResourceManager.getRegion(WASP_ATLAS_PATH, "wasp_idle");
        this.waspFlyAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(WASP_ATLAS_PATH, "wasp_fly")
        );

        // Present
        this.presentIdle = ResourceManager.getRegion(PRESENT_ATLAS_PATH, "present_idle");
        this.presentOpenAnimation = new Animation<>(
            PLAYER_ANIMATION_FRAME_DURATION,
            ResourceManager.getRegions(PRESENT_ATLAS_PATH, "present")
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

    private TextureRegion getSupplyCurrentFrame(Supply supply) {
        if (!supply.isMoving()) {
            return switch (supply.getLastDirection()) {
                case UP -> supplyIdleUp;
                case LEFT -> supplyIdleLeft;
                case RIGHT -> supplyIdleRight;
                case DOWN -> supplyIdleDown;
            };
        }

        return switch (supply.getLastDirection()) {
            case UP -> supplyWalkUpAnimation.getKeyFrame(supply.getStateTime(), true);
            case LEFT -> supplyWalkLeftAnimation.getKeyFrame(supply.getStateTime(), true);
            case RIGHT -> supplyWalkRightAnimation.getKeyFrame(supply.getStateTime(), true);
            case DOWN -> supplyWalkDownAnimation.getKeyFrame(supply.getStateTime(), true);
        };
    }

    private TextureRegion getGuardianCurrentFrame(Guardian guardian) {
        if (logicManager.getCurrentLevel() == 1) {
            if (!guardian.isMoving()) {
                return switch (guardian.getLastDirection()) {
                    case UP -> boarIdleUp;
                    case LEFT -> boarIdleLeft;
                    case RIGHT -> boarIdleRight;
                    case DOWN -> boarIdleDown;
                };
            }

            return switch (guardian.getLastDirection()) {
                case UP -> boarRunUpAnimation.getKeyFrame(guardian.getStateTime(), true);
                case LEFT -> boarRunLeftAnimation.getKeyFrame(guardian.getStateTime(), true);
                case RIGHT -> boarRunRightAnimation.getKeyFrame(guardian.getStateTime(), true);
                case DOWN -> boarRunDownAnimation.getKeyFrame(guardian.getStateTime(), true);
            };
        }

        if (!guardian.isMoving()) {
            return switch (guardian.getLastDirection()) {
                case UP -> rabbitIdleUp;
                case LEFT -> rabbitIdleLeft;
                case RIGHT -> rabbitIdleRight;
                case DOWN -> rabbitIdleDown;
            };
        }

        return switch (guardian.getLastDirection()) {
            case UP -> rabbitJumpUpAnimation.getKeyFrame(guardian.getStateTime(), true);
            case LEFT -> rabbitJumpLeftAnimation.getKeyFrame(guardian.getStateTime(), true);
            case RIGHT -> rabbitJumpRightAnimation.getKeyFrame(guardian.getStateTime(), true);
            case DOWN -> rabbitJumpDownAnimation.getKeyFrame(guardian.getStateTime(), true);
        };
    }

    private TextureRegion getBirdCurrentFrame(Bird bird) {
        if (logicManager.getCurrentLevel() == 1) {
            return birdFlyAnimation.getKeyFrame(bird.getStateTime(), true);
        }

        if (!bird.isActive()) {
            return waspIdle;
        }

        return waspFlyAnimation.getKeyFrame(bird.getStateTime(), true);
    }

    private TextureRegion getPresentCurrentFrame() {
        if (logicManager.isPresentOpened()) {
            return presentOpenAnimation.getKeyFrame(presentOpenAnimation.getAnimationDuration(), false);
        }

        if (logicManager.isPresentOpening()) {
            return presentOpenAnimation.getKeyFrame(logicManager.getPresentAnimationTime(), false);
        }

        return presentIdle;
    }

    public void render() {
        Player player = logicManager.getPlayer();

        hudMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.setProjectionMatrix(cameraManager.getCamera().combined);
        batch.begin();

        batch.draw(getPlayerCurrentFrame(player), player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Relic relic = logicManager.getRelic();
        if (!relic.isCollected()) {
            if ("present".equalsIgnoreCase(relicMode)) {
                // Tamaño de la reliquia del nivel 1 duplicado
                batch.draw(getPresentCurrentFrame(), relic.getX(), relic.getY(), relic.getWidth(), relic.getHeight());
            } else {
                batch.draw(relicTexture, relic.getX(), relic.getY(), relic.getWidth() *2 , relic.getHeight() *2);
            }
        }

        List<ScoreItem> scoreItems = logicManager.getScoreItems();
        for (ScoreItem scoreItem : scoreItems) {
            if (!scoreItem.isCollected()) {
                batch.draw(scoreItemTexture, scoreItem.getX(), scoreItem.getY(), scoreItem.getWidth(), scoreItem.getHeight());
            }
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
        batch.draw(getSupplyCurrentFrame(supply), supply.getX(), supply.getY(), supply.getWidth(), supply.getHeight());

        // friend
        batch.draw(friendIdleDown, logicManager.getFriendX(), logicManager.getFriendY(), 24, 24);

        Guardian guardian = logicManager.getGuardian();
        batch.draw(getGuardianCurrentFrame(guardian), guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight());

        Bird bird = logicManager.getBird();
            if (bird.isVisible()) {
                batch.draw(getBirdCurrentFrame(bird), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight());
            }
            batch.end();

            batch.setProjectionMatrix(hudMatrix);
            batch.begin();

            font.draw(batch, "Pulsa ESC para volver al menú", 50, 460);
            font.draw(batch, "Puntuación: " + logicManager.getPlayer().getScore(), 50, 430);
            font.draw(batch, "Energía: " + logicManager.getPlayer().getEnergy() + "/" + logicManager.getPlayer().getMaxEnergy(), 50, 410);
            font.draw(batch, "Vidas: " + logicManager.getPlayer().getLives(), 50, 390);
            font.draw(batch, "Nivel: " + logicManager.getCurrentLevel(), 50, 370);

            if (logicManager.getGuardianDeathMessageTimer() > 0) {
                if (logicManager.getCurrentLevel() == 1) {
                    font.draw(batch, "¡El jabalí te ha atacado! Quizás si le llevas comida..", 120, 315);
                    font.draw(batch, "Quizás si le llevas comida..", 120, 300);
                } else {
                    font.draw(batch, "¡El conejo te ha golpeado en pleno salto!", 120, 315);
                    font.draw(batch, "Esquívalo la próxima vez..", 120, 300);
                }
            }

            if (logicManager.getScoreMessageTimer() > 0) {

                font.draw(batch, "+25 puntos", 120, 120);
            }

            if (logicManager.getPoisonMessageTimer() > 0 ) {
                font.draw(batch, "¡MALA SUERTE, te has envenenado!", 100, 315);
                font.draw(batch, "Serás más lento durante los próximos 5s", 100, 300);
            }

            if (logicManager.getSupplyUnavailableMessageTimer() > 0) {
                font.draw(batch, "Todavía no puedes pedir ayuda a Frodo", 120, 200);
            }

            if (logicManager.getSupplyHealMessageTimer() > 0) {
                font.draw(batch, "¡Frodo ha restaurado " + logicManager.getLastSupplyHealAmount() + " puntos de energía!", 120, 200);
            }

            if (logicManager.getBirdHitMessageTimer() > 0) {
                if (logicManager.getCurrentLevel() == 1) {
                    font.draw(batch, "¡Has asustado al pájaro y te ha atacado!", 120, 200);
                } else {
                    font.draw(batch, "¡Oh, oh! La avispa te ha atacado!", 120, 200);
                }
            }

            if (logicManager.getImmunityMessageTimer() > 0) {
                if (logicManager.getCurrentLevel() == 1) {
                    font.draw(batch, "¡SUERTE! Con esta manzana podrás acceder a la cueva", 100, 315);
                    font.draw(batch, "sin que te ataque el jabalí", 100, 300);
                } else {
                    font.draw(batch, "¡IMPULSO! Esta fruta aumenta tu velocidad durante unos segundos", 100, 300);
                }
            }

            if (logicManager.getFriendMessageTimer() > 0) {
                font.draw(batch, "¡Ya casi has llegado! ", 120, 315);
                font.draw(batch, "Busca tu recompensa en una de las casas", 120, 300);
            }

            batch.end();

    }

    public void dispose() {
        font.dispose();
    }
}
