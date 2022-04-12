package me.ferdithedev.overblock.mpitems.cmds;

import me.ferdithedev.overblock.mpitems.ItemPackage;
import me.ferdithedev.overblock.mpitems.MPItem;
import me.ferdithedev.overblock.OverBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetMPItem implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(args.length > 0) {
                for(MPItem mpitem : OverBlock.getMPItemManager().getMPItems()) {
                    if(args[0].equalsIgnoreCase(mpitem.getInternalName()) || args[0].equalsIgnoreCase(mpitem.getItemPackage().getInternalName().toLowerCase() + ":" + mpitem.getInternalName())) {
                        ((Player) sender).getInventory().addItem(mpitem.getItemStack());
                        if(!mpitem.isEnabled()) sender.sendMessage("§c§lWARINING! §cThis item is currently not available in-game since it's not enabled in the 'items.yml' at §e'" + mpitem.getPlugin().getName() + "->" + mpitem.getInternalName() + "->enabled'§c!");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        for(ItemPackage itemPackage : OverBlock.getMPItemManager().getItemPackages()) {
            for(MPItem item : itemPackage.getItems()) {
                list.add(itemPackage.getInternalName().toLowerCase()+ ":" + item.getInternalName());
            }
        }
        return list;
    }
}
