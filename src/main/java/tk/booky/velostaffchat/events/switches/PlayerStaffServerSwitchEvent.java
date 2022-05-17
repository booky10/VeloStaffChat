package tk.booky.velostaffchat.events.switches;
// Created by booky10 in VeloStaffChat (17:10 19.06.21)

import com.velocitypowered.api.event.ResultedEvent;
import org.jetbrains.annotations.Nullable;
import tk.booky.velostaffchat.utils.SwitchAction;

public class PlayerStaffServerSwitchEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private GenericResult result = GenericResult.allowed();
    private String player, from, to;
    private SwitchAction action;

    public PlayerStaffServerSwitchEvent(String player, @Nullable String from, @Nullable String to, SwitchAction action) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.action = action;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    public String getPlayer() {
        return player;
    }

    public @Nullable String getFrom() {
        return from;
    }

    public @Nullable String getTo() {
        return to;
    }

    public SwitchAction getAction() {
        return action;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = result;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setFrom(@Nullable String from) {
        this.from = from;
    }

    public void setTo(@Nullable String to) {
        this.to = to;
    }

    public void setAction(SwitchAction action) {
        this.action = action;
    }
}