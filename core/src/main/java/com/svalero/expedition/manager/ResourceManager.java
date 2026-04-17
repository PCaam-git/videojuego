package com.svalero.expedition.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class ResourceManager {

    private static final AssetManager manager = new AssetManager();
    private static boolean resourcesLoaded = false;
    private static Texture whitePixelTexture;

    public static void loadAllResources() {
        if (resourcesLoaded) {
            return;
        }

        // Atlas de personajes
        manager.load("player/player.atlas", TextureAtlas.class);
        manager.load("supply/supply.atlas", TextureAtlas.class);
        manager.load("boar/boar.atlas", TextureAtlas.class);
        manager.load("rabbit/rabbit.atlas", TextureAtlas.class);
        manager.load("bird/bird.atlas", TextureAtlas.class);
        manager.load("wasp/wasp.atlas", TextureAtlas.class);
        manager.load("friend/friend.atlas", TextureAtlas.class);

        manager.load("present/present.atlas", TextureAtlas.class);

        // Texturas sueltas necesarias
        manager.load("items/score_item_egg.png", Texture.class);
        manager.load("relic/bone.png", Texture.class);
        manager.load("items/immunity_item_apple.png", Texture.class);
        manager.load("items/poison_item_seed.png", Texture.class);
        manager.load("items/score_item_orange.png", Texture.class);
        manager.load("items/poison_item_mushroom.png", Texture.class);

        // Música de fondo
        manager.load("music/background_music.wav", Music.class);

        // Sonidos
        manager.load("sounds/button.wav", Sound.class);
        manager.load("sounds/call_supply.wav", Sound.class);
        manager.load("sounds/gameover.wav", Sound.class);
        manager.load("sounds/hurt.wav", Sound.class);
        manager.load("sounds/immunity_collect.wav", Sound.class);
        manager.load("sounds/level_victory.wav", Sound.class);
        manager.load("sounds/munching_food.wav", Sound.class);
        manager.load("sounds/relic_collect.wav", Sound.class);
        manager.load("sounds/score_collect.wav", Sound.class);
        manager.load("sounds/poison_collect.mp3", Sound.class);

        if (whitePixelTexture == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(1, 1, 1, 1);
            pixmap.fill();
            whitePixelTexture = new Texture(pixmap);
            pixmap.dispose();
        }

        manager.finishLoading();
        resourcesLoaded = true;
    }

    public static Texture getTexture(String path) {
        return manager.get(path, Texture.class);
    }

    public static TextureAtlas getAtlas(String path) {
        return manager.get(path, TextureAtlas.class);
    }

    public static TextureRegion getRegion(String atlasPath, String regionName) {
        return getAtlas(atlasPath).findRegion(regionName);
    }

    public static Array<TextureAtlas.AtlasRegion> getRegions(String atlasPath, String regionName) {
        return getAtlas(atlasPath).findRegions(regionName);
    }

    public static Texture getWhitePixelTexture() {
        return whitePixelTexture;
    }

    public static Music getMusic(String path) {
        return manager.get(path, Music.class);
    }

    public static Sound getSound(String path) {
        return manager.get(path, Sound.class);
    }

    public static AssetManager getManager() {
        return manager;
    }

    public static void dispose() {
        manager.dispose();

        if (whitePixelTexture != null) {
            whitePixelTexture.dispose();
            whitePixelTexture = null;
        }

        resourcesLoaded = false;
    }
}
