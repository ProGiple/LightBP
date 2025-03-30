package org.satellite.dev.progiple.lightbp.menu;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.Events.CooldownPrevent;
import org.novasparkle.lunaspring.API.Menus.AMenu;
import org.novasparkle.lunaspring.API.Menus.Items.Decoration;
import org.novasparkle.lunaspring.API.Menus.Items.Item;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class PMenu extends AMenu {
    private final ButtonSetter setter;
    private final CooldownPrevent<Integer> cooldownPrevent = new CooldownPrevent<>();
    public PMenu(Player player, PageConfig pageConfig) {
        super(player, pageConfig.getTitle(), pageConfig.getSize(), pageConfig.getSection("items.decorations"));
        this.setter = new ButtonSetter(player, pageConfig);
        this.cooldownPrevent.setCooldownMS(55);
    }

    public PMenu(Player player) {
        this(player, PageConfig.getPages().get(Config.getList("page_order").get(0)));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.setter.getButtons().forEach(button -> button.insert(this));

        Decoration decorations = this.getDecoration();
        for (Item decoration : decorations.getDecorationItems()) {
            List<String> lore = new ArrayList<>(decoration.getLore());
            lore.replaceAll(s -> PlaceholderAPI.setPlaceholders(this.getPlayer(), s));
            decoration.setLore(lore);
        }
        this.setDecoration(decorations);
        this.getDecoration().insert(this);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        e.setCancelled(true);
        if (this.cooldownPrevent.isCancelled(null, e.getSlot())) return;
        for (Button button : this.setter.getButtons()) {
            if (button.checkId(item)) {
                if (button.click(this.getPlayer())) button.insert(this);
                return;
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
    }
}
