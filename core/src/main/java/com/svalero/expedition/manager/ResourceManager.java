package com.svalero.expedition.manager;

import com.badlogic.gdx.assets.AssetManager;

public class ResourceManager {

    private static final AssetManager manager = new AssetManager();

    public static void loadAllResources() {

    }

    public static AssetManager getManager() {
        return manager;
    }

    public static void dispose() {
        manager.dispose();
    }
}
