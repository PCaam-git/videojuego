package com.svalero.expedition.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.screen.GameScreen;
import com.svalero.expedition.screen.ConfigurationScreen;

public class MainMenuScreen implements Screen {

    private final ExpeditionGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(ExpeditionGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla en cada frame
        // Fondo negro
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        font.draw(batch, "EXPEDITION", 235, 380);

        // Opciones principales del menu
        font.draw(batch, "ENTER -> jugar", 220, 300);
        font.draw(batch, "I -> instrucciones", 220, 270);
        font.draw(batch, "C -> configuracion", 220, 240);
        batch.end();

        // Cambia a la pantalla de juego
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // todas las partidas comienzan desde el nivel 1
            game.resetToFirstLevel();
            game.setScreen(new GameScreen(game));
        }

        // Cambia a la pantalla de instrucciones
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            game.setScreen(new InstructionsScreen(game));
        }

        // Cambia a la pantalla de configuracion
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
        batch.dispose();
        font.dispose();
    }
}

