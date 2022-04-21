package me.evilterabite.rplace.commands;

import me.evilterabite.rplace.RPlace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaletteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(RPlace.playersInCanvas.contains(player.getUniqueId())) {
                RPlace.paletteGUI.open(player);
            }
        }
        return true;
    }
}
