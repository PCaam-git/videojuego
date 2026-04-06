package com.svalero.expedition;

import com.badlogic.gdx.Game;
import com.svalero.expedition.screen.MainMenuScreen;

public class ExpeditionGame extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this));
    }
}
