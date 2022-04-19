package me.evilterabite.rplace.utils;

import org.bukkit.command.CommandSender;

public class C {

    public static void consoleNotAllowed(CommandSender sender) {
        sender.sendMessage("Not allowed on console");
    }

    public static void noPermission(CommandSender sender) {
        sender.sendMessage("No Permission");
    }
}
