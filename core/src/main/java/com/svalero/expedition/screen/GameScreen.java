package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.*;


public class GameScreen implements Screen {

    //TO DO. Revisar tamaño mapa
    private static final float MAP_SCALE = 1f;

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private final LogicManager logicManager; // actualiza el estado del juego
    private final LevelManager levelManager;
    private RenderManager renderManager; // responsable del dibujo
    private OrthographicCamera camera;

    public GameScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
        this.logicManager = new LogicManager();
        this.levelManager = new LevelManager();
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();

        levelManager.loadCurrentLevel();
        logicManager.loadMapObjects(levelManager.getObjectsLayer());
        this.renderManager = new RenderManager(levelManager.getBatch(), logicManager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    @Override
    public void render(float delta) {
        // limpia la pantalla en cada frame
        // fondo gris oscuro
        ScreenUtils.clear(0, 0, 0.2f, 1);

        logicManager.update(delta);

        if (logicManager.isLevelCompleted()) {
            game.setScreen(new VictoryScreen(game, logicManager.getPlayer().getScore()));
            return;
        }

        if (logicManager.isGameOver()) {
            game.setScreen(new GameOverScreen(game));
            return;
        }

        levelManager.getMapRenderer().setView(camera);
        levelManager.getMapRenderer().render();

        renderManager.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
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
        renderManager.dispose();
        levelManager.dispose();
        ResourceManager.dispose();
    }
}
