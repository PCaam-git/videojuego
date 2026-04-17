package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.domain.RankingEntry;
import com.svalero.expedition.manager.RankingManager;
import com.svalero.expedition.manager.TextStyleManager;

import java.util.List;

public class RankingScreen implements Screen {

    private final ExpeditionGame game;
    private final RankingManager rankingManager;

    private SpriteBatch batch;
    private BitmapFont font;

    public RankingScreen(ExpeditionGame game) {
        this.game = game;
        this.rankingManager = new RankingManager();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        List<RankingEntry> rankingEntries = rankingManager.getRankingEntries();

        batch.begin();

        font.draw(batch, "TOP 10 - EXPEDITION", 220, 420);

        if (rankingEntries.isEmpty()) {
            font.draw(batch, "Todavía no hay puntuaciones guardadas", 180, 350);
        } else {
            int y = 360;

            for (int i = 0; i < rankingEntries.size(); i++) {
                RankingEntry rankingEntry = rankingEntries.get(i);
                font.draw(batch, (i + 1) + ". " + rankingEntry.getPlayerName() + " - " + rankingEntry.getScore(), 180, y);
                y -= 25;
            }
        }

        font.draw(batch, "ESC -> Volver al menu principal", 180, 80);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
