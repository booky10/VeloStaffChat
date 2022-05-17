package dev.booky.staff.listener;
// Created by booky10 in VeloStaffChat (16:32 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import dev.booky.staff.StaffChatManager;

public record ChatListener(StaffChatManager manager) {

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("staff.chat")) return;

        String message = event.getMessage();
        if (message.startsWith("#")) {
            message = message.substring(1).trim();
        } else if (!manager.toggledStaffChat(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setResult(PlayerChatEvent.ChatResult.denied());
        manager.messageStaff(event.getPlayer(), message);
    }
}
