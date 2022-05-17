package tk.booky.velostaffchat.utils;
// Created by booky10 in VeloStaffChat (17:04 19.06.21)

import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public enum SwitchAction {

    SWITCH((from, to) -> from != null && to != null),
    JOIN((from, to) -> from == null && to != null),
    LEAVE((from, to) -> from != null && to == null),
    UNKNOWN((from, to) -> true);

    private static final SwitchAction[] VALUES = values();
    private final BiFunction<String, String, Boolean> predicate;

    SwitchAction(BiFunction<String, String, Boolean> predicate) {
        this.predicate = predicate;
    }

    public BiFunction<String, String, Boolean> getPredicate() {
        return predicate;
    }

    public static SwitchAction getAction(@Nullable String from, @Nullable String to) {
        for (SwitchAction action : VALUES) {
            if (!action.getPredicate().apply(from, to)) continue;
            return action;
        }

        throw new IllegalStateException("Wait! That's illegal! (from=" + from + ", to=" + to + ")");
    }
}