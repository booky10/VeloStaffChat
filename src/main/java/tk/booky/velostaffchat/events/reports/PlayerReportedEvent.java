package tk.booky.velostaffchat.events.reports;
// Created by booky10 in VeloStaffChat (16:01 19.06.21)

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;

public class PlayerReportedEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private GenericResult result = GenericResult.allowed();
    private CommandSource reporter;
    private Player target;
    private String reason;

    public PlayerReportedEvent(CommandSource reporter, Player target, String reason) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    public CommandSource getReporter() {
        return reporter;
    }

    public Player getTarget() {
        return target;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = result;
    }

    public void setReporter(CommandSource reporter) {
        this.reporter = reporter;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}