package com.github.frcsty.spigotlogger;

import com.github.frcsty.spigotlogger.listener.SpigotListener;
import com.github.frcsty.spigotlogger.provider.DatabaseProvider;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class LoggerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new DatabaseProvider(this).setupDatabase();

        registerListeners(
                new SpigotListener(this)
        );
    }

    @Override
    public void onDisable() {
        reloadConfig();
    }

    private void registerListeners(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

}
