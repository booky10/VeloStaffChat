package dev.booky.staff.listener;
// Created by booky10 in VeloStaffChat (16:37 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.booky.staff.StaffChatManager;

import java.util.Optional;

public record NotifyListener(StaffChatManager manager) {

    @Subscribe
    public void onServerSwitch(ServerConnectedEvent event) {
        if (!event.getPlayer().hasPermission("staff.notify")) return;

        String from = event.getPreviousServer().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).orElse(null);
        String to = event.getServer().getServerInfo().getName();

        manager.switchedServers(event.getPlayer().getUsername(), from, to);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!event.getPlayer().hasPermission("staff.notify")) return;

        Optional<ServerConnection> currServer = event.getPlayer().getCurrentServer();
        String name = currServer.map(ServerConnection::getServerInfo).map(ServerInfo::getName).orElse(null);
        manager.switchedServers(event.getPlayer().getUsername(), name, null);
    }
}
