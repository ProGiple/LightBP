package org.satellite.dev.progiple.lightbp;

import lombok.Getter;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.realized.NBTService;
import org.novasparkle.lunaspring.Util.Service.realized.RegionService;
import org.novasparkle.lunaspring.Util.managers.NBTManager;
import org.novasparkle.lunaspring.Util.managers.RegionManager;
import org.novasparkle.lunaspring.other.LunaPlugin;
import org.satellite.dev.progiple.lightbp.configs.Config;
import org.satellite.dev.progiple.lightbp.configs.PageConfig;
import org.satellite.dev.progiple.lightbp.configs.PlayerData;
import org.satellite.dev.progiple.lightbp.progress.realized.BlockBreakHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.ChatHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.KillHandler;
import org.satellite.dev.progiple.lightbp.progress.realized.Listeners;

public final class LightBP extends LunaPlugin {
    @Getter private static LightBP plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        this.loadFile("pages/page1.yml");
        PageConfig.load();
        PlayerData.load();

        this.initialize();
        this.registerListener(new BlockBreakHandler());
        this.registerListener(new ChatHandler());
        this.registerListener(new KillHandler());
        this.registerListener(new Listeners());

        this.createPlaceholder("lightbp", (player, params) -> {
            PlayerData playerData = PlayerData.getData().get(player.getName());
            if (playerData == null) playerData = new PlayerData(player.getName());

            if (params.equalsIgnoreCase("level")) {
                return String.valueOf(playerData.getInt("level"));
            }
            else if (params.equalsIgnoreCase("exp")) {
                return String.valueOf(playerData.getInt("exp"));
            }
            else if (params.equalsIgnoreCase("next_level")) {
                return String.valueOf(playerData.getInt("level") + 1);
            }
            else if (params.equalsIgnoreCase("need_exp")) {
                return String.valueOf(Config.getInt(String.format("levels.%s", playerData.getInt("level") + 1)));
            }
            return "";
        });
        this.registerTabExecutor(new Command(), "lightbp");

        NBTService nbtService = new NBTService();
        LunaSpring.getServiceProvider().register(nbtService);
        NBTManager.init(nbtService);

        RegionService regionService = new RegionService();
        LunaSpring.getServiceProvider().register(regionService);
        RegionManager.init(regionService);
    }

    @Override
    public void onDisable() {
    }
}
