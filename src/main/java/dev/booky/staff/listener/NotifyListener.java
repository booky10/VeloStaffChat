package dev.booky.staff.listener;
// Created by booky10 in VeloStaffChat (16:37 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.booky.staff.StaffChatManager;
import org.jspecify.annotations.NullMarked;

import static dev.booky.staff.util.StaffChatConstants.STAFF_NOTIFY_PERMISSION;

@NullMarked
public class NotifyListener {

    private final StaffChatManager manager;

    public NotifyListener(StaffChatManager manager) {
        this.manager = manager;
    }

    @Subscribe
    public void onServerSwitch(ServerConnectedEvent event) {
        if (!event.getPlayer().hasPermission(STAFF_NOTIFY_PERMISSION)) {
            return;
        }

        String username = event.getPlayer().getUsername();
        String from = event.getPreviousServer()
                .map(RegisteredServer::getServerInfo)
                .map(ServerInfo::getName)
                .orElse(null);
        String to = event.getServer().getServerInfo().getName();
        this.manager.sendServerSwitch(username, from, to);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!event.getPlayer().hasPermission(STAFF_NOTIFY_PERMISSION)) {
            return;
        }

        String username = event.getPlayer().getUsername();
        String serverName = event.getPlayer().getCurrentServer()
                .map(ServerConnection::getServerInfo)
                .map(ServerInfo::getName)
                .orElse(null);
        this.manager.sendServerSwitch(username, serverName, null);
    }
}
