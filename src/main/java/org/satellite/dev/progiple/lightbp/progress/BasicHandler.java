package org.satellite.dev.progiple.lightbp.progress;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.novasparkle.lunaspring.Util.Utils;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class BasicHandler implements Listener {
    @Getter private static final Set<BasicHandler> handlers = new HashSet<>();

    private EventCooldown cooldown;
    private String[] exp;
    private int maxTimes;
    private List<String> whitelist;
    private int times = 0;

    private final String sectionId;
    public BasicHandler(String sectionId) {
        this.sectionId = sectionId;
        this.update();
        handlers.add(this);
    }

    public void update() {
        ConfigurationSection configSection = Config.getSection(String.format("exp_farm.%s", this.sectionId));
        int cooldownMs = configSection.getInt("cooldown");
        if (this.cooldown == null) this.cooldown = new EventCooldown(cooldownMs);
        else this.cooldown.setCooldownMS(cooldownMs);

        this.exp = Objects.requireNonNull(configSection.getString("exp")).split("-");
        this.maxTimes = configSection.getInt("amount");
        this.whitelist = configSection.getStringList("whitelist");
    }

    public void progress(Player player, String value) {
        if (player == null || this.cooldown.cancel(player) ||
                (this.whitelist != null && !this.whitelist.isEmpty() && !this.whitelist.contains(value))) return;

        if (this.times + 1 < this.maxTimes) {
            this.times++;
            return;
        }

        int exp = Utils.getRandom().nextInt(Utils.toInt(this.exp[1]) - Utils.toInt(this.exp[0])) + Utils.toInt(this.exp[0]);
        PlayerData playerData = PlayerData.getData().get(player.getName());
        if (playerData == null) playerData = new PlayerData(player.getName());
        playerData.addExp(player, exp, true);

        this.times = 0;
        this.cooldown.put(player);
    }
}
