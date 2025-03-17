package org.satellite.dev.progiple.lightbp.progress;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class Runnable extends BukkitRunnable {
    @Getter private static final Map<String, Runnable> tasks = new HashMap<>();

    private final Player player;
    private final BasicHandler handler;
    public Runnable(Player player) {
        this.player = player;
        this.handler = new BasicHandler("playTime") {};
        tasks.put(player.getName(), this);
    }

    @Override
    public void run() {
        this.handler.progress(this.player, "");
    }
}
