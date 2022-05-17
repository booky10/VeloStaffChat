package tk.booky.velostaffchat.commands;
// Created by booky10 in VeloStaffChat (15:51 19.06.21)

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import tk.booky.velostaffchat.VeloStaffChat;

import java.util.Collections;
import java.util.List;

public class StaffCommand implements SimpleCommand {

    public static CommandMeta createMeta(CommandManager manager) {
        return manager.metaBuilder("staff").aliases("staffchat", "sc").build();
    }

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player) {
            if (invocation.arguments().length == 1) {
                if (":toggle".equalsIgnoreCase(invocation.arguments()[0])) {
                    boolean permanent = VeloStaffChat.getMain().getManager().togglePermanentStaffChat(((Player) invocation.source()).getUniqueId());
                    String action = permanent ? "immer" : "nicht mehr immer";
                    invocation.source().sendMessage(Component.text("Du schreibst nun " + action + " in den Teamchat!"));
                    return;
                }
            }
        }

        String message = String.join(" ", invocation.arguments());
        VeloStaffChat.getMain().getManager().tryStaffMessage(invocation.source(), message);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return invocation.source() instanceof Player ? Collections.singletonList(":toggle") : ImmutableList.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("staff.chat");
    }
}