package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.TextStyleManager;

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
        // Historia del juego
        font.draw(batch, "Emily, una joven exploradora, recorre un entorno natural junto a Frodo,", 80, 440);
        font.draw(batch, "su perro y compañero de aventuras", 80, 410);
        font.draw(batch, "para recuperar su hueso antes de volver a casa.", 80, 380);
        font.draw(batch, "¿Serán capaces de sortear todos los obstáculos del camino?", 80, 350);

        // Objetivos
        font.draw(batch, "OBJETIVOS", 80, 300);
        font.draw(batch, "Nivel 1: recupera el hueso escapando del jabalí.", 80, 280);
        font.draw(batch, "Nivel 2: llega a casa sorteando los obstáculos del camino.", 80, 260);

        // Objetos por nivel
        font.draw(batch, "OBJETOS", 80, 210);
        font.draw(batch, "Nivel 1 -> Huevo: +25 puntos | Manzana: inmunidad | Semilla: ralentiza", 80, 190);
        font.draw(batch, "Nivel 2 -> Naranja: +25 puntos | Fruta: impulso | Seta: ralentiza", 80, 170);


        // Controles
        font.draw(batch, "CONTROLES", 80, 120);
        font.draw(batch, "Flechas -> mover a la protagonista", 80, 100);
        font.draw(batch, "P -> llamar a Frodo y recuperar energía", 80, 80);

        // Navegacion
        font.draw(batch, "ESC -> volver al menu principal", 80, 40);
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
