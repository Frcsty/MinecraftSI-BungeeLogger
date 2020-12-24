package com.github.frcsty.bungeechannellistener;

import com.github.frcsty.bungeechannellistener.listener.BungeeListener;
import com.github.frcsty.bungeechannellistener.provider.DatabaseProvider;
import com.github.frcsty.bungeechannellistener.util.FileUtils;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class LoggerPlugin extends Plugin implements Listener {

    @Override
    public void onEnable() {
        loadDefaults();

        new DatabaseProvider(this).setupDatabase();

        registerListeners(
                this,
                new BungeeListener(this)
        );
    }

    private void registerListeners(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getProxy().getPluginManager().registerListener(this, listener));
    }

    private void loadDefaults() {
        final File dir = new File(getDataFolder() + "");
        if (!dir.exists()) {
            dir.mkdir();
        }

        final File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        final Configuration config = FileUtils.getConfiguration(this, "config.yml");

        if (config.get("dataSource.user") == null) config.set("dataSource.user", "FrostyDB");
        if (config.get("dataSource.password") == null) config.set("dataSource.password", "debpasswd");
        if (config.get("dataSource.databaseName") == null) config.set("dataSource.databaseName", "bungeeLogger");
        if (config.get("dataSource.port") == null) config.set("dataSource.port", "3306");
        if (config.get("dataSource.server") == null) config.set("dataSource.server", "85.10.194.250");
        if (config.get("database.rez") == null) config.set("database.rez", "water breeding");

        saveFile(config);
    }

    private void saveFile(final Configuration configuration) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onMessageReceive(final PluginMessageEvent event) {
        final ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        final String tag = event.getTag();
        //final Connection sender = event.getSender();
        //final Connection receiver = event.getReceiver();
        final String data = in.readLine();

        if (tag.equalsIgnoreCase("minecraft:brand") && data.contains("Geyser")
                || tag.equalsIgnoreCase("btlp:bridge") && data.contains("Geyser")) {
            event.setCancelled(true);
            //System.out.println("Cancelled: [" + tag + " - @" + sender + "] -> [" + receiver + "] " + data);
        }
    }

}
