package me.ferdithedev.overblock.obitems.packagebrowser;

import me.ferdithedev.overblock.util.invs.ListInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PackageBrowserInv extends ListInventory {

    public PackageBrowserInv() {
        super(null, false, null,"packagebrowser", "Package Browser");
    }

    @Override
    public void onItemEnable(ItemStack i) {
        System.out.println(i.getType().toString() + i.getAmount() + " enabled ");
    }

    @Override
    public void onItemDisable(ItemStack i) {
        System.out.println(i.getType().toString() + i.getAmount() + " disabled ");
    }

    @Override
    public void onItemClick(ItemStack i, Player clicker) {

    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }
}
