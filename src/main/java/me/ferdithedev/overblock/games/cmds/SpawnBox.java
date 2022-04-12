package me.ferdithedev.overblock.games.cmds;

import me.ferdithedev.overblock.games.ItemSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnBox implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender.isOp()) {
            if(commandSender instanceof Player p) {
                if(args.length > 0) {
                    if(isNumeric(args[0])) {
                        int i = Integer.parseInt(args[0]);
                        ItemSpawner.spawnItem(i,p.getLocation().subtract(0,2,0));
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
