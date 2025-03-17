package org.satellite.dev.progiple.lightbp.progress.realized;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.satellite.dev.progiple.lightbp.LightBP;
import org.satellite.dev.progiple.lightbp.progress.Runnable;

public class Listeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        new Runnable(e.getPlayer()).runTaskTimerAsynchronously(LightBP.getPlugin(), 20L * 60, 20L * 60);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (Runnable.getTasks().containsKey(player.getName())) {
            int taskId = Runnable.getTasks().get(player.getName()).getTaskId();
            if (Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId))
                Bukkit.getScheduler().cancelTask(taskId);
        }
    }
}
