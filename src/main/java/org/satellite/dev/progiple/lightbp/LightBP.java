package org.satellite.dev.progiple.lightbp;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.realized.NBTService;
import org.novasparkle.lunaspring.Util.managers.NBTManager;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.progress.realized.BlockBreakHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.ChatHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.KillHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.Listeners;

import java.util.Objects;

public final class LightBP extends JavaPlugin {
    @Getter private static LightBP plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        saveResource("pages/page1.yml", false);
        PageConfig.load();
        PlayerData.load();

        this.reg(new MenuHandler());
        this.reg(new BlockBreakHandler());
        this.reg(new ChatHandler());
        this.reg(new KillHandler());
        this.reg(new Listeners());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new Placeholders().register();

        Command command = new Command();
        Objects.requireNonNull(getCommand("lightbp")).setTabCompleter(command);
        Objects.requireNonNull(getCommand("lightbp")).setExecutor(command);

        NBTService nbtService = new NBTService();
        LunaSpring.getServiceProvider().register(nbtService);
        NBTManager.init(nbtService);
    }

    @Override
    public void onDisable() {
    }

    private void reg(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
