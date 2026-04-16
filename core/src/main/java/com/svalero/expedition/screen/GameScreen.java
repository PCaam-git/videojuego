package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.*;


public class GameScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private final LogicManager logicManager; // actualiza el estado del juego
    private final LevelManager levelManager;
    private RenderManager renderManager; // responsable del dibujo
    private CameraManager cameraManager;
    private Music backgroundMusic;
    private SpriteBatch pauseBatch;
    private BitmapFont pauseFont;
    private boolean paused;

    public GameScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
        this.logicManager = new LogicManager(game.getCurrentLevel());
        this.levelManager = new LevelManager();
        this.paused = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();

        // Carga el TMX correspondiente al nivel actual del juego
        levelManager.loadCurrentLevel(game.getCurrentLevel());
        logicManager.loadMapObjects(levelManager.getObjectsLayer());
        logicManager.setWorldSize(levelManager.getWorldWidth(), levelManager.getWorldHeight());
        logicManager.setCollisionLayer(
            levelManager.getCollisionLayer(),
            levelManager.getTileWidth(),
            levelManager.getTileHeight()
        );

        cameraManager = new CameraManager(logicManager, levelManager);
        this.renderManager = new RenderManager(levelManager.getBatch(), logicManager, cameraManager);

        // Prepara la música de fondo del nivel
        backgroundMusic = ResourceManager.getMusic("music/background_music.wav");
        backgroundMusic.setLooping(true);

        // Solo se reproduce si la configuración está activada
        if (configurationManager.isMusicEnabled()) {
            backgroundMusic.play();
        }

        pauseBatch = new SpriteBatch();
        pauseFont = new BitmapFont();

        // Si se vuelve desde configuración, mantiene el estado pausado de la partida
        if (paused) {
            backgroundMusic.pause();
        }
    }

    private void updateBackgroundMusicState() {
        if (backgroundMusic == null) {
            return;
        }

        // Si el juego está pausado, la música queda detenida temporalmente
        if (paused) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
            }
            return;
        }

        // Mantiene la música sincronizada con la preferencia guardada
        if (configurationManager.isMusicEnabled()) {
            if (!backgroundMusic.isPlaying()) {
                backgroundMusic.play();
            }
        } else {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
        }
    }

    private void handlePauseInput() {
        // ESC alterna entre pausa y reanudación
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

        // Si no está pausado, no se muestran opciones de pausa
        if (!paused) {
            return;
        }

        // Reanuda la partida
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            paused = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            game.setScreen(new ConfigurationScreen(game, this));
        }

        // Vuelve al menú principal
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Cierra el juego
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            Gdx.app.exit();
        }
    }

    private void renderPauseOverlay() {
        pauseBatch.begin();

        // Panel oscuro semitransparente para que el texto quede por encima visualmente
        pauseBatch.setColor(0f, 0f, 0f, 0.75f);
        pauseBatch.draw(ResourceManager.getWhitePixelTexture(), 150, 110, 360, 250);

        // Se restaura el color normal para dibujar el texto
        pauseBatch.setColor(1f, 1f, 1f, 1f);

        pauseFont.draw(pauseBatch, "JUEGO EN PAUSA", 250, 320);
        pauseFont.draw(pauseBatch, "R -> Reanudar", 250, 270);
        pauseFont.draw(pauseBatch, "C -> Configuración", 250, 240);
        pauseFont.draw(pauseBatch, "M -> Menu principal", 250, 210);
        pauseFont.draw(pauseBatch, "X -> Salir", 250, 180);
        pauseFont.draw(pauseBatch, "ESC -> Continuar", 250, 150);

        pauseBatch.end();
    }

    @Override
    public void render(float delta) {
        // Usa un color de limpieza parecido al fondo del mapa para evitar franjas azules visibles
        //ScreenUtils.clear(0.78f, 0.88f, 0.52f, 1);

        // Mantiene la música con la preferencia guardada
        updateBackgroundMusicState();

        handlePauseInput();

        // Solo se actualiza la lógica si la partida no está pausada
        if (!paused) {
            logicManager.update(delta);

            if (logicManager.isLevelCompleted()) {
                game.setScreen(new VictoryScreen(game, logicManager.getPlayer()));
                return;
            }

            if (logicManager.isGameOver()) {
                game.setScreen(new GameOverScreen(game));
                return;
            }

            cameraManager.update();
        }

        // Se sigue dibujando el mundo aunque esté pausado
        levelManager.getMapRenderer().render();
        renderManager.render();

        // Dibuja el menú de pausa por encima del juego
        if (paused) {
            renderPauseOverlay();
        }
    }

    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // Detiene la música al salir de la pantalla de juego
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    @Override
    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }

        if (pauseBatch != null) {
            pauseBatch.dispose();
        }

        if (pauseFont != null) {
            pauseFont.dispose();
        }

        renderManager.dispose();
        levelManager.dispose();
        ResourceManager.dispose();
    }
}
