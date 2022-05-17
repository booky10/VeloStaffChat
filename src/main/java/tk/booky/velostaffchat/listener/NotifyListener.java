package tk.booky.velostaffchat.listener;
// Created by booky10 in VeloStaffChat (16:37 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import tk.booky.velostaffchat.VeloStaffChat;

import java.util.Optional;

public class NotifyListener {

    @Subscribe
    public void onServerSwitch(ServerConnectedEvent event) {
        if (!event.getPlayer().hasPermission("staff.notify")) return;

        String from = event.getPreviousServer().isPresent() ? event.getPreviousServer().get().getServerInfo().getName() : null;
        String to = event.getServer().getServerInfo().getName();

        VeloStaffChat.getMain().getManager().trySwitchedServers(event.getPlayer().getUsername(), from, to);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        if (!event.getPlayer().hasPermission("staff.notify")) return;

        Optional<ServerConnection> optionalConnection = event.getPlayer().getCurrentServer();
        optionalConnection.ifPresent(connection -> VeloStaffChat.getMain().getManager().trySwitchedServers(event.getPlayer().getUsername(), connection.getServerInfo().getName(), null));
    }
}