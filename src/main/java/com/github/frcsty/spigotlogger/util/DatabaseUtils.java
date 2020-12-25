package com.github.frcsty.spigotlogger.util;

import co.aikar.idb.DB;
import com.github.frcsty.spigotlogger.provider.DatabaseProvider;

import java.util.UUID;

public final class DatabaseUtils {

    public static void saveToDatabase(final UUID uuid, final String event, final String json, final String rez) {
        DB.executeUpdateAsync(
                "INSERT INTO `" + DatabaseProvider.databaseName + "`.`logger` (`uuid`, `timestamp`, `event`, `entry`, `rez`) VALUES (`?`, current_timestamp(), `?`, `?`, `?`);",
                uuid == null ? "0000" : uuid.toString(), event, json, rez
        );
    }

}
