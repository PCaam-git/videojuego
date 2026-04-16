package com.svalero.expedition.lwjgl3;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AtlasGenerator {

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();

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
            "assets/atlas_source/supply",
            "assets/supply",
            "supply"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/bird",
            "assets/bird",
            "bird"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/rabbit",
            "assets/rabbit",
            "rabbit"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/wasp",
            "assets/wasp",
            "wasp"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/boar",
            "assets/boar",
            "boar"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/friend",
            "assets/friend",
            "friend"
        );

        TexturePacker.process(
            settings,
            "assets/atlas_source/present",
            "assets/present",
            "present"
        );

    }
}
