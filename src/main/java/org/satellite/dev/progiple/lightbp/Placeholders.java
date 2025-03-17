package org.satellite.dev.progiple.lightbp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;

public class Placeholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "lightbp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ProGiple";
    }

    @Override
    public @NotNull String getVersion() {
        return "latest";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        PlayerData playerData = PlayerData.getData().get(player.getName());
        if (playerData == null) playerData = new PlayerData(player.getName());

        if (params.equalsIgnoreCase("level")) {
            return String.valueOf(playerData.getInt("level"));
        }
        else if (params.equalsIgnoreCase("exp")) {
            return String.valueOf(playerData.getInt("exp"));
        }
        else if (params.equalsIgnoreCase("next_level")) {
            return String.valueOf(playerData.getInt("level") + 1);
        }
        else if (params.equalsIgnoreCase("need_exp")) {
            return String.valueOf(Config.getInt(String.format("levels.%s", playerData.getInt("level") + 1)));
        }

        return "";
    }
}
