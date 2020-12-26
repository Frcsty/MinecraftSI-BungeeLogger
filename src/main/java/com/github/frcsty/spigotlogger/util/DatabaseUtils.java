package com.github.frcsty.spigotlogger.util;

import co.aikar.idb.DB;
import com.github.frcsty.spigotlogger.provider.DatabaseProvider;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

public final class DatabaseUtils {

    public static void insertIntoDatabase(final Plugin plugin, final UUID uuid, final String event, final String json, final String rez) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    DB.executeInsert(
                            "INSERT INTO `" + DatabaseProvider.databaseName + "`.`logger` (`uuid`, `timestamp`, `event`, `entry`, `rez`) VALUES (?, current_timestamp(), ?, ?, ?);",
                            uuid == null ? "0000" : uuid.toString(), event, json, rez
                    );
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}
