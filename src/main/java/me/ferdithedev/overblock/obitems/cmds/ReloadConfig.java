package me.ferdithedev.overblock.obitems.cmds;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            OverBlock.getOBItemManager().reloadConfig();
        }
        return false;
    }
}
