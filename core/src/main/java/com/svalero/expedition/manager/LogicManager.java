package com.svalero.expedition.manager;

import com.svalero.expedition.domain.Player;

public class LogicManager {

    private Player player;

    // Ubicación del jugador
    public LogicManager() {
        player = new Player(100,100,150,100,0);
    }

    public void update(float delta) {
        player.update(delta);
    }

    public Player getPlayer() {
        return player;
    }
}
