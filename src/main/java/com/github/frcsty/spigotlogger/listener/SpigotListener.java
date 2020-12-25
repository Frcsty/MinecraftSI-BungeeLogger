package com.github.frcsty.spigotlogger.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.async.AsyncMarker;
import com.comphenix.protocol.events.*;
import com.github.frcsty.spigotlogger.LoggerPlugin;
import com.github.frcsty.spigotlogger.util.DatabaseUtils;
import com.github.frcsty.spigotlogger.util.JsonUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.net.InetAddress;
import java.util.*;

public final class SpigotListener implements Listener {

    private final LoggerPlugin plugin;
    private final String rez;

    private final Set<PacketType> SERVER_PACKET_TYPES = new HashSet<>(Arrays.asList(
            PacketType.Play.Server.SPAWN_ENTITY,
            PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB,
            PacketType.Play.Server.SPAWN_ENTITY_WEATHER,
            PacketType.Play.Server.SPAWN_ENTITY_LIVING,
            PacketType.Play.Server.SPAWN_ENTITY_PAINTING,
            PacketType.Play.Server.NAMED_ENTITY_SPAWN,
            PacketType.Play.Server.ANIMATION,
            PacketType.Play.Server.STATISTIC,
            PacketType.Play.Server.BLOCK_BREAK,
            PacketType.Play.Server.BLOCK_BREAK_ANIMATION,
            PacketType.Play.Server.TILE_ENTITY_DATA,
            PacketType.Play.Server.BLOCK_ACTION,
            PacketType.Play.Server.BLOCK_CHANGE,
            PacketType.Play.Server.BOSS,
            PacketType.Play.Server.SERVER_DIFFICULTY,
            PacketType.Play.Server.CHAT,
            PacketType.Play.Server.MULTI_BLOCK_CHANGE,
            PacketType.Play.Server.TAB_COMPLETE,
            PacketType.Play.Server.COMMANDS,
            PacketType.Play.Server.TRANSACTION,
            PacketType.Play.Server.CLOSE_WINDOW,
            PacketType.Play.Server.WINDOW_ITEMS,
            PacketType.Play.Server.WINDOW_DATA,
            PacketType.Play.Server.SET_SLOT,
            PacketType.Play.Server.SET_COOLDOWN,
            PacketType.Play.Server.CUSTOM_PAYLOAD,
            PacketType.Play.Server.CUSTOM_SOUND_EFFECT,
            PacketType.Play.Server.KICK_DISCONNECT,
            PacketType.Play.Server.ENTITY_STATUS,
            PacketType.Play.Server.EXPLOSION,
            //PacketType.Play.Server.UNLOAD_CHUNK,
            PacketType.Play.Server.GAME_STATE_CHANGE,
            PacketType.Play.Server.OPEN_WINDOW_HORSE,
            PacketType.Play.Server.KEEP_ALIVE,
            //PacketType.Play.Server.MAP_CHUNK,
            PacketType.Play.Server.WORLD_EVENT,
            PacketType.Play.Server.WORLD_PARTICLES,
            //PacketType.Play.Server.LIGHT_UPDATE,
            PacketType.Play.Server.LOGIN,
            PacketType.Play.Server.MAP,
            PacketType.Play.Server.OPEN_WINDOW_MERCHANT,
            PacketType.Play.Server.REL_ENTITY_MOVE,
            PacketType.Play.Server.REL_ENTITY_MOVE_LOOK,
            PacketType.Play.Server.ENTITY_LOOK,
            PacketType.Play.Server.ENTITY,
            PacketType.Play.Server.VEHICLE_MOVE,
            PacketType.Play.Server.OPEN_BOOK,
            PacketType.Play.Server.OPEN_WINDOW,
            PacketType.Play.Server.OPEN_SIGN_EDITOR,
            PacketType.Play.Server.AUTO_RECIPE,
            PacketType.Play.Server.ABILITIES,
            PacketType.Play.Server.COMBAT_EVENT,
            PacketType.Play.Server.PLAYER_INFO,
            PacketType.Play.Server.LOOK_AT,
            PacketType.Play.Server.POSITION,
            PacketType.Play.Server.RECIPES,
            PacketType.Play.Server.ENTITY_DESTROY,
            PacketType.Play.Server.REMOVE_ENTITY_EFFECT,
            PacketType.Play.Server.RESOURCE_PACK_SEND,
            PacketType.Play.Server.RESPAWN,
            PacketType.Play.Server.ENTITY_HEAD_ROTATION,
            PacketType.Play.Server.SELECT_ADVANCEMENT_TAB,
            PacketType.Play.Server.WORLD_BORDER,
            PacketType.Play.Server.CAMERA,
            PacketType.Play.Server.HELD_ITEM_SLOT,
            PacketType.Play.Server.VIEW_CENTRE,
            PacketType.Play.Server.VIEW_DISTANCE,
            PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE,
            PacketType.Play.Server.ENTITY_METADATA,
            PacketType.Play.Server.ATTACH_ENTITY,
            PacketType.Play.Server.ENTITY_VELOCITY,
            PacketType.Play.Server.ENTITY_EQUIPMENT,
            PacketType.Play.Server.EXPERIENCE,
            PacketType.Play.Server.UPDATE_HEALTH,
            PacketType.Play.Server.SCOREBOARD_OBJECTIVE,
            PacketType.Play.Server.MOUNT,
            PacketType.Play.Server.SCOREBOARD_TEAM,
            PacketType.Play.Server.SCOREBOARD_SCORE,
            PacketType.Play.Server.SPAWN_POSITION,
            //PacketType.Play.Server.UPDATE_TIME,
            PacketType.Play.Server.TITLE,
            PacketType.Play.Server.ENTITY_SOUND,
            PacketType.Play.Server.NAMED_SOUND_EFFECT,
            PacketType.Play.Server.STOP_SOUND,
            PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER,
            PacketType.Play.Server.NBT_QUERY,
            PacketType.Play.Server.COLLECT,
            PacketType.Play.Server.ENTITY_TELEPORT,
            PacketType.Play.Server.ADVANCEMENTS,
            PacketType.Play.Server.UPDATE_ATTRIBUTES,
            PacketType.Play.Server.ENTITY_EFFECT,
            PacketType.Play.Server.RECIPE_UPDATE,
            PacketType.Play.Server.TAGS
    ));

    private final Set<PacketType> CLIENT_PACKET_TYPES = new HashSet<>(Arrays.asList(
            PacketType.Play.Client.TELEPORT_ACCEPT,
            PacketType.Play.Client.TILE_NBT_QUERY,
            PacketType.Play.Client.DIFFICULTY_CHANGE,
            PacketType.Play.Client.CHAT,
            PacketType.Play.Client.CLIENT_COMMAND,
            PacketType.Play.Client.SETTINGS,
            PacketType.Play.Client.TAB_COMPLETE,
            PacketType.Play.Client.TRANSACTION,
            PacketType.Play.Client.ENCHANT_ITEM,
            PacketType.Play.Client.WINDOW_CLICK,
            PacketType.Play.Client.CLOSE_WINDOW,
            PacketType.Play.Client.CUSTOM_PAYLOAD,
            PacketType.Play.Client.B_EDIT,
            PacketType.Play.Client.ENTITY_NBT_QUERY,
            PacketType.Play.Client.USE_ENTITY,
            PacketType.Play.Client.KEEP_ALIVE,
            PacketType.Play.Client.DIFFICULTY_LOCK,
            PacketType.Play.Client.POSITION,
            PacketType.Play.Client.POSITION_LOOK,
            PacketType.Play.Client.LOOK,
            PacketType.Play.Client.FLYING,
            PacketType.Play.Client.VEHICLE_MOVE,
            PacketType.Play.Client.BOAT_MOVE,
            PacketType.Play.Client.PICK_ITEM,
            PacketType.Play.Client.AUTO_RECIPE,
            PacketType.Play.Client.ABILITIES,
            PacketType.Play.Client.BLOCK_DIG,
            PacketType.Play.Client.ENTITY_ACTION,
            PacketType.Play.Client.STEER_VEHICLE,
            PacketType.Play.Client.RECIPE_DISPLAYED,
            PacketType.Play.Client.ITEM_NAME,
            PacketType.Play.Client.RESOURCE_PACK_STATUS,
            PacketType.Play.Client.ADVANCEMENTS,
            PacketType.Play.Client.TR_SEL,
            PacketType.Play.Client.BEACON,
            PacketType.Play.Client.HELD_ITEM_SLOT,
            PacketType.Play.Client.SET_COMMAND_BLOCK,
            PacketType.Play.Client.SET_COMMAND_MINECART,
            PacketType.Play.Client.SET_CREATIVE_SLOT,
            PacketType.Play.Client.SET_JIGSAW,
            PacketType.Play.Client.STRUCT,
            PacketType.Play.Client.UPDATE_SIGN,
            PacketType.Play.Client.ARM_ANIMATION,
            PacketType.Play.Client.SPECTATE,
            PacketType.Play.Client.USE_ITEM,
            PacketType.Play.Client.BLOCK_PLACE
    ));

    public SpigotListener(final LoggerPlugin plugin) {
        this.rez = plugin.getConfig().getString("database.rez");
        this.plugin = plugin;

        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(
                getServerPacketListener()
        );


        manager.addPacketListener(
                getClientPacketListener()
        );
    }

    private PacketAdapter getServerPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, SERVER_PACKET_TYPES) {
            @Override
            public void onPacketSending(PacketEvent event) {
                try {
                    final Map<String, String> packetContents = JsonUtils.getClassContents(event.getAsyncMarker(), AsyncMarker.class);
                    packetContents.putAll(JsonUtils.getClassContents(event.getNetworkMarker(), NetworkMarker.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getPacket(), PacketContainer.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getPlayer(), Player.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getSource(), Object.class));

                    System.out.println(JsonUtils.serializeJson(packetContents));
                } catch (final Exception ignored) {
                }
            }
        };
    }

    private PacketAdapter getClientPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, CLIENT_PACKET_TYPES) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                try {
                    final Map<String, String> packetContents = JsonUtils.getClassContents(event.getAsyncMarker(), AsyncMarker.class);
                    packetContents.putAll(JsonUtils.getClassContents(event.getNetworkMarker(), NetworkMarker.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getPacket(), PacketContainer.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getPlayer(), Player.class));
                    packetContents.putAll(JsonUtils.getClassContents(event.getSource(), Object.class));

                    System.out.println(JsonUtils.serializeJson(packetContents));
                } catch (final Exception ignored) { }
            }
        };
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event.getDamager(), Entity.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getEntity(), Entity.class));

        DatabaseUtils.saveToDatabase(
                null,
                event.getEventName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event.getPlayer(), Player.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getAddress(), InetAddress.class));
        eventContents.putAll(JsonUtils.getClassContents(event.getResult(), PlayerLoginEvent.Result.class));

        DatabaseUtils.saveToDatabase(
                event.getPlayer().getUniqueId(),
                event.getEventName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event.getPlayer(), Player.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getReason(), PlayerQuitEvent.QuitReason.class));

        DatabaseUtils.saveToDatabase(
                event.getPlayer().getUniqueId(),
                event.getEventName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

    @EventHandler
    public void onPlayerCommand(final PlayerCommandSendEvent event) {
        final Map<String, String> eventContents = JsonUtils.getClassContents(event.getPlayer(), Player.class);
        eventContents.putAll(JsonUtils.getClassContents(event.getCommands(), Collection.class));

        DatabaseUtils.saveToDatabase(
                event.getPlayer().getUniqueId(),
                event.getEventName(),
                JsonUtils.serializeJson(eventContents),
                rez
        );
    }

}
