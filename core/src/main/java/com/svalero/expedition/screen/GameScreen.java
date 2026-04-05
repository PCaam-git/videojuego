package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.screen.MainMenuScreen;
import com.svalero.expedition.ExpeditionGame;


public class GameScreen implements Screen {

    private final ExpeditionGame game;

    public GameScreen(ExpeditionGame game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // limpia la pantalla en cada frame
        // fondo gris oscuro
        ScreenUtils.clear(0, 0, 0.2f, 1);

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
