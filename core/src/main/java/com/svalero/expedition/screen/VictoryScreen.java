package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.domain.Player;
import com.svalero.expedition.manager.ConfigurationManager;
import com.svalero.expedition.manager.ResourceManager;

public class VictoryScreen implements Screen {

    private final ExpeditionGame game;
    private final Player finalPlayerState;
    private final ConfigurationManager configurationManager;
    private SpriteBatch batch;
    private BitmapFont font;

    private final int baseScore;
    private final int livesBonus;
    private final int energyBonus;
    private final int immunityBonus;
    private final int poisonPenalty;
    private final int totalScore;

    public VictoryScreen(ExpeditionGame game, Player player) {
        this.game = game;
        this.finalPlayerState = player;
        this.configurationManager = new ConfigurationManager();
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        // Puntuación con la que cruzó la meta
        this.baseScore = player.getScore();

        // Bonus
        this.livesBonus = player.getLives() * 100;
        this.energyBonus = player.getEnergy() * 2;
        this.immunityBonus = player.getImmunityCollected() * 500;

        // Penalización
        this.poisonPenalty = player.getPoisonCollected() * 200;

        // Suma total de puntos
        this.totalScore = baseScore + livesBonus + energyBonus + immunityBonus - poisonPenalty;
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();

        batch = new SpriteBatch();
        font = new BitmapFont();

        // Sonido de victoria
        if (configurationManager.isSoundEnabled()) {
            ResourceManager.getSound("sounds/level_victory.wav").play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.5f, 0, 1);

        batch.begin();

        if (game.getCurrentLevel() == 1) {
            font.draw(batch, "¡NIVEL 1 COMPLETADO!", 100, 300);
        } else {
            font.draw(batch, "¡NIVEL 2 COMPLETADO!", 100, 300);
        }

        // Recuento desglosado
        font.draw(batch, "Puntuación de objetos: " + baseScore, 300, 430);
        font.draw(batch, "Bonus por vidas restantes (" + finalPlayerState.getLives() + " x 100): +" + livesBonus, 300, 400);
        font.draw(batch, "Bonus por energía restante (" + finalPlayerState.getEnergy() + " x 2): +" + energyBonus, 300, 370);
        font.draw(batch, "Bonus por objeto de inmunidad (" + finalPlayerState.getImmunityCollected() + " x 500): +" + immunityBonus, 300, 340);
        font.draw(batch, "Penalización por veneno (" + finalPlayerState.getPoisonCollected() + " x 200): -" + poisonPenalty, 300, 310);

        // Resultado final
        font.draw(batch, "-----------------------------------", 300, 280);
        font.draw(batch, "PUNTUACIÓN TOTAL: " + totalScore, 300, 250);

        if (game.getCurrentLevel() == 1) {
            font.draw(batch, "Pulsa ENTER para jugar el nivel 2", 250, 150);
        } else {
            font.draw(batch, "Pulsa ENTER para volver al menu principal", 210, 150);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (game.getCurrentLevel() == 1) {
                // Avanza al nivel 2
                game.goToNextLevel();
                game.setScreen(new GameScreen(game));
            } else {
                // Al terminar el nivel 2, reinicia la progresión y vuelve al menú
                game.resetToFirstLevel();
                game.setScreen(new MainMenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
