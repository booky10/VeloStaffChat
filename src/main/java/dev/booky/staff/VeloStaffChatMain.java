package dev.booky.staff;

import com.google.inject.Inject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.booky.staff.listener.ChatListener;
import dev.booky.staff.listener.NotifyListener;
import dev.booky.staff.util.StaffChatConstants;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
@Plugin(
        id = "velostaffchat",
        name = "VeloStaffChat",
        version = "${version}",
        authors = "booky10"
)
public class VeloStaffChatMain {

    private final StaffChatManager manager;
    private final ProxyServer server;

    @Inject
    public VeloStaffChatMain(
            StaffChatManager manager,
            ProxyServer server
    ) {
        this.manager = manager;
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register(new BrigadierCommand(LiteralArgumentBuilder.
                <CommandSource>literal("staff")
                .requires(source -> source.hasPermission("staff.command"))
                .then(LiteralArgumentBuilder.<CommandSource>literal("toggle")
                        .requires(source -> source instanceof Player)
                        .executes(context -> {
                            boolean newVal = manager.toggleStaffChat(((Player) context.getSource()).getUniqueId());
                            String msg = "Toggled staff chat " + (newVal ? "on" : "off");
                            context.getSource().sendMessage(Component.text(msg));
                            return 1;
                        }))
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("text", StringArgumentType.greedyString())
                        .requires(source -> source.hasPermission(StaffChatConstants.STAFF_USE_PERMISSION))
                        .executes(context -> {
                            manager.sendStaffMessage(context.getSource(), StringArgumentType.getString(context, "text"));
                            return 1;
                        }))));

        server.getEventManager().register(this, new ChatListener(manager));
        server.getEventManager().register(this, new NotifyListener(manager));
    }
}
