package tk.booky.velostaffchat.events.chats;
// Created by booky10 in VeloStaffChat (16:31 19.06.21)

import com.velocitypowered.api.proxy.Player;

public class PlayerStaffChatEvent extends StaffChatEvent<Player> {

    public PlayerStaffChatEvent(String server, String message, Player sender) {
        super(server, message, sender);
    }
}