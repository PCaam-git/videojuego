package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import com.svalero.expedition.domain.Player;

public class CameraManager {

    private final OrthographicCamera camera;
    private final LogicManager logicManager;
    private final LevelManager levelManager;

    public CameraManager(LogicManager logicManager, LevelManager levelManager) {
        this.logicManager = logicManager;
        this.levelManager = levelManager;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        update();
    }

    public void update() {
        Player player = logicManager.getPlayer();

        float targetX = player.getX() + player.getWidth() / 2f;
        float targetY = player.getY() + player.getHeight() / 2f;

        float halfViewportWidth = camera.viewportWidth / 2f;
        float halfViewportHeight = camera.viewportHeight / 2f;

        float worldWidth = levelManager.getWorldWidth();
        float worldHeight = levelManager.getWorldHeight();

        float cameraX = MathUtils.clamp(targetX, halfViewportWidth, worldWidth - halfViewportWidth);
        float cameraY = MathUtils.clamp(targetY, halfViewportHeight, worldHeight - halfViewportHeight);

        if (worldWidth <= camera.viewportWidth) {
            cameraX = worldWidth / 2f;
        }

        if (worldHeight <= camera.viewportHeight) {
            cameraY = worldHeight / 2f;
        }

        camera.position.set(cameraX, cameraY, 0);
        camera.update();
        levelManager.getMapRenderer().setView(camera);
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
