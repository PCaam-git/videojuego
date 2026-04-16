package com.svalero.expedition;

import com.badlogic.gdx.Game;
import com.svalero.expedition.screen.MainMenuScreen;

public class ExpeditionGame extends Game {

    private int currentLevel;
    private int accumulatedScore;

    @Override
    public void create() {
        // El juego siempre arranca en el nivel 1
        currentLevel = 1;
        accumulatedScore = 0;
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
        accumulatedScore = 0;
    }

    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    public void setAccumulatedScore(int accumulatedScore) {
        this.accumulatedScore = accumulatedScore;
    }

    public void addToAccumulatedScore(int score) {
        accumulatedScore += score;
    }

    public void resetAccumulatedScore() {
        accumulatedScore = 0;
    }
}
