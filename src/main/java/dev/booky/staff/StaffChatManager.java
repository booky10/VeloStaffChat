package dev.booky.staff;
// Created by booky10 in VeloStaffChat (15:53 19.06.21)

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatManager {

    private static final Component PREFIX = Component.text().color(NamedTextColor.WHITE)
            .append(Component.text('[', NamedTextColor.GRAY))
            .append(Component.text("Staff", TextColor.color(0x216844)))
            .append(Component.text(']', NamedTextColor.GRAY))
            .append(Component.space())
            .build();

    private final Set<UUID> toggledChat = new HashSet<>();
    private final ProxyServer server;

    public StaffChatManager(ProxyServer server) {
        this.server = server;
    }

    public boolean toggleStaffChat(UUID uniqueId) {
        if (toggledChat.add(uniqueId)) {
            return true;
        }

        toggledChat.remove(uniqueId);
        return false;
    }

    public boolean toggledStaffChat(UUID uniqueId) {
        return toggledChat.contains(uniqueId);
    }

    public void switchedServers(String player, String from, String to) {
        if (from == null && to == null) return;

        TextComponent.Builder builder = Component.text().color(NamedTextColor.WHITE);
        builder.append(PREFIX);

        if (from == null) {
            builder.append(Component.text('+', NamedTextColor.GREEN));
            builder.append(Component.text(player));
            messageStaff(builder.build());
            return;
        }

        if (to == null) {
            builder.append(Component.text('-', NamedTextColor.RED));
            builder.append(Component.text(player));
            messageStaff(builder.build());
            return;
        }

        builder.append(Component.text(from));
        builder.append(Component.text(" -> ", NamedTextColor.GRAY));
        builder.append(Component.text(to));
        builder.append(Component.text(" (", NamedTextColor.GRAY));
        builder.append(Component.text(player));
        builder.append(Component.text(')', NamedTextColor.GRAY));
        messageStaff(builder.build());
    }

    public void messageStaff(CommandSource source, String message) {
        String server = !(source instanceof Player player) ? "limbo" :
                player.getCurrentServer().map(ServerConnection::getServerInfo).map(ServerInfo::getName).orElse("limbo");
        String name = source instanceof Player player ? player.getUsername() : "console";
        messageStaff(name, server, message);
    }

    public void messageStaff(String name, String server, String message) {
        TextComponent.Builder builder = Component.text().color(NamedTextColor.WHITE);
        builder.append(PREFIX);
        builder.append(Component.text('<'));
        builder.append(Component.text(name, NamedTextColor.BLUE));
        builder.append(Component.text('@'));
        builder.append(Component.text(server, NamedTextColor.BLUE));
        builder.append(Component.text("> " + message));
        messageStaff(builder.build());
    }

    public void messageStaff(Component message) {
        server.getConsoleCommandSource().sendMessage(message);
        for (Player player : server.getAllPlayers()) {
            if (!player.hasPermission("staff.see")) continue;
            player.sendMessage(message);
        }
    }
}
