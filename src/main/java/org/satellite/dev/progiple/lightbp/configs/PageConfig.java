package org.satellite.dev.progiple.lightbp.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Configuration.IConfig;
import org.novasparkle.lunaspring.Util.Utils;
import org.satellite.dev.progiple.lightbp.LightBP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PageConfig {
    @Getter private static final Map<String, PageConfig> pages = new HashMap<>();

    public static void load() {
        File dir = new File(LightBP.getPlugin().getDataFolder(), "pages");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                pages.clear();
                for (File file : files) {
                    if (!file.isDirectory()) new PageConfig(file);
                }
            }
        }
    }

    private final IConfig config;
    @Getter private final String id;
    public PageConfig(File file) {
        this.config = new IConfig(file);
        this.id = file.getName().replace(".yml", "");
        pages.put(this.id, this);
    }

    public String getTitle() {
        return Utils.color(this.config.getString("title"));
    }

    public byte getSize() {
        return (byte) (this.config.getInt("rows") * 9);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getSection(path);
    }
}
