package tk.booky.velostaffchat.messages;
// Created by booky10 in VeloStaffChat (16:50 19.06.21)

import org.jetbrains.annotations.NotNull;

public class DefaultMessageProvider implements IMessageProvider {

    public @NotNull String getPrefix() {
        return "§7[§bStaff§7]§f ";
    }

    @Override
    public @NotNull String getReportFormat() {
        return getPrefix() + "§9%s§f@§9%s§f hat §9%s§f@§9%s§f mit dem Grund \"§9%s§f\" gemeldet!";
    }

    @Override
    public @NotNull String getChatFormat() {
        return getPrefix() + "§f<§9%s§f@§9%s§f> %s";
    }

    @Override
    public @NotNull String getSwitchedFormat() {
        return getPrefix() + "§9%s§f ist von §9%s§f auf §9%s§f gewechselt!";
    }

    @Override
    public @NotNull String getJoinFormat() {
        return getPrefix() + "§9%s§f@§9%s§f ist auf den Server gegangen!";
    }

    @Override
    public @NotNull String getLeaveFormat() {
        return getPrefix() + "§9%s§f@§9%s§f ist von dem Server gegangen!";
    }
}