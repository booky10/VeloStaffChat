package dev.booky.staff;
// Created by booky10 in VeloStaffChat (15:53 19.06.21)

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.ServerInfo;
import jakarta.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static dev.booky.staff.util.StaffChatConstants.STAFF_SEE_PERMISSION;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

@NullMarked
public final class StaffChatManager {

    private static final String FALLBACK_SERVER_NAME = "limbo";
    private static final String FALLBACK_USER_NAME = "console";

    private static final Component PREFIX = text().color(WHITE)
            .append(text('[', GRAY))
            .append(text("Staff", TextColor.color(0x216844)))
            .append(text(']', GRAY))
            .append(space())
            .build();

    private final Set<UUID> toggledChat = new HashSet<>();
    private final ProxyServer server;

    @Inject
    public StaffChatManager(ProxyServer server) {
        this.server = server;
    }

    public boolean toggleStaffChat(UUID uniqueId) {
        if (this.toggledChat.add(uniqueId)) {
            return true;
        }
        this.toggledChat.remove(uniqueId);
        return false;
    }

    public boolean hasToggledStaffChat(UUID uniqueId) {
        return this.toggledChat.contains(uniqueId);
    }

    public void sendServerSwitch(String player, @Nullable String from, @Nullable String to) {
        if (from == null && to == null) {
            return;
        }

        TextComponent.Builder builder = text().color(WHITE).append(PREFIX);

        // if from is null, then this is a player join: [Staff] +Notch (lobby1)
        if (from == null) {
            this.sendStaffMessage(builder
                    .append(text('+', GREEN))
                    .append(text(player))
                    .append(text(" (", GRAY))
                    .append(text(to))
                    .append(text(')', GRAY))
                    .build());
            return;
        }

        // if to is null, then this is a player quit: [Staff] -Notch (lobby42)
        if (to == null) {
            this.sendStaffMessage(builder
                    .append(text('-', RED))
                    .append(text(player))
                    .append(text(" (", GRAY))
                    .append(text(from))
                    .append(text(')', GRAY))
                    .build());
            return;
        }

        // this is a server switch: [Staff] Notch: lobby1 -> lobby42
        this.sendStaffMessage(builder
                .append(text(player + ": " + from))
                .append(text(" -> ", GRAY))
                .append(text(to))
                .build());
    }

    public void sendStaffMessage(CommandSource source, String message) {
        String server;
        if (source instanceof Player player) {
            server = player.getCurrentServer()
                    .map(ServerConnection::getServerInfo)
                    .map(ServerInfo::getName)
                    .orElse(FALLBACK_SERVER_NAME);
        } else {
            server = FALLBACK_SERVER_NAME;
        }
        String name = source instanceof Player player ? player.getUsername() : FALLBACK_USER_NAME;
        this.sendStaffMessage(name, server, message);
    }

    public void sendStaffMessage(String username, String server, String message) {
        // [Staff] <Notch@lobby42> Hello World!
        this.sendStaffMessage(text().color(WHITE)
                .append(PREFIX)
                .append(text('<'))
                .append(text(username, BLUE))
                .append(text('@'))
                .append(text(server, BLUE))
                .append(text("> " + message))
                .build());
    }

    public void sendStaffMessage(Component message) {
        // always print to console
        this.server.getConsoleCommandSource().sendMessage(message);

        for (Player player : this.server.getAllPlayers()) {
            if (player.hasPermission(STAFF_SEE_PERMISSION)) {
                player.sendMessage(message);
            }
        }
    }
}
