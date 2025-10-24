package dev.booky.staff.commands;
// Created by booky10 in VeloStaffChat (19:19 24.10.2025)

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.booky.staff.StaffChatManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NullMarked;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static dev.booky.staff.util.StaffChatConstants.PREFIX;
import static dev.booky.staff.util.StaffChatConstants.STAFF_COMMAND_PERMISSION;
import static dev.booky.staff.util.StaffChatConstants.STAFF_USE_PERMISSION;
import static net.kyori.adventure.text.Component.text;

@NullMarked
@Singleton
public final class StaffCommand extends AbstractCommand {

    private final StaffChatManager manager;

    @Inject
    public StaffCommand(CommandManager commands, StaffChatManager manager) {
        super(commands, "staff");
        this.manager = manager;
    }

    @Override
    protected LiteralArgumentBuilder<CommandSource> provideNode() {
        return literal(this.getMainLabel())
                .requires(source -> source.hasPermission(STAFF_COMMAND_PERMISSION))
                .then(literal("toggle")
                        .requires(source -> source instanceof Player)
                        .executes(ctx -> this.executeToggle((Player) ctx.getSource())))
                .then(argument("text", greedyString())
                        .requires(source -> source.hasPermission(STAFF_USE_PERMISSION))
                        .executes(ctx -> this.executeChat(ctx.getSource(), getString(ctx, "text"))));
    }

    private int executeToggle(Player player) {
        boolean newState = this.manager.toggleStaffChat(player.getUniqueId());
        String newStateStr = newState ? "always-on" : "no longer always-on";
        player.sendMessage(text().append(PREFIX)
                .append(text("Staff chat is now " + newStateStr)));
        return newState ? 1 : 0;
    }

    private int executeChat(CommandSource source, String text) {
        this.manager.sendStaffMessage(source, text);
        return 1;
    }
}
