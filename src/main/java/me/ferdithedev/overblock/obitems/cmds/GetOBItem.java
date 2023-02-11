package me.ferdithedev.overblock.obitems.cmds;

import me.ferdithedev.overblock.obitems.ItemPackage;
import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.OverBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetOBItem implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length > 0) {
                for(OBItem obitem : OverBlock.getOBItemManager().getOBItems()) {
                    if(args[0].equalsIgnoreCase(obitem.getInternalName()) || args[0].equalsIgnoreCase(obitem.getItemPackage().getInternalName().toLowerCase() + ":" + obitem.getInternalName())) {
                        ((Player) sender).getInventory().addItem(obitem.getItemStack());
                        if(!obitem.isEnabled()) sender.sendMessage("§c§lWARINING! §cThis item is currently not available in-game since it's not enabled in the 'items.yml' at §e'" + obitem.getPlugin().getName() + "->" + obitem.getInternalName() + "->enabled'§c!");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        for(ItemPackage itemPackage : OverBlock.getOBItemManager().getItemPackages()) {
            for(OBItem item : itemPackage.getItems()) {
                list.add(itemPackage.getInternalName().toLowerCase()+ ":" + item.getInternalName());
            }
        }
        return list;
    }
}
