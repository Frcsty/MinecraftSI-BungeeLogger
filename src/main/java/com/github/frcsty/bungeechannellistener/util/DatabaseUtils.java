package com.github.frcsty.bungeechannellistener.util;

import co.aikar.idb.DB;
import com.github.frcsty.bungeechannellistener.provider.DatabaseProvider;

import java.sql.SQLException;
import java.util.UUID;

public final class DatabaseUtils {

    public static void saveToDatabase(final UUID uuid, final String event, final String json, final String rez) {
        try {
            DB.executeInsert(
                    "INSERT INTO `" + DatabaseProvider.databaseName + "`.`logger` (`uuid`, `timestamp`, `event`, `entry`, `rez`) VALUES (?, current_timestamp(), ?, ?, ?);",
                    uuid == null ? "0000" : uuid.toString(), event, json, rez
            );
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

}
