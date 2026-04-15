package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class LevelManager {

    private static final float MAP_SCALE = 2f; // mapa 16x16, escala similar a 32x32 que encaja mejor con los sprites elegidos

    private final SpriteBatch batch;

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    TiledMapTileLayer collisionLayer;
    MapLayer objectsLayer;

    public LevelManager() {
        this.batch = new SpriteBatch();
    }

    public void loadCurrentLevel(int currentLevel) {
        TmxMapLoader loader = new TmxMapLoader();

        String levelPath;
        if (currentLevel == 1) {
            levelPath = "level_01/mapa_nivel_01.tmx";
        } else {
            levelPath = "level_02/mapa_nivel_02.tmx";
        }

        map = loader.load(levelPath);

        collisionLayer = (TiledMapTileLayer) map.getLayers().get("Terrain");
        objectsLayer = map.getLayers().get("Objects");

        mapRenderer = new OrthogonalTiledMapRenderer(map, MAP_SCALE, batch);
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public MapLayer getObjectsLayer() {
        return objectsLayer;
    }

    public int getMapWidthInTiles() {
        return collisionLayer.getWidth();
    }

    public int getMapHeightInTiles() {
        return collisionLayer.getHeight();
    }

    public float getTileWidth() {
        return collisionLayer.getTileWidth() * MAP_SCALE;
    }

    public float getTileHeight() {
        return collisionLayer.getTileHeight() * MAP_SCALE;
    }

    public float getWorldWidth() {
        return getMapWidthInTiles() * getTileWidth();
    }

    public float getWorldHeight() {
        return getMapHeightInTiles() * getTileHeight();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void dispose() {
        mapRenderer.dispose();
        map.dispose();
        batch.dispose();
    }
}
