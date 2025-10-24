package dev.booky.staff.util;
// Created by booky10 in VeloStaffChat (17:46 24.10.2025)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

@NullMarked
public final class StaffChatConstants {

    public static final Component PREFIX = text().color(WHITE)
            .append(text('[', GRAY))
            .append(text("Staff", TextColor.color(0x216844)))
            .append(text(']', GRAY))
            .append(space())
            .build();

    public static final String STAFF_USE_PERMISSION = "staff.chat";
    public static final String STAFF_SEE_PERMISSION = "staff.see";
    public static final String STAFF_NOTIFY_PERMISSION = "staff.notify";
    public static final String STAFF_COMMAND_PERMISSION = "staff.command";

    public static final char MESSAGE_PREFIX = '#';

    private StaffChatConstants() {
    }
}
