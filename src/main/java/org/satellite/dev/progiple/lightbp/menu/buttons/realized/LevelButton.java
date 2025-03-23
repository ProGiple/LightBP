package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import org.bukkit.entity.Player;
import org.satellite.dev.progiple.lightbp.Status;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

public class LevelButton extends Button {
    public LevelButton(Status status, int currentLevel, int nowExp, byte slot) {
        super(Config.getSection(String.format("items.%s", status == Status.NOT_OPENED ? "level_uncompleted" :
                (status == Status.THIS_IS ? "level_started" : "level_completed"))), slot);

        String name = this.getDisplayName();
        int needExp = Config.getInt(String.format("levels.%s", currentLevel));
        name = name.replace("{need_exp}", String.valueOf(needExp))
                .replace("{level}", String.valueOf(currentLevel))
                .replace("{exp}", String.valueOf(nowExp));
        this.setDisplayName(name);
    }

    @Override
    public boolean click(Player player) {
        return false;
    }
}
