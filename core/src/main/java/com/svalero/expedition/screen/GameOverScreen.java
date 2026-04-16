package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.ConfigurationManager;
import com.svalero.expedition.manager.ResourceManager;
import com.svalero.expedition.manager.RankingManager;

public class GameOverScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private final RankingManager rankingManager;
    private final StringBuilder playerName;
    private boolean scoreSaved;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameOverScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
        this.rankingManager = new RankingManager();
        this.playerName = new StringBuilder();
        this.scoreSaved = false;
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();

        batch = new SpriteBatch();
        font = new BitmapFont();

        // Sonido de game over
        if (configurationManager.isSoundEnabled()) {
            ResourceManager.getSound("sounds/gameover.wav").play();
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
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        font.draw(batch, "GAME OVER", 250, 300);
        font.draw(batch, "PUNTUACION NIVEL: " + game.getAccumulatedScore(), 210, 250);
        font.draw(batch, "Escribe tu nombre (max 12): " + playerName, 160, 200);
        font.draw(batch, "ENTER -> guardar puntuacion y volver al menu", 160, 170);
        font.draw(batch, "BACKSPACE -> borrar", 160, 140);
        batch.end();

        handleNameInput();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !scoreSaved && playerName.length() > 0) {
            rankingManager.saveScore(playerName.toString().trim(), game.getAccumulatedScore());
            scoreSaved = true;

            game.resetToFirstLevel();
            game.setScreen(new MainMenuScreen(game));
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
