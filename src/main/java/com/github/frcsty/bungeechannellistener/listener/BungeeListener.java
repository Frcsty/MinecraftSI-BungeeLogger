package com.github.frcsty.bungeechannellistener.listener;

import com.github.frcsty.bungeechannellistener.LoggerPlugin;
import com.github.frcsty.bungeechannellistener.util.DatabaseUtils;
import com.github.frcsty.bungeechannellistener.util.FileUtils;
import com.github.frcsty.bungeechannellistener.util.JsonUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public final class BungeeListener implements Listener {

    private final String rez;

    public BungeeListener(final LoggerPlugin plugin) {
        this.rez = FileUtils.getConfiguration(plugin, "config.yml").getString("database.rez");
    }

    @EventHandler
    public void onChat(final ChatEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event, ChatEvent.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getSender(), Connection.class));
        eventContents.putAll(JsonUtils.getClassContents(event.getReceiver(), Connection.class));

        DatabaseUtils.saveToDatabase(
                null,
                event.getClass().getName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event, PluginMessageEvent.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getSender(), Connection.class));
        eventContents.putAll(JsonUtils.getClassContents(event.getReceiver(), Connection.class));

        DatabaseUtils.saveToDatabase(
                null,
                event.getClass().getName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onServerDisconnect(final ServerDisconnectEvent event) {
        final ServerInfo targetInfo = event.getTarget();
        final ProxiedPlayer player = event.getPlayer();

        final Map<String, String> eventContents = JsonUtils.getClassContents(event, ServerDisconnectEvent.class);
        eventContents.putAll(JsonUtils.getClassContents(targetInfo, ServerInfo.class));
        eventContents.putAll(JsonUtils.getClassContents(player, ProxiedPlayer.class));

        DatabaseUtils.saveToDatabase(
                player.getUniqueId(),
                event.getClass().getName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        final Map<String, String> eventContents = JsonUtils.getClassContents(event, PlayerDisconnectEvent.class);
        eventContents.putAll(JsonUtils.getClassContents(player, ProxiedPlayer.class));

        DatabaseUtils.saveToDatabase(
                player.getUniqueId(),
                event.getClass().getName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        final Map<String, String> eventContents = JsonUtils.getClassContents(event, PostLoginEvent.class);
        eventContents.putAll(JsonUtils.getClassContents(player, ProxiedPlayer.class));

        DatabaseUtils.saveToDatabase(
                player.getUniqueId(),
                event.getClass().getName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

}
