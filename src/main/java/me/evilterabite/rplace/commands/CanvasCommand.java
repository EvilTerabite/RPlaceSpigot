package me.evilterabite.rplace.commands;

import me.evilterabite.rplace.RPlace;
import me.evilterabite.rplace.libraries.Canvas;
import me.evilterabite.rplace.utils.C;
import me.evilterabite.rplace.utils.ItemCreator;
import me.evilterabite.rplace.utils.Zone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CanvasCommand implements CommandExecutor, Listener {

    public static final ItemStack canvasCreatorItem = ItemCreator.create(
            Material.WOODEN_AXE,
            ChatColor.YELLOW + "Canvas Creator Axe",
            new ArrayList<>(Arrays.asList("Left click for pos 1", "Right click for pos 2")));

    public static final List<Location> posList = new ArrayList<>(Arrays.asList(null, null));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("rplace.admin")) {
                if(args.length >= 1) {
                    switch (args[0]) {
                        case "setpos1": posList.set(0, player.getLocation().subtract(0, 1, 0)); break;
                        case "setpos2": posList.set(1, player.getLocation().subtract(0, 1, 0)); break;
                        case "timer": RPlace.canvas.setPlaceBlockTimer(Integer.parseInt(args[1])); break;
                        case "reset": RPlace.canvas.reset(); break;
                        case "remove": RPlace.canvas.remove(); break;
                        case "create": createCanvas(); break;
                    }
                }
            } else {
                C.noPermission(sender);
            }
        } else {
            C.consoleNotAllowed(sender);
        }
        return true;
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) return;
        if(!Objects.equals(event.getItem(), canvasCreatorItem)) return;
        Location loc = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        Player player = event.getPlayer();
        if(!player.hasPermission("rplace.admin")) return;
        Action action = event.getAction();
        if(action == Action.LEFT_CLICK_BLOCK) {
            posList.set(0, loc);
            player.sendMessage("Pos 1 set");
        }
        if(action == Action.RIGHT_CLICK_BLOCK) {
            posList.set(1, loc);
            player.sendMessage("Pos 2 set");
        }


    }

    private void createCanvas() {
        if(posList.size() == 2) {
            Canvas canvas = new Canvas("test", new Zone(posList.get(0), posList.get(1)), 120);
            canvas.create();
        }
    }
}
