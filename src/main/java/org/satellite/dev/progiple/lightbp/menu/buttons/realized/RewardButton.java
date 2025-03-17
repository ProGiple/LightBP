package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.Util.Utils;
import org.satellite.dev.progiple.lightbp.Status;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.menu.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class RewardButton extends Button {
    private final boolean isPremium;
    private final List<String> commands;
    private final int level;
    private Status status;
    public RewardButton(ConfigurationSection section, Status status, OfflinePlayer player, boolean isPremium) {
        super(Material.getMaterial(Config.getString(String.format("items.%s",
                        status == Status.NOT_OPENED ? "reward_not_opened" : (status == Status.THIS_IS ?
                                "reward_opened" : "reward_collected")))),
                section.getString("displayName"),
                section.getStringList("lore"), 1,
                (byte) section.getInt("slot"));
        this.level = Utils.toInt(section.getName()
                .replace("PREMIUM_REWARD-", "")
                .replace("REWARD-", ""));
        this.isPremium = isPremium;
        this.commands = section.getStringList("commands");
        this.status = status;
        this.updateLore(player);
    }

    @Override
    public boolean click(Player player) {
        if (this.isPremium && !player.hasPermission("lightbp.premium")) {
            Config.sendMessage(player, "noPremium");
            return false;
        }
        if (this.commands == null || this.commands.isEmpty()) return false;

        switch (this.status) {
            case THIS_IS -> {
                this.commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        command.replace("{player}", player.getName())));
                this.status = Status.COMPLETED;
                this.setMaterial(Material.getMaterial(Config.getString("items.reward_collected")));

                PlayerData playerData = PlayerData.getData().get(player.getName());
                String path = this.isPremium ? "collected_premium_rewards" : "collected_default_rewards";

                List<Integer> list = new ArrayList<>(playerData.getIntList(path));
                list.add(this.level);
                playerData.set(path, list);
                playerData.save();

                Config.sendMessage(player, "collectReward");
                return true;
            }
            case NOT_OPENED -> Config.sendMessage(player, "rewardNotOpened");
            case COMPLETED -> Config.sendMessage(player, "rewardAlreadyCollected");
        }

        return false;
    }
}
