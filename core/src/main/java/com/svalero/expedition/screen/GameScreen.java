package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.*;


public class GameScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private final LogicManager logicManager; // actualiza el estado del juego
    private final LevelManager levelManager;
    private RenderManager renderManager; // responsable del dibujo
    private CameraManager cameraManager;

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
        logicManager.setWorldSize(levelManager.getWorldWidth(), levelManager.getWorldHeight());
        logicManager.setCollisionLayer(
            levelManager.getCollisionLayer(),
            levelManager.getTileWidth(),
            levelManager.getTileHeight()
        );

        cameraManager = new CameraManager(logicManager, levelManager);
        this.renderManager = new RenderManager(levelManager.getBatch(), logicManager, cameraManager);
    }

    @Override
    public void render(float delta) {
        // limpia la pantalla en cada frame
        // fondo gris oscuro
        ScreenUtils.clear(0, 0, 0.2f, 1);

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

        levelManager.getMapRenderer().render();
        renderManager.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
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

    }

    @Override
    public void dispose() {
        renderManager.dispose();
        levelManager.dispose();
        ResourceManager.dispose();
    }
}
