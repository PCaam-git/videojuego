package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.manager.ConfigurationManager;
import com.svalero.expedition.manager.LogicManager;
import com.svalero.expedition.manager.RenderManager;
import com.svalero.expedition.manager.ResourceManager;


public class GameScreen implements Screen {

    private final ExpeditionGame game;
    private final ConfigurationManager configurationManager;
    private final LogicManager logicManager; // actualiza el estado del juego
    private RenderManager renderManager; // responsable del dibujo
    private final SpriteBatch batch;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    public GameScreen(ExpeditionGame game) {
        this.game = game;
        this.configurationManager = new ConfigurationManager();
        this.logicManager = new LogicManager();
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {
        ResourceManager.loadAllResources();
        this.renderManager = new RenderManager(batch, logicManager);

        tiledMap = new TmxMapLoader().load("level_01/mapa_nivel_01.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

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

        mapRenderer.setView(camera);
        mapRenderer.render();

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
        mapRenderer.dispose();
        tiledMap.dispose();
        batch.dispose();
        ResourceManager.dispose();
    }
}
