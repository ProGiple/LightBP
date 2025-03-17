package org.satellite.dev.progiple.lightbp.progress.realized;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

public class BlockBreakHandler extends BasicHandler {
    public BlockBreakHandler() {
        super("blockbreak");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        this.progress(player, e.getBlock().getType().name());
    }
}
