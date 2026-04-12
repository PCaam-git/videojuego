package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import com.svalero.expedition.domain.Player;

public class CameraManager {

    // La cámara  que proyecta el juego en 2D
    private final OrthographicCamera camera;

    private final LogicManager logicManager;
    private final LevelManager levelManager;

    public CameraManager(LogicManager logicManager, LevelManager levelManager) {
        this.logicManager = logicManager;
        this.levelManager = levelManager;

        camera = new OrthographicCamera();

        // Configuración de la cámara para que coincida con el tamaño inicial de la ventana.
        // El parámetro 'false' indica que el eje Y crece hacia arriba.
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Actualización para posicionarla correctamente al inicio
        update();
    }

     // Se encarga de seguir a la jugadora y evitar que la cámara muestre zonas fuera del mapa.

    public void update() {
        // Referencia de la jugadora actual
        Player player = logicManager.getPlayer();

        // Cálculo del punto central de la jugadora.
        // Suma de la mitad de su anchura y altura a su posición (X, Y) inferior izquierda.
        float targetX = player.getX() + player.getWidth() / 2f;
        float targetY = player.getY() + player.getHeight() / 2f;

        // Cálculo de la mitad del tamaño de la visión de la cámara.
        // Es necesario para saber a qué distancia del centro de la cámara están los bordes de la pantalla.
        float halfViewportWidth = camera.viewportWidth / 2f;
        float halfViewportHeight = camera.viewportHeight / 2f;

        // Obtención de las dimensiones totales del mapa (en píxeles, considerando la escala)
        float worldWidth = levelManager.getWorldWidth();
        float worldHeight = levelManager.getWorldHeight();

        // MathUtils.clamp asegura que el valor de la cámara nunca sea menor que el borde izquierdo/inferior
        // ni mayor que el borde derecho/superior del mapa.
        float cameraX = MathUtils.clamp(targetX, halfViewportWidth, worldWidth - halfViewportWidth);
        float cameraY = MathUtils.clamp(targetY, halfViewportHeight, worldHeight - halfViewportHeight);

        // Si el mapa es más pequeño que la ventana del juego, se centra la cámara estáticamente en medio del mapa.

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

    /**
     * Método para reajustar la cámara si la ventana del juego cambia de tamaño.
     * Evita que los gráficos se estiren o se deformen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

