package org.satellite.dev.progiple.lightbp.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.Configuration.IConfig;
import org.novasparkle.lunaspring.Util.Utils;
import org.satellite.dev.progiple.lightbp.LightBP;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

import java.util.ArrayList;
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

    @SuppressWarnings("deprecation")
    public void sendMessage(CommandSender sender, String id, String... replacements) {
        List<String> message = new ArrayList<>(config.getStringList(String.format("messages.%s", id)));

        if (message.isEmpty()) return;
        for (String line : message) {
            byte index = 0;
            for (String replacement : replacements) {
                line = line.replace("{" + index + "}", replacement);
                index++;
            }

            String newLine = Utils.color(line
                    .replace("ACTION_BAR", "")
                    .replace("TITLE", "")
                    .replace("SOUND", ""));
            if (sender instanceof Player &&
                    (line.startsWith("ACTION_BAR") || line.startsWith("SOUND") || line.startsWith("TITLE"))) {
                Player player = (Player) sender;
                if (line.startsWith("ACTION_BAR")) player.sendActionBar(newLine);
                else if (line.startsWith("SOUND")) player.playSound(player.getLocation(), Sound.valueOf(newLine), 1, 1);
                else {
                    String[] split = newLine.split("\\{S}");
                    if (split.length < 2) split = new String[]{split[0], ""};
                    player.sendTitle(split[0], split[1], 15, 20, 15);
                }
            }
            else sender.sendMessage(newLine);
        }
    }
}
