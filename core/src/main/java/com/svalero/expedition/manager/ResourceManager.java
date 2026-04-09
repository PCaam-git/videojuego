package com.svalero.expedition.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import javax.swing.*;

public class ResourceManager {

    private static final AssetManager manager = new AssetManager();

    public static void loadAllResources() {

        manager.load("player/player_idle.png", Texture.class);
        manager.load("dog/dog_idle.png", Texture.class);
        manager.load("bear/bear_idle.png", Texture.class);
        manager.load("deer/deer_walk_4.png", Texture.class);

        manager.finishLoading();
    }

    public static Texture getTexture(String path) {
        return manager.get(path, Texture.class);
    }

    public static AssetManager getManager() {
        return manager;
    }

    public static void dispose() {

        manager.dispose();
    }
}
