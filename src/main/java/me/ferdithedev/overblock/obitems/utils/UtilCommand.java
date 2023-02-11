package me.ferdithedev.overblock.obitems.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            if(commandSender instanceof Player p) {
                EnablingUtils.openMenu(p);
            }
        }
        return false;
    }
}
