package org.satellite.dev.progiple.lightbp.progress;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;

import java.util.*;

@Getter
public abstract class BasicHandler implements Listener {
    @Getter private static final Set<BasicHandler> handlers = new HashSet<>();

    private EventCooldown cooldown;
    private String[] exp;
    private int maxTimes;
    private List<String> whitelist;

    private final Map<String, Integer> times = new HashMap<>();
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

        String nick = player.getName();
        int valueTime = this.times.getOrDefault(nick, 0);
        if (valueTime + 1 < this.maxTimes) {
            this.times.put(nick, valueTime + 1);
            return;
        }

        int exp = LunaMath.getRandom().nextInt(
                LunaMath.toInt(this.exp[1]) - LunaMath.toInt(this.exp[0])) + LunaMath.toInt(this.exp[0]);
        PlayerData playerData = PlayerData.getData().get(player.getName());
        if (playerData == null) playerData = new PlayerData(player.getName());
        playerData.addExp(player, exp, true);

        this.times.put(nick, 0);
        this.cooldown.put(player);
    }
}
