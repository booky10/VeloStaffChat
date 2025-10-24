package dev.booky.staff.commands;
// Created by booky10 in VeloStaffChat (19:24 24.10.2025)

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

@NullMarked
public abstract class AbstractCommand {

    protected final CommandManager commands;

    private final String[] labels;

    public AbstractCommand(CommandManager commands, String... labels) {
        this.commands = commands;
        if (labels.length < 1) {
            throw new IllegalArgumentException("Commands require at least one label, 0 provided");
        }
        this.labels = labels;
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    protected abstract LiteralArgumentBuilder<CommandSource> provideNode();

    public final void register(Object plugin) {
        BrigadierCommand command = new BrigadierCommand(this.provideNode());
        CommandMeta meta = this.commands.metaBuilder(command)
                .aliases(this.getAliases()).plugin(plugin)
                .build();
        this.commands.register(meta, command);
    }

    public String getMainLabel() {
        return this.labels[0];
    }

    public String[] getLabels() {
        return this.labels.clone();
    }

    public String[] getAliases() {
        if (this.labels.length < 1) {
            // only one main label specified, no aliases present
            return new String[0];
        }
        // return a copy of the labels after the main label
        return Arrays.copyOfRange(this.labels, 1, this.labels.length);
    }
}
