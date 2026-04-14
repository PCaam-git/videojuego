package com.svalero.expedition.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.svalero.expedition.ExpeditionGame;
import com.svalero.expedition.domain.Player;

public class VictoryScreen implements Screen {

    private final ExpeditionGame game;
    private final Player finalPlayerState;
    private SpriteBatch batch;
    private BitmapFont font;

    private final int baseScore;
    private final int livesBonus;
    private final int energyBonus;
    private final int immunityBonus;
    private final int poisonPenalty;
    private final int totalScore;

    public VictoryScreen(ExpeditionGame game, Player player) {
        this.game = game;
        this.finalPlayerState = player;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();

        // puntuación con la que cruzó la meta
        this.baseScore = player.getScore();

        // bonus
        this.livesBonus = player.getLives() * 100;
        this.energyBonus = player.getEnergy() * 2;
        this.immunityBonus = player.getImmunityCollected() * 500;

        // penalización
        this.poisonPenalty = player.getPoisonCollected() * 200;

        // suma total de puntos
        this.totalScore = baseScore + livesBonus + energyBonus + immunityBonus - poisonPenalty;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.5f, 0, 1);

        batch.begin();

        font.draw(batch, "¡NIVEL COMPLETADO!", 220, 300);

        //Recuento desglosado
        font.draw(batch, "Puntuación de objetos: " + baseScore, 300, 430);
        font.draw(batch, "Bonus por Vidas restantes (" + finalPlayerState.getLives() + " x 100): +" + livesBonus, 300, 400);
        font.draw(batch, "Bonus por Energía restante (" + finalPlayerState.getEnergy() + " x 2): +" + energyBonus, 300, 370);
        font.draw(batch, "Bonus por objeto de Inmunidad (" + finalPlayerState.getImmunityCollected() + " x 500): +" + immunityBonus, 300, 340);
        font.draw(batch, "Penalización Veneno (" + finalPlayerState.getPoisonCollected() + " x 200): -" + poisonPenalty, 220, 310);

        // El resultado final
        font.draw(batch, "-----------------------------------", 300, 310);
        font.draw(batch, "PUNTUACIÓN TOTAL: " + totalScore, 300, 280);

        font.draw(batch, "Pulsa ENTER para continuar", 300, 150);

        batch.end();

        // Lógica para ir al menú principal o al siguiente nivel
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override public void resize(int width, int height) {

    }

    @Override public void pause() {

    }

    @Override public void resume() {

    }

    @Override public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
