package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;

public class VictoryScreen implements Screen {

    private final ExpeditionGame game;
    private final int score;
    private SpriteBatch batch;
    private BitmapFont font;

    public VictoryScreen(ExpeditionGame game, int score) {
        this.game = game;
        this.score = score;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.2f, 0, 1);

        batch.begin();
        font.draw(batch, "¡HAS COMPLETADO EL NIVEL!", 220, 300);
        font.draw(batch, "Puntuación: " + score, 300, 260);
        font.draw(batch, "Pulsa ENTER para volver al menú", 220, 220);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void resize(int width, int height) {

    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
