package tk.booky.velostaffchat.events.chats;
// Created by booky10 in VeloStaffChat (16:29 19.06.21)

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;

public class StaffChatEvent<T extends CommandSource> implements ResultedEvent<ResultedEvent.GenericResult> {

    private GenericResult result = GenericResult.allowed();
    private String server, message;
    private T sender;

    public StaffChatEvent(String server, String message, T sender) {
        this.server = server;
        this.message = message;
        this.sender = sender;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    public String getServer() {
        return server;
    }

    public String getMessage() {
        return message;
    }

    public T getSender() {
        return sender;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = result;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(T sender) {
        this.sender = sender;
    }
}