package org.satellite.dev.progiple.lightbp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;

public class ToggleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        PlayerData playerData = PlayerData.getData().get(commandSender.getName());
        if (playerData == null) playerData = new PlayerData(commandSender.getName());

        Config.sendMessage(commandSender, playerData.getBoolean("disabled_message") ? "showMessage" : "disableMessage");

        playerData.set("disabled_message", !playerData.getBoolean("disabled_message"));
        playerData.save();
        return true;
    }
}
