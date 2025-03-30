package org.satellite.dev.progiple.lightbp.menu;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.novasparkle.lunaspring.API.Menus.MenuManager;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;
import org.satellite.dev.progiple.lightbp.Status;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;
import org.satellite.dev.progiple.lightbp.menu.buttons.realized.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ButtonSetter {
    private final List<Button> buttons = new ArrayList<>();
    public ButtonSetter(Player player, PageConfig pageConfig) {
        String nick = player.getName();
        PlayerData playerData = PlayerData.getData().get(nick);
        if (playerData == null) playerData = new PlayerData(nick);
        int playerLevel = playerData.getInt("level");

        this.registerItems(playerData, pageConfig, playerLevel, player);
        this.registerLevels(playerData, pageConfig, playerLevel);
    }

    private void registerItems(PlayerData playerData, PageConfig pageConfig, int playerLevel, Player player) {
        ConfigurationSection section = pageConfig.getSection("items.clickable");
        if (section != null) for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            Button button = null;
            if (key.equalsIgnoreCase("NEXT_PAGE")) {
                button = new NextPageButton(itemSection, pageConfig.getId());
            }
            else if (key.equalsIgnoreCase("BACK_PAGE")) {
                button = new BackPageButton(itemSection, pageConfig.getId());
            }
            else if (key.equalsIgnoreCase("CLOSE")) {
                button = new Button(itemSection) {
                    @Override
                    public boolean click(Player player) {
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                        return false;
                    }
                };
            }
            else if (key.startsWith("PAGE-")) {
                String pageId = key.replace("PAGE-", "");

                PageConfig pageConfig1 = PageConfig.getPages().get(pageId);
                if (pageConfig1 != null) {
                    button = new Button(itemSection) {
                        @Override
                        public boolean click(Player player) {
                            MenuManager.openInventory(player, new PMenu(player, pageConfig1));
                            return false;
                        }
                    };
                }
            }
            else if (key.startsWith("REWARD-")) {
                int level = LunaMath.toInt(key.replace("REWARD-", ""));
                Status status = playerData.getIntList("collected_default_rewards").contains(level) ? Status.COMPLETED :
                        (playerLevel >= level ? Status.THIS_IS : Status.NOT_OPENED);
                button = new RewardButton(itemSection, status, false);
            }
            else if (key.startsWith("PREMIUM_REWARD-")) {
                int level = LunaMath.toInt(key.replace("PREMIUM_REWARD-", ""));
                Status status = playerData.getIntList("collected_premium_rewards").contains(level) ? Status.COMPLETED :
                        (playerLevel >= level ? Status.THIS_IS : Status.NOT_OPENED);
                button = new RewardButton(itemSection, status, true);
            }
            else {
                String[] name = key.split("-");
                String permission = name.length >= 2 && name[0].contains("PERMISSION") ? name[1] : null;
                if (permission != null) permission = permission.replace("=", "\\.");

                boolean requiredPermission = name[0].contains("HAS");
                if (permission != null) {
                    boolean hasPerm = player.hasPermission(permission);
                    if (!hasPerm && requiredPermission || hasPerm && !requiredPermission) continue;
                }

                button = new CommandButton(itemSection);
            }

            if (button != null) {
                button.updateLore(player);
                this.buttons.add(button);
            }
        }
    }

    private void registerLevels(PlayerData playerData, PageConfig pageConfig, int playerLevel) {
        ConfigurationSection levelSection = pageConfig.getSection("items.levels");
        int exp = playerData.getInt("exp");
        for (String key : levelSection.getKeys(false)) {
            byte slot = (byte) levelSection.getInt(key);
            int level = LunaMath.toInt(key);

            LevelButton levelButton = new LevelButton(level - 1 == playerLevel ? Status.THIS_IS :
                    (playerLevel > level - 1 ? Status.COMPLETED : Status.NOT_OPENED), level, exp, slot);
            this.buttons.add(levelButton);
        }
    }
}
