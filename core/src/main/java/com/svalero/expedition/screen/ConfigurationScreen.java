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


public class ConfigurationScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private SpriteBatch batch;
    private BitmapFont font;

    public ConfigurationScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();
        batch = new SpriteBatch();
        font =new BitmapFont();
    }

    private void playButtonSound() {
        // Solo reproduce el sonido del botón si el sonido está activado
        if (!configurationManager.isSoundEnabled()) {
            return;
        }

        ResourceManager.getSound("sounds/button.wav").play();
    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla en cada frame
        // Fondo azul marino oscuro
        ScreenUtils.clear(0.1f, 0.1f, 0.3f, 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            configurationManager.setMusicEnabled(!configurationManager.isMusicEnabled());
            playButtonSound();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            boolean soundWasEnabled = configurationManager.isSoundEnabled();
            configurationManager.setSoundEnabled(!soundWasEnabled);

            // El sonido del botón se reproduce antes de quedar desactivado
            if (soundWasEnabled) {
                ResourceManager.getSound("sounds/button.wav").play();
            } else {
                playButtonSound();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        batch.begin();
        font.draw(batch, "CONFIGURACIÓN", 300, 420);
        font.draw(batch, "Pulsa M para activar o desactivar la musica", 160, 320);
        font.draw(batch, "Música: " + (configurationManager.isMusicEnabled() ? "activada" : "desactivada"), 160, 290);

        font.draw(batch, "Pulsa S para activar o desactivar el sonido", 160, 230);
        font.draw(batch, "Sonido: " + (configurationManager.isSoundEnabled() ? "activado" : "desactivado"), 160, 200);

        font.draw(batch, "ESC -> volver al menu principal", 160, 120);
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
