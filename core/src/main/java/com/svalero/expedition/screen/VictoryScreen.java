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
import com.svalero.expedition.manager.RankingManager;
import com.svalero.expedition.manager.TextStyleManager;

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
    private final RankingManager rankingManager;
    private final StringBuilder playerName;
    private boolean scoreSaved;

    public VictoryScreen(ExpeditionGame game, Player player) {
        this.game = game;
        this.finalPlayerState = player;
        this.configurationManager = new ConfigurationManager();
        this.rankingManager = new RankingManager();
        this.playerName = new StringBuilder();
        this.scoreSaved = false;
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

    private void handleNameInput() {
        appendCharacterIfPressed(Input.Keys.A, 'A');
        appendCharacterIfPressed(Input.Keys.B, 'B');
        appendCharacterIfPressed(Input.Keys.C, 'C');
        appendCharacterIfPressed(Input.Keys.D, 'D');
        appendCharacterIfPressed(Input.Keys.E, 'E');
        appendCharacterIfPressed(Input.Keys.F, 'F');
        appendCharacterIfPressed(Input.Keys.G, 'G');
        appendCharacterIfPressed(Input.Keys.H, 'H');
        appendCharacterIfPressed(Input.Keys.I, 'I');
        appendCharacterIfPressed(Input.Keys.J, 'J');
        appendCharacterIfPressed(Input.Keys.K, 'K');
        appendCharacterIfPressed(Input.Keys.L, 'L');
        appendCharacterIfPressed(Input.Keys.M, 'M');
        appendCharacterIfPressed(Input.Keys.N, 'N');
        appendCharacterIfPressed(Input.Keys.O, 'O');
        appendCharacterIfPressed(Input.Keys.P, 'P');
        appendCharacterIfPressed(Input.Keys.Q, 'Q');
        appendCharacterIfPressed(Input.Keys.R, 'R');
        appendCharacterIfPressed(Input.Keys.S, 'S');
        appendCharacterIfPressed(Input.Keys.T, 'T');
        appendCharacterIfPressed(Input.Keys.U, 'U');
        appendCharacterIfPressed(Input.Keys.V, 'V');
        appendCharacterIfPressed(Input.Keys.W, 'W');
        appendCharacterIfPressed(Input.Keys.X, 'X');
        appendCharacterIfPressed(Input.Keys.Y, 'Y');
        appendCharacterIfPressed(Input.Keys.Z, 'Z');

        appendCharacterIfPressed(Input.Keys.NUM_0, '0');
        appendCharacterIfPressed(Input.Keys.NUM_1, '1');
        appendCharacterIfPressed(Input.Keys.NUM_2, '2');
        appendCharacterIfPressed(Input.Keys.NUM_3, '3');
        appendCharacterIfPressed(Input.Keys.NUM_4, '4');
        appendCharacterIfPressed(Input.Keys.NUM_5, '5');
        appendCharacterIfPressed(Input.Keys.NUM_6, '6');
        appendCharacterIfPressed(Input.Keys.NUM_7, '7');
        appendCharacterIfPressed(Input.Keys.NUM_8, '8');
        appendCharacterIfPressed(Input.Keys.NUM_9, '9');

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && playerName.length() < 12) {
            playerName.append(' ');
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && playerName.length() > 0) {
            playerName.deleteCharAt(playerName.length() - 1);
        }
    }

    private void appendCharacterIfPressed(int keycode, char character) {
        if (Gdx.input.isKeyJustPressed(keycode) && playerName.length() < 12) {
            playerName.append(character);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.5f, 0, 1);

        batch.begin();

        if (game.getCurrentLevel() == 1) {
            font.draw(batch, "¡NIVEL 1 COMPLETADO!", 80, 300);
        } else {
            font.draw(batch, "¡NIVEL 2 COMPLETADO!", 80, 300);
        }

        // Recuento desglosado
        font.draw(batch, "Puntuación de objetos: " + baseScore, 280, 430);
        font.draw(batch, "Bonus por vidas restantes (" + finalPlayerState.getLives() + " x 100): +" + livesBonus, 280, 400);
        font.draw(batch, "Bonus por energía restante (" + finalPlayerState.getEnergy() + " x 2): +" + energyBonus, 280, 370);
        font.draw(batch, "Bonus por objeto de inmunidad (" + finalPlayerState.getImmunityCollected() + " x 500): +" + immunityBonus, 280, 340);
        font.draw(batch, "Penalización por veneno (" + finalPlayerState.getPoisonCollected() + " x 200): -" + poisonPenalty, 280, 310);

        // Resultado final
        font.draw(batch, "-----------------------------------", 280, 280);
        font.draw(batch, "PUNTUACIÓN TOTAL: " + totalScore, 280, 250);

        if (game.getCurrentLevel() == 1) {
            font.draw(batch, "PUNTUACION ACUMULADA: " + (game.getAccumulatedScore() + totalScore), 280, 180);
            font.draw(batch, "Pulsa ENTER para jugar el nivel 2", 250, 150);
        } else {
            font.draw(batch, "PUNTUACION NIVEL: " + (game.getAccumulatedScore() + totalScore), 280, 180);
            font.draw(batch, "Escribe tu nombre (max 12): " + playerName, 180, 150);
            font.draw(batch, "ENTER -> guardar puntuacion y volver al menu", 180, 120);
            font.draw(batch, "BACKSPACE -> borrar", 180, 90);
        }

        batch.end();

        if (game.getCurrentLevel() == 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.addToAccumulatedScore(totalScore);
                game.goToNextLevel();
                game.setScreen(new GameScreen(game));
            }
        } else {
            handleNameInput();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !scoreSaved && playerName.length() > 0) {
                int campaignScore = game.getAccumulatedScore() + totalScore;
                rankingManager.saveScore(playerName.toString().trim(), campaignScore);
                scoreSaved = true;

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
