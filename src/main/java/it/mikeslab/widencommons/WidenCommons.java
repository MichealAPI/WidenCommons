package it.mikeslab.widencommons;

import io.sentry.Sentry;
import it.mikeslab.widencommons.api.formatter.FormatUtil;
import it.mikeslab.widencommons.api.inventory.CustomGui;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WidenCommons extends JavaPlugin implements Listener {

    public static final String PLUGIN_NAME = "WidenCommons";


    @Override
    public void onEnable() {
        saveDefaultConfig();


        FormatUtil.printStartupInfos(this, "00FF72");

        if(this.getConfig().getBoolean("sentry.enabled")) {
            this.initSentry();
        }

    }

    @Override
    public void onDisable() {

        // close all open customGui inventories

        for(Player player : Bukkit.getOnlinePlayers()) {

            Inventory inventory = player.getOpenInventory().getTopInventory();
            InventoryHolder holder = inventory.getHolder();

            if(holder instanceof CustomGui) {
                player.closeInventory();
            }

        }

    }

    /**
     * Initialize Sentry
     */
    private void initSentry() {

        String version = this.getPluginMeta().getVersion();
        String pluginName = this.getPluginMeta().getName();

        Sentry.init(options -> {
            options.setDsn(this.getConfig().getString("sentry.dsn"));
            options.setRelease(pluginName + "@" + version);
            options.setEnvironment("development");

            options.setTracesSampleRate(1.0);

        });
    }

}
