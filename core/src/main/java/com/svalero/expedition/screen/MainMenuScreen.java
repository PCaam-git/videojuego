package com.svalero.expedition.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.screen.GameScreen;
import com.svalero.expedition.screen.ConfigurationScreen;

public class MainMenuScreen implements Screen {

    private final ExpeditionGame game;

    public MainMenuScreen(ExpeditionGame game) {
        this.game = game;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // limpia la pantalla en cada frame
        // Fondo negro
        ScreenUtils.clear(0, 0, 0, 1);

        // gdx.input -> lectura del teclado
        // isKeyJustPressed -> pulsación puntual
        // setScreen -> cambio de pantalla
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            game.setScreen(new ConfigurationScreen(game));
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

