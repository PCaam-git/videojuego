package com.svalero.expedition.manager;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextStyleManager {

    private static final float DARK_TEXT_R = 0.1f;
    private static final float DARK_TEXT_G = 0.1f;
    private static final float DARK_TEXT_B = 0.1f;
    private static final float DARK_TEXT_A = 1f;

    private TextStyleManager() {
    }

    // Fuente general para pantallas con un tamaño más cómodo
    public static BitmapFont createScreenFont() {
        BitmapFont font = new BitmapFont();
        font.setColor(DARK_TEXT_R, DARK_TEXT_G, DARK_TEXT_B, DARK_TEXT_A);
        font.getData().setScale(1);
        return font;
    }

    // Fuente algo mayor para textos principales o paneles
    public static BitmapFont createTitleFont() {
        BitmapFont font = new BitmapFont();
        font.setColor(DARK_TEXT_R, DARK_TEXT_G, DARK_TEXT_B, DARK_TEXT_A);
        font.getData().setScale(1);
        return font;
    }

    // Fuente para overlays tipo pausa
    public static BitmapFont createPauseFont() {
        BitmapFont font = new BitmapFont();
        font.setColor(DARK_TEXT_R, DARK_TEXT_G, DARK_TEXT_B, DARK_TEXT_A);
        font.getData().setScale(1);
        return font;
    }
}
