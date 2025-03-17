package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

import java.util.List;

public class CommandButton extends Button {
    private final List<String> commands;
    public CommandButton(ConfigurationSection section, Player player) {
        super(section);
        this.commands = section.getStringList("commands");
        this.updateLore(player);
    }

    @Override
    public boolean click(Player player) {
        this.commands.forEach(command -> {
            if (command.equalsIgnoreCase("close")) player.closeInventory();
            else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
        });
        return false;
    }
}
