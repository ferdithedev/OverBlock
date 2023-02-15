package me.ferdithedev.overblock.obitems.packagebrowser;

import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BrowserCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            if(p.isOp()) {
                PackageBrowser browser = ListInventoryManager.packageBrowser;
                if(args.length > 0 && args[0].equals("update")) {
                    browser.updatePackages();
                    return false;
                } else {
                    ListInventoryManager.inventories.get("packagebrowser").openInventory(p,0);
                }

            }
        }
        return false;
    }
}
