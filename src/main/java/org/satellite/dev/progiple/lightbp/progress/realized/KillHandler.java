package org.satellite.dev.progiple.lightbp.progress.realized;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

public class KillHandler extends BasicHandler {
    public KillHandler() {
        super("kill");
    }

    @EventHandler
    public void onKillPlayer(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        this.progress(e.getEntity().getKiller(), entity.getType().name());
    }
}
