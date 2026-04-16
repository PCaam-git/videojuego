package com.svalero.expedition.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AtlasGenerator {

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();

        // Ajustes simples y seguros para esta primera versión
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.duplicatePadding = true;
        settings.rotation = false;
        settings.stripWhitespaceX = false;
        settings.stripWhitespaceY = false;
        settings.pot = true;

        TexturePacker.process(
            settings,
            "assets/atlas_source/player",
            "assets/player",
            "player"
        );

        System.out.println("Atlas generado correctamente en assets/player");
    }
}
