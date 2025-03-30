package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import org.novasparkle.lunaspring.API.Menus.MenuManager;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.menu.PMenu;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

import java.util.List;

public class BackPageButton extends Button {
    private final String backPageId;
    public BackPageButton(ConfigurationSection section, String pageConfigId) {
        super(section);

        List<String> list = Config.getList("page_order");
        int index = list.indexOf(pageConfigId);
        this.backPageId = index <= 0 ? null : list.get(index - 1);
    }

    @Override
    public boolean click(Player player) {
        if (this.backPageId == null) {
            Config.sendMessage(player, "noBackPage");
        }
        else MenuManager.openInventory(player, new PMenu(player, PageConfig.getPages().get(this.backPageId)));
        return false;
    }
}