package tk.booky.velostaffchat;
// Created by booky10 in VeloStaffChat (15:53 19.06.21)

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import tk.booky.velostaffchat.events.chats.GenericStaffChatEvent;
import tk.booky.velostaffchat.events.chats.PlayerStaffChatEvent;
import tk.booky.velostaffchat.events.chats.StaffChatEvent;
import tk.booky.velostaffchat.events.reports.PlayerReportedEvent;
import tk.booky.velostaffchat.events.switches.PlayerStaffServerSwitchEvent;
import tk.booky.velostaffchat.messages.DefaultMessageProvider;
import tk.booky.velostaffchat.messages.IMessageProvider;
import tk.booky.velostaffchat.utils.SwitchAction;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StaffChatManager {

    private final IMessageProvider messages = new DefaultMessageProvider();
    private final Set<UUID> permanentChat = new HashSet<>();
    private final ProxyServer server;

    public StaffChatManager(ProxyServer server) {
        this.server = server;
    }

    public CompletableFuture<ResultedEvent.GenericResult> tryReportPlayer(CommandSource reporter, Player target, String reason) {
        CompletableFuture<ResultedEvent.GenericResult> future = new CompletableFuture<>();

        server.getEventManager().fire(new PlayerReportedEvent(reporter, target, reason)).thenAccept((event) -> {
            future.complete(event.getResult());

            if (event.getResult().isAllowed()) {
                String reporterName = "console", reporterServer = "limbo";
                if (event.getReporter() instanceof Player) {
                    Optional<ServerConnection> connection = ((Player) event.getReporter()).getCurrentServer();

                    reporterName = ((Player) event.getReporter()).getUsername();
                    reporterServer = connection.isPresent() ? connection.get().getServerInfo().getName() : "limbo";
                }

                Optional<ServerConnection> connection = event.getTarget().getCurrentServer();

                String targetName = event.getTarget().getUsername();
                String targetServer = connection.isPresent() ? connection.get().getServerInfo().getName() : "limbo";

                reportPlayer(reporterName, reporterServer, targetName, targetServer, event.getReason());
            }
        });

        return future;
    }

    public CompletableFuture<ResultedEvent.GenericResult> tryStaffMessage(CommandSource source, String message) {
        CompletableFuture<ResultedEvent.GenericResult> future = new CompletableFuture<>();
        StaffChatEvent<? extends CommandSource> chatEvent;

        if (source instanceof Player) {
            Player player = (Player) source;
            Optional<ServerConnection> connection = player.getCurrentServer();
            String server = connection.isPresent() ? connection.get().getServerInfo().getName() : "limbo";
            chatEvent = new PlayerStaffChatEvent(server, message, player);
        } else {
            chatEvent = new GenericStaffChatEvent("limbo", message, source);
        }

        server.getEventManager().fire(chatEvent).thenAccept((event) -> {
            future.complete(event.getResult());

            if (event.getResult().isAllowed()) {
                String name = event.getSender() instanceof Player ? ((Player) event.getSender()).getUsername() : "console";
                messageStaff(name, event.getServer(), event.getMessage());
            }
        });

        return future;
    }

    public CompletableFuture<ResultedEvent.GenericResult> trySwitchedServers(String player, String from, String to) {
        CompletableFuture<ResultedEvent.GenericResult> future = new CompletableFuture<>();

        SwitchAction action = SwitchAction.getAction(from, to);
        PlayerStaffServerSwitchEvent switchEvent = new PlayerStaffServerSwitchEvent(player, from, to, action);

        server.getEventManager().fire(switchEvent).thenAccept((event) -> {
            future.complete(event.getResult());

            if (event.getResult().isAllowed()) {
                switchedServers(event.getPlayer(), event.getFrom(), event.getTo(), event.getAction());
            }
        });

        return future;
    }

    public void rawStaffMessage(String message) {
        rawStaffMessage(LegacyComponentSerializer.legacy('§').deserialize(message));
    }

    public void rawStaffMessage(Component message) {
        server.getConsoleCommandSource().sendMessage(message);
        for (Player player : server.getAllPlayers()) {
            if (!player.hasPermission("staff.see")) continue;
            player.sendMessage(message);
        }
    }

    public boolean togglePermanentStaffChat(UUID uuid) {
        boolean permanent = permanentChat.add(uuid);
        if (!permanent) permanentChat.remove(uuid);
        return permanent;
    }

    public boolean hasPermanentStaffChat(UUID uuid) {
        return permanentChat.contains(uuid);
    }

    public void reportPlayer(String reporter, String reporterServer, String target, String targetServer, String reason) {
        rawStaffMessage(String.format(messages.getReportFormat(), reporter, reporterServer, target, targetServer, reason));
    }

    public void messageStaff(String name, String server, String message) {
        rawStaffMessage(String.format(messages.getChatFormat(), name, server, message));
    }

    public void switchedServers(String player, String from, String to, SwitchAction action) {
        switch (action) {
            case SWITCH:
                rawStaffMessage(String.format(messages.getSwitchedFormat(), player, from, to));
                break;
            case JOIN:
                rawStaffMessage(String.format(messages.getJoinFormat(), player, to));
                break;
            case LEAVE:
                rawStaffMessage(String.format(messages.getLeaveFormat(), player, from));
                break;
            default:
                break;
        }
    }
}