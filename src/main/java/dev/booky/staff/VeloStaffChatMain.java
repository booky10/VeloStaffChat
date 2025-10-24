package dev.booky.staff;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import dev.booky.staff.commands.StaffCommand;
import dev.booky.staff.listener.ChatListener;
import dev.booky.staff.listener.NotifyListener;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Plugin(
        id = "velostaffchat",
        name = "VeloStaffChat",
        version = "${version}",
        authors = "booky10"
)
@Singleton
public class VeloStaffChatMain {

    private final EventManager events;

    // event listeners
    private final Provider<ChatListener> chatListener;
    private final Provider<NotifyListener> notifyListener;

    // commands
    private final Provider<StaffCommand> staffCommand;

    @Inject
    public VeloStaffChatMain(
            EventManager events,
            Provider<ChatListener> chatListener,
            Provider<NotifyListener> notifyListener,
            Provider<StaffCommand> staffCommand
    ) {
        this.events = events;
        this.chatListener = chatListener;
        this.notifyListener = notifyListener;
        this.staffCommand = staffCommand;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // register events
        this.events.register(this, this.chatListener.get());
        this.events.register(this, this.notifyListener.get());

        // register commands
        this.staffCommand.get().register(this);
    }
}
