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

public class GameOverScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private SpriteBatch batch;
    private BitmapFont font;

    public GameOverScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
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

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        font.draw(batch, "GAME OVER", 250, 260);
        font.draw(batch, "Pulsa ENTER para volver al menú", 200, 220);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
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
