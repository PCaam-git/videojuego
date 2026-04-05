package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.screen.MainMenuScreen;
import com.svalero.expedition.ExpeditionGame;

public class ConfigurationScreen implements Screen {

    private final ExpeditionGame game;

    public ConfigurationScreen(ExpeditionGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla en cada frame
        // Fondo azul marino oscuro
        ScreenUtils.clear(0.1f, 0.1f, 0.3f, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
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

    }
}
