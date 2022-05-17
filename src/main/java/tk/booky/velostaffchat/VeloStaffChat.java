package tk.booky.velostaffchat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import tk.booky.velostaffchat.commands.ReportCommand;
import tk.booky.velostaffchat.commands.StaffCommand;
import tk.booky.velostaffchat.listener.ChatListener;
import tk.booky.velostaffchat.listener.NotifyListener;

@Plugin(id = "velostaffchat", name = "VeloStaffChat", version = "@version@", authors = "booky10")
public class VeloStaffChat {

    private static VeloStaffChat main;

    private final StaffChatManager manager;
    private final ProxyServer server;

    @Inject
    @SuppressWarnings("unused")
    public VeloStaffChat(ProxyServer server, Logger logger) {
        this.manager = new StaffChatManager(server);
        this.server = server;

        main = this;
    }

    public StaffChatManager getManager() {
        return manager;
    }

    public ProxyServer getServer() {
        return server;
    }

    public static VeloStaffChat getMain() {
        return main;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register(ReportCommand.createMeta(server.getCommandManager()), new ReportCommand());
        server.getCommandManager().register(StaffCommand.createMeta(server.getCommandManager()), new StaffCommand());

        server.getEventManager().register(this, new ChatListener());
        server.getEventManager().register(this, new NotifyListener());
    }
}
