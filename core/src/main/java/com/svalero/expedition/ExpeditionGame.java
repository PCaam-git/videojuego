package com.svalero.expedition;

import com.badlogic.gdx.Game;
import com.svalero.expedition.screen.MainMenuScreen;

public class ExpeditionGame extends Game {

    private int currentLevel;

    @Override
    public void create() {
        // El juego siempre arranca en el nivel 1
        currentLevel = 1;
        setScreen(new MainMenuScreen(this));
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void goToNextLevel() {
        currentLevel++;
    }

    public void resetToFirstLevel() {
        currentLevel = 1;
    }
}
