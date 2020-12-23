package com.github.frcsty.bungeechannellistener.provider;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.github.frcsty.bungeechannellistener.GeyserChannelBlocker;
import com.github.frcsty.bungeechannellistener.util.FileUtils;
import net.md_5.bungee.config.Configuration;

public final class DatabaseProvider {

    public static String databaseName;

    public DatabaseProvider(final GeyserChannelBlocker plugin) {
        final Configuration configuration = FileUtils.getConfiguration(plugin, "config.yml");
        databaseName = configuration.getString("dataSource.databaseName");

        final DatabaseOptions options = DatabaseOptions.builder().mysql(
                configuration.getString("dataSource.user"),
                configuration.getString("dataSource.password"),
                databaseName,
                configuration.getString("dataSource.server") + ":" + configuration.getString("dataSource.port")
        ).build();
        final Database database = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(database);
    }

    public void setupDatabase() {
        DB.executeUpdateAsync(
                "CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`logger` (" +
                        "`id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "`uuid` varchar(255) NOT NULL, " +
                        "`timestamp` timestamp NOT NULL DEFAULT current_timestamp(), " +
                        "`event` varchar(255) NOT NULL, " +
                        "`entry` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`entry`)), " +
                        "`rez` varchar(255) NOT NULL)" +
                        "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        );
    }

}
