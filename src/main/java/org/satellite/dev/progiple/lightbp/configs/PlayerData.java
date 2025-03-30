package org.satellite.dev.progiple.lightbp.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.Configuration.Configuration;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;
import org.satellite.dev.progiple.lightbp.LightBP;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerData {
    @Getter private static final Map<String, PlayerData> data = new HashMap<>();

    public static void load() {
        File dir = new File(LightBP.getPlugin().getDataFolder(), "data");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) for (File file : files) {
                if (!file.isDirectory()) new PlayerData(file);
            }
        }
    }

    private final Configuration config;
    private final String nick;
    public PlayerData(String nick) {
        this.nick = nick;

        File file = new File(LightBP.getPlugin().getDataFolder(), String.format("data/%s.yml", nick));
        this.config = new Configuration(file);
        if (!file.exists()) {
            this.config.set("exp", 0);
            this.config.set("level", 1);
            this.config.set("collected_default_rewards", new ArrayList<>());
            this.config.set("collected_premium_rewards", new ArrayList<>());
            this.config.set("disabled_message", false);
            this.config.save();
        }
        data.put(this.nick, this);
    }

    public PlayerData(File file) {
        this.nick = file.getName().replace(".yml", "");
        this.config = new Configuration(file);
        data.put(this.nick, this);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<Integer> getIntList(String path) {
        return this.config.getIntList(path);
    }

    public void set(String path, Object o) {
        this.config.set(path, o);
    }

    public void addExp(Player player, int exp, boolean showMessages) {
        int level = this.getInt("level");
        if (this.isMaxLevel(level)) return;
        int nowExp = this.getInt("exp");

        int needExp = Config.getInt(String.format("levels.%s", level + 1));
        if (nowExp + exp >= needExp) {
            this.set("level", level + 1);
            this.set("exp", (nowExp + exp) - needExp);
            if (showMessages) Config.sendMessage(player, "completeLevel", String.valueOf(level), String.valueOf(level + 1));
        }
        else {
            this.set("exp", nowExp + exp);
            if (showMessages && this.getBoolean("disabled_message"))
                Config.sendMessage(player, "upExp", String.valueOf(exp), String.valueOf(nowExp),
                    String.valueOf(nowExp + exp), String.valueOf(needExp), String.valueOf(level));
        }
        this.save();
    }

    public void save() {
        this.config.save();
    }

    public void reload() {
        this.config.reload();
    }

    public boolean isMaxLevel(int level) {
        ConfigurationSection section = Config.getSection("levels");
        List<Integer> list = section.getKeys(false).stream().map(LunaMath::toInt).collect(Collectors.toList());
        return level >= Collections.max(list);
    }
}
