package org.satellite.dev.progiple.lightbp.menu.buttons.realized;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
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
    public RewardButton(ConfigurationSection section, Status status, boolean isPremium) {
        super(Material.getMaterial(Config.getString(String.format("items.%s",
                        status == Status.NOT_OPENED ? "reward_not_opened" : (status == Status.THIS_IS ?
                                "reward_opened" : "reward_collected")))),
                section.getString("displayName"),
                section.getStringList("lore"), 1,
                (byte) section.getInt("slot"));
        this.setGlowing(section.getBoolean("enchanted"));

        this.level = LunaMath.toInt(section.getName()
                .replace("PREMIUM_REWARD-", "")
                .replace("REWARD-", ""));
        this.isPremium = isPremium;
        this.commands = section.getStringList("commands");
        this.status = status;

        if (this.status == Status.COMPLETED)
            this.setDisplayName(this.getDisplayName() + Utils.color(Config.getString("messages.reward_is_collected")));
        else if (this.status == Status.NOT_OPENED)
            this.setDisplayName(this.getDisplayName() + Utils.color(Config.getString("messages.reward_denied")));
    }

    @Override @SuppressWarnings("deprecation")
    public boolean click(Player player) {
        if (this.isPremium && !player.hasPermission("lightbp.premium")) {
            Config.sendMessage(player, "noPremium");
            return false;
        }
        if (this.commands == null || this.commands.isEmpty()) return false;

        switch (this.status) {
            case THIS_IS -> {
                this.commands.forEach(command -> {
                    String line = command
                            .replace("broadcast ", "")
                            .replace("msg ", "")
                            .replace("{player}", player.getName());
                    if (command.startsWith("broadcast ")) Bukkit.broadcastMessage(Utils.color(line));
                    else if (command.startsWith("msg ")) player.sendMessage(Utils.color(line));
                    else Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
                });
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
