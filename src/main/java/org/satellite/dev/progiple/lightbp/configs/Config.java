package org.satellite.dev.progiple.lightbp.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.Configuration.IConfig;
import org.satellite.dev.progiple.lightbp.LightBP;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

import java.util.List;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        config = new IConfig(LightBP.getPlugin());
    }

    public void reload() {
        config.reload(LightBP.getPlugin());
        BasicHandler.getHandlers().forEach(BasicHandler::update);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getList(String path) {
        return config.getStringList(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }

    public void sendMessage(CommandSender sender, String id, String... replacements) {
        config.sendMessage(sender, id, replacements);
    }
}
