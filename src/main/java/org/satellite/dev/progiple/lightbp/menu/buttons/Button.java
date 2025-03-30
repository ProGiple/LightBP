package org.satellite.dev.progiple.lightbp.menu.buttons;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.Menus.Items.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class Button extends Item implements IButton {
    public Button(ConfigurationSection section, byte slot) {
        super(section, slot);
    }

    public Button(ConfigurationSection section) {
        super(section, section.getInt("slot"));
    }

    public Button(Material material, String displayName, List<String> lore, int amount, byte slot) {
        super(material, displayName, lore, amount, slot);
    }

    public void updateLore(OfflinePlayer player) {
        List<String> lore = new ArrayList<>(this.getLore());
        lore.replaceAll(s -> PlaceholderAPI.setPlaceholders(player, s));
        this.setLore(lore);
    }
}
