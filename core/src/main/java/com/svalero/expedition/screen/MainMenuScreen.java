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
        // limpia la pantalla en cada frame
        // Fondo negro
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        font.draw(batch, "EXPEDITION", 320, 440);
        font.draw(batch, "Emily, una joven exploradora, recorre el bosque junto a Frodo,", 80, 410);
        font.draw(batch, "su perro y compañero de aventura.", 80, 380);
        font.draw(batch, "En lo profundo del bosque se encuentra el hueso perdido,", 80, 350);
        font.draw(batch, "custodiado por un gran oso.", 80, 320);

        font.draw(batch, "OBJETIVO DEL NIVEL 1", 80, 270);
        font.draw(batch, "Recupera el hueso y evita los peligros del camino.", 80, 250);

        font.draw(batch, "OBJETOS DEL NIVEL", 80, 210);
        font.draw(batch, "Huevo: +25 puntos", 80, 190);
        font.draw(batch, "Manzana: inmunidad temporal", 80, 170);
        font.draw(batch, "Veneno: ralentiza al personaje", 80, 150);

        font.draw(batch, "CONTROLES", 80, 100);
        font.draw(batch, "Flechas -> mover a la protagonista", 80, 80);
        font.draw(batch, "P -> llamar a Frodo", 80, 60);

        font.draw(batch, "ENTER -> jugar", 400, 80);
        font.draw(batch, "C -> configuración", 400, 60);
        batch.end();

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
        batch.dispose();
        font.dispose();
    }
}

