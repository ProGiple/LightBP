package org.satellite.dev.progiple.lightbp;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.novasparkle.lunaspring.Util.LunaMath;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.menu.PMenu;

import java.util.List;
import java.util.stream.Collectors;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length >= 1) {
            if (commandSender.hasPermission("lightbp.admin")) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    Config.reload();
                    PlayerData.getData().values().forEach(PlayerData::reload);
                    PageConfig.load();
                    Config.sendMessage(commandSender, "reload");
                }
                else if (strings.length >= 3) {
                    PlayerData playerData = PlayerData.getData().get(strings[1]);
                    if (playerData == null) playerData = new PlayerData(strings[1]);

                    int amount = LunaMath.toInt(strings[2]);
                    switch (strings[0]) {
                        case "setExp" -> {
                            playerData.set("exp", 0);
                            playerData.save();

                            playerData.addExp(null, amount, false);
                            Config.sendMessage(commandSender, "editInfo", strings[1]);
                        }
                        case "addExp" -> {
                            playerData.addExp(null, amount, false);
                            Config.sendMessage(commandSender, "editInfo", strings[1]);
                        }
                        case "setLevel" -> {
                            playerData.set("level", amount);
                            playerData.save();
                            Config.sendMessage(commandSender, "editInfo", strings[1]);
                        }
                        case "addLevel" -> {
                            playerData.set("level", playerData.getInt("level") + amount);
                            playerData.save();
                            Config.sendMessage(commandSender, "editInfo", strings[1]);
                        }
                        default -> {
                            return false;
                        }
                    }
                }
                else return false;
            }
            else Config.sendMessage(commandSender, "noPermission");
        }
        else if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            MenuManager.openInventory(player, new PMenu(player));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("reload", "setExp", "addExp", "setLevel", "addLevel");
        }
        else if (strings.length == 2 && List.of("setExp", "addExp", "setLevel", "addLevel").contains(strings[0])) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .filter(nick -> nick.toUpperCase().startsWith(strings[1].toUpperCase()))
                    .collect(Collectors.toList());
        }
        else if (strings.length == 3) return List.of("<amount>");
        return List.of();
    }
}
