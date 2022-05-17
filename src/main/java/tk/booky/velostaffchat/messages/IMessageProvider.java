package tk.booky.velostaffchat.messages;
// Created by booky10 in VeloStaffChat (16:49 19.06.21)

import org.jetbrains.annotations.NotNull;

public interface IMessageProvider {

    @NotNull String getReportFormat();

    @NotNull String getChatFormat();

    @NotNull String getSwitchedFormat();

    @NotNull String getJoinFormat();

    @NotNull String getLeaveFormat();
}