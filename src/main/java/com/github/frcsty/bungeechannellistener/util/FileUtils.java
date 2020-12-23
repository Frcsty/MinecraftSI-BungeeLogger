package com.github.frcsty.bungeechannellistener.util;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class FileUtils {

    public static Configuration getConfiguration(final Plugin plugin, final String fileName) {
        Configuration configuration = null;

        try {
            configuration = ConfigurationProvider
                    .getProvider(YamlConfiguration.class)
                    .load(new File(plugin.getDataFolder(), fileName));


        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return configuration;
    }

}
