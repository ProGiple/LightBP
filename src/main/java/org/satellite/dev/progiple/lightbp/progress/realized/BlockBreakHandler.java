package org.satellite.dev.progiple.lightbp.progress.realized;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.novasparkle.lunaspring.Util.managers.RegionManager;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

public class BlockBreakHandler extends BasicHandler {
    public BlockBreakHandler() {
        super("blockbreak");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        String nick = player.getName();
        PlayerData playerData = PlayerData.getData().get(nick);
        if (playerData == null) playerData = new PlayerData(nick);

        int level = playerData.getInt("level");
        ConfigurationSection section = Config.getSection("exp_farm.blockbreak.disabled_regions_for_level");
        for (String key : section.getKeys(false)) {
            if (RegionManager.containsBlock(key, e.getBlock().getLocation())) {
                if (level >= section.getInt(key)) return;
            }
        }

        this.progress(player, e.getBlock().getType().name());
    }
}
