package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.svalero.expedition.screen.ConfigurationScreen;

public class ConfigurationManager {

    private static final String PREFERENCES_NAME = "Configuración de Expedition";
    private static final String MUSIC_ENABLED_KEY = "Activar música";
    private static final String SOUND_ENABLED_KEY = "Activar sonido";

    private final Preferences preferences;

    public ConfigurationManager() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public boolean isMusicEnabled() {
        return preferences.getBoolean(MUSIC_ENABLED_KEY, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        preferences.putBoolean(MUSIC_ENABLED_KEY, musicEnabled);
        preferences.flush();
    }

    public boolean isSoundEnabled() {
        return preferences.getBoolean(SOUND_ENABLED_KEY, true);
    }

    public void setSoundEnabledKey(boolean soundEnabled) {
        preferences.putBoolean(SOUND_ENABLED_KEY, soundEnabled);
        preferences.flush();
    }
}
