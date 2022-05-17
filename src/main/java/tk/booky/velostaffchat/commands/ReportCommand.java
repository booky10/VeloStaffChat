package tk.booky.velostaffchat.commands;
// Created by booky10 in VeloStaffChat (15:51 19.06.21)

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import tk.booky.velostaffchat.VeloStaffChat;

import java.util.Arrays;
import java.util.Optional;

public class ReportCommand implements SimpleCommand {

    public static CommandMeta createMeta(CommandManager manager) {
        return manager.metaBuilder("report").aliases("wdr").build();
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.arguments().length >= 2) {
            Optional<Player> target = VeloStaffChat.getMain().getServer().getPlayer(invocation.arguments()[0]);
            if (target.isPresent()) {
                String[] reasonSplit = Arrays.copyOfRange(invocation.arguments(), 1, invocation.arguments().length);
                String reason = String.join(" ", reasonSplit);

                ResultedEvent.GenericResult result = VeloStaffChat.getMain().getManager().tryReportPlayer(invocation.source(), target.get(), reason).join();
                if (result.isAllowed()) {
                    invocation.source().sendMessage(Component.text("§aDu hast den Spieler " + target.get().getUsername() + "§a erfolgreich gemeldet!"));
                } else {
                    invocation.source().sendMessage(Component.text("§cDu darfst den Spieler nicht melden!"));
                }
            } else {
                invocation.source().sendMessage(Component.text("§cDer Spieler konnte nicht gefunden werden!"));
            }
        } else {
            invocation.source().sendMessage(Component.text("§cBitte gebe Spieler und Grund an!"));
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("staff.report");
    }
}