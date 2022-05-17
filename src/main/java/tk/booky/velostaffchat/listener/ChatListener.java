package tk.booky.velostaffchat.listener;
// Created by booky10 in VeloStaffChat (16:32 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import tk.booky.velostaffchat.VeloStaffChat;

public class ChatListener {

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("staff.chat")) return;

        String message = event.getMessage();
        if (message.startsWith("#")) {
            message = message.substring(1).trim();
        } else if (!VeloStaffChat.getMain().getManager().hasPermanentStaffChat(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setResult(PlayerChatEvent.ChatResult.denied());
        VeloStaffChat.getMain().getManager().tryStaffMessage(event.getPlayer(), message);
    }
}