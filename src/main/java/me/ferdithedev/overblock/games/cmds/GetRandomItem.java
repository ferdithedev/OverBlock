package me.ferdithedev.overblock.games.cmds;

import me.ferdithedev.overblock.games.ItemSpawner;
import me.ferdithedev.overblock.mpitems.OBItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetRandomItem implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player p = (Player)commandSender;
            if(p.hasPermission("mp.getitem")) {
                int i;
                if(args.length == 1 && isNumeric(args[0])) {
                    i = Integer.parseInt(args[0]);
                } else {
                    i = 0;
                }

                OBItem item = ItemSpawner.getItem(i);
                if(item == null) return false;
                p.getInventory().addItem(item.getItemStack());
                p.sendMessage("§aYou got " + item.getName() + " §awith luck of §3" + i);
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
