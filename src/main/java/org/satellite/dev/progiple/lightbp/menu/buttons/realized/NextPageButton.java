package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.Menus.MenuManager;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.menu.PMenu;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

import java.util.List;

public class NextPageButton extends Button {
    private final String nextPageId;
    public NextPageButton(ConfigurationSection section, String pageConfigId, OfflinePlayer player) {
        super(section);

        List<String> list = Config.getList("page_order");
        int index = list.indexOf(pageConfigId);
        this.nextPageId = list.size() > index + 1 ? list.get(index + 1) : null;
        this.updateLore(player);
    }

    @Override
    public boolean click(Player player) {
        if (this.nextPageId == null) {
            Config.sendMessage(player, "noNextPage");
        }
        else MenuManager.openInventory(player, new PMenu(player, PageConfig.getPages().get(this.nextPageId)));
        return false;
    }
}
