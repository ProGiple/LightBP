package org.satellite.dev.progiple.lightbp.progress;

import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.Events.CooldownPrevent;

public class EventCooldown extends CooldownPrevent<Player> {
    public EventCooldown(int seconds) {
        this.setCooldownMS(seconds * 1000);
    }

    public boolean cancel(Player player) {
        if (this.getCooldownMS() <= 0) {
            return false;
        } else return this.getCooldownMap().containsKey(player)
                && this.getCooldownMap().get(player) >= System.currentTimeMillis();
    }

    public void put(Player player) {
        this.getCooldownMap().put(player, System.currentTimeMillis() + (long) this.getCooldownMS());
    }
}
