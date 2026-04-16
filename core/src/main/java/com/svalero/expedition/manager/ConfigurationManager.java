package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConfigurationManager {

    private static final String PREFERENCES_NAME = "configuracion_expedition";
    private static final String MUSIC_ENABLED_KEY = "musica_activada";
    private static final String SOUND_ENABLED_KEY = "sonido_activado";

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

    public void setSoundEnabled(boolean soundEnabled) {
        preferences.putBoolean(SOUND_ENABLED_KEY, soundEnabled);
        preferences.flush();
    }
}
