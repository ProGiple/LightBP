package org.satellite.dev.progiple.lightbp.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Events.CooldownPrevent;
import org.novasparkle.lunaspring.Menus.AMenu;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

public class PMenu extends AMenu {
    private final ButtonSetter setter;
    private final CooldownPrevent<Integer> cooldown = new CooldownPrevent<>();
    public PMenu(Player player, PageConfig pageConfig) {
        super(player, pageConfig.getTitle(), pageConfig.getSize(), pageConfig.getSection("items.decorations"));
        this.setter = new ButtonSetter(player, pageConfig);
        this.cooldown.setCooldownMS(35);
    }

    public PMenu(Player player) {
        this(player, PageConfig.getPages().get(Config.getList("page_order").get(0)));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.setter.getButtons().forEach(button -> button.insert(this));
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        int slot = e.getSlot();
        if (item == null || item.getType() == Material.AIR) return;

        e.setCancelled(true);
        if (this.cooldown.isCancelled(e, slot)) return;
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
