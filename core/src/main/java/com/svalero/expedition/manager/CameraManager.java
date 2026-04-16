package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import com.svalero.expedition.domain.Player;

public class CameraManager {

    // Tamaño fijo de la cámara. 15 Tiles de 32 pixeles de ancho y algo
    private static final float VIEWPORT_WIDTH = 15f * 32f;
    private static final float VIEWPORT_HEIGHT = 15f * 32f;
    // La cámara  que proyecta el juego en 2D
    private final OrthographicCamera camera;

    private final LogicManager logicManager;
    private final LevelManager levelManager;

    public CameraManager(LogicManager logicManager, LevelManager levelManager) {
        this.logicManager = logicManager;
        this.levelManager = levelManager;

        // Usa un tamaño fijo del mundo
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        // Actualización para posicionarla correctamente al inicio
        update();
    }

    // Se encarga de seguir a la jugadora y evitar que la cámara muestre zonas fuera del mapa
    public void update() {
        // Referencia de la jugadora actual
        Player player = logicManager.getPlayer();

        // Cálculo del punto central de la jugadora
        float targetX = player.getX() + player.getWidth() / 2f;
        float targetY = player.getY() + player.getHeight() / 2f;

        // Mitad del tamaño visible de la cámara
        float halfViewportWidth = camera.viewportWidth / 2f;
        float halfViewportHeight = camera.viewportHeight / 2f;

        // Dimensiones totales del mapa
        float worldWidth = levelManager.getWorldWidth();
        float worldHeight = levelManager.getWorldHeight();

        // La cámara no puede mostrar zonas fuera del mapa
        float cameraX = MathUtils.clamp(targetX, halfViewportWidth, worldWidth - halfViewportWidth);
        float cameraY = MathUtils.clamp(targetY, halfViewportHeight, worldHeight - halfViewportHeight);

        // Si el mapa es más pequeño que el viewport, se centra la cámara
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


     // Mantiene el mismo tamaño de cámara aunque cambie la ventana.
     // Así se evita que el área visible del mundo dependa del tamaño real de la pantalla.

    public void resize(int width, int height) {
        camera.viewportWidth = VIEWPORT_WIDTH;
        camera.viewportHeight = VIEWPORT_HEIGHT;
        update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

