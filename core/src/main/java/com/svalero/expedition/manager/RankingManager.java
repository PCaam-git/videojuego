package com.svalero.expedition.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.svalero.expedition.domain.RankingEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingManager {

    private static final String PREFERENCES_NAME = "expedition_ranking";
    private static final String RANKING_KEY = "top10";
    private static final int MAX_ENTRIES = 10;

    private final Preferences preferences;

    public RankingManager() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public void saveScore(String playerName, int score) {
        List<RankingEntry> rankingEntries = getRankingEntries();

        // Limpia caracteres problemáticos para el formato simple usado
        String sanitizedName = playerName.replace("|", "").replace("\n", "").trim();

        if (sanitizedName.isEmpty()) {
            sanitizedName = "PLAYER";
        }

        rankingEntries.add(new RankingEntry(sanitizedName, score));

        rankingEntries.sort(Comparator.comparingInt(RankingEntry::getScore).reversed());

        while (rankingEntries.size() > MAX_ENTRIES) {
            rankingEntries.remove(rankingEntries.size() - 1);
        }

        saveRankingEntries(rankingEntries);
    }

    public List<RankingEntry> getRankingEntries() {
        String rawData = preferences.getString(RANKING_KEY, "");
        List<RankingEntry> rankingEntries = new ArrayList<>();

        if (rawData.isEmpty()) {
            return rankingEntries;
        }

        String[] lines = rawData.split("\n");

        for (String line : lines) {
            if (line.isEmpty() || !line.contains("|")) {
                continue;
            }

            String[] parts = line.split("\\|");

            if (parts.length != 2) {
                continue;
            }

            String playerName = parts[0];

            try {
                int score = Integer.parseInt(parts[1]);
                rankingEntries.add(new RankingEntry(playerName, score));
            } catch (NumberFormatException ignored) {
            }
        }

        return rankingEntries;
    }

    private void saveRankingEntries(List<RankingEntry> rankingEntries) {
        StringBuilder builder = new StringBuilder();

        for (RankingEntry rankingEntry : rankingEntries) {
            builder.append(rankingEntry.getPlayerName())
                .append("|")
                .append(rankingEntry.getScore())
                .append("\n");
        }

        preferences.putString(RANKING_KEY, builder.toString());
        preferences.flush();
    }
}
