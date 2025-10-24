package dev.booky.staff.listener;
// Created by booky10 in VeloStaffChat (16:32 19.06.21)

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import dev.booky.staff.StaffChatManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NullMarked;

import static dev.booky.staff.util.StaffChatConstants.MESSAGE_PREFIX;
import static dev.booky.staff.util.StaffChatConstants.STAFF_USE_PERMISSION;

@NullMarked
@Singleton
public final class ChatListener {

    private final StaffChatManager manager;

    @Inject
    public ChatListener(StaffChatManager manager) {
        this.manager = manager;
    }

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (!event.getPlayer().hasPermission(STAFF_USE_PERMISSION)) {
            return; // doesn't have permission, ignore
        }

        String input = event.getMessage();
        String message;
        if (!input.isEmpty() && input.charAt(0) == MESSAGE_PREFIX) {
            // send message to staff chat
            message = input.substring(1).trim();
        } else if (this.manager.hasToggledStaffChat(event.getPlayer().getUniqueId())) {
            // send whole message to staff chat, player has staff chat always enabled
            message = input;
        } else {
            return; // don't send staff message
        }

        // deny sending message normally and send to staff chat
        event.setResult(PlayerChatEvent.ChatResult.denied());
        this.manager.sendStaffMessage(event.getPlayer(), message);
    }
}
