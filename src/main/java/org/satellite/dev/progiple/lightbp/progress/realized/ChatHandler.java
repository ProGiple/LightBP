package org.satellite.dev.progiple.lightbp.progress.realized;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.satellite.dev.progiple.lightbp.progress.BasicHandler;

public class ChatHandler extends BasicHandler {
    public ChatHandler() {
        super("chat");
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onChat(PlayerChatEvent e) {
        this.progress(e.getPlayer(), "");
    }
}
