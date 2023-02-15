package me.ferdithedev.overblock.obitems.manage;

import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            if(commandSender instanceof Player p) {
                ListInventoryManager.inventories.get("packagelistinv").openInventory(p,0);
            }
        }
        return false;
    }
}
