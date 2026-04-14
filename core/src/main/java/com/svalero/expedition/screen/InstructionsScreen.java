package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.svalero.expedition.ExpeditionGame;

public class InstructionsScreen implements Screen{
    private final ExpeditionGame game;
    private SpriteBatch batch;
    private BitmapFont font;

    public InstructionsScreen(ExpeditionGame game) {
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
        // Fondo azul oscuro para diferenciarlo del menu principal
        ScreenUtils.clear(0.05f, 0.05f, 0.2f, 1);

        // Vuelve al menu principal
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        batch.begin();
        font.draw(batch, "INSTRUCCIONES", 300, 440);

        // Historia del juego
        font.draw(batch, "Emily, una joven exploradora, recorre el bosque junto a Frodo,", 80, 390);
        font.draw(batch, "su perro y compañero de aventuras.", 80, 360);
        font.draw(batch, "En lo profundo del bosque se encuentra el hueso perdido,", 80, 330);
        font.draw(batch, "custodiado por un gran oso.", 80, 300);

        // Objetivo del nivel
        font.draw(batch, "OBJETIVO DEL NIVEL 1", 80, 250);
        font.draw(batch, "Recupera el hueso y evita los peligros del camino.", 80, 220);

        // Objetos del nivel
        font.draw(batch, "OBJETOS DEL NIVEL", 80, 180);
        font.draw(batch, "Huevo: +25 puntos", 80, 150);
        font.draw(batch, "Manzana: inmunidad", 80, 120);
        font.draw(batch, "Veneno: ralentiza al personaje", 80, 90);

        // Controles
        font.draw(batch, "CONTROLES", 380, 180);
        font.draw(batch, "Flechas -> mover a la protagonista", 380, 150);
        font.draw(batch, "P -> llamar a Frodo y recuperar energía", 380, 120);

        // Navegacion
        font.draw(batch, "ESC -> volver al menu principal", 220, 40);
        batch.end();
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
