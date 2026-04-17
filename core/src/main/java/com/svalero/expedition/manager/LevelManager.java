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

    String scoreItemTexturePath;
    String poisonItemTexturePath;
    boolean birdAlwaysVisible;
    String relicMode;
    String relicTexturePath;

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

        Object propScoreItemTexture = map.getProperties().get("scoreItemTexture");
        scoreItemTexturePath = (propScoreItemTexture != null)
            ? propScoreItemTexture.toString()
            : "items/score_item_egg.png";

        Object propPoisonItemTexture = map.getProperties().get("poisonItemTexture");
        poisonItemTexturePath = (propPoisonItemTexture != null)
            ? propPoisonItemTexture.toString()
            : "items/poison_item_seed.png";

        Object propBirdAlwaysVisible = map.getProperties().get("birdAlwaysVisible");
        if (propBirdAlwaysVisible instanceof Boolean) {
            birdAlwaysVisible = (Boolean) propBirdAlwaysVisible;
        } else {
            birdAlwaysVisible = propBirdAlwaysVisible != null
                && propBirdAlwaysVisible.toString().equalsIgnoreCase("true");
        }

        Object propRelicMode = map.getProperties().get("relicMode");
        relicMode = (propRelicMode != null) ? propRelicMode.toString() : "texture";

        Object propRelicTexturePath = map.getProperties().get("relicTexturePath");
        relicTexturePath = (propRelicTexturePath != null)
            ? propRelicTexturePath.toString()
            : "relic/bone.png";

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

    public String getScoreItemTexturePath() {
        return scoreItemTexturePath;
    }

    public String getPoisonItemTexturePath() {
        return poisonItemTexturePath;
    }

    public boolean isBirdAlwaysVisible() {
        return birdAlwaysVisible;
    }

    public String getRelicMode() {
        return relicMode;
    }

    public String getRelicTexturePath() {
        return relicTexturePath;
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
