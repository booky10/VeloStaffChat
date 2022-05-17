package tk.booky.velostaffchat.events.chats;
// Created by booky10 in VeloStaffChat (16:32 19.06.21)

import com.velocitypowered.api.command.CommandSource;

public class GenericStaffChatEvent extends StaffChatEvent<CommandSource> {

    public GenericStaffChatEvent(String server, String message, CommandSource sender) {
        super(server, message, sender);
    }
}