package me.ferdithedev.overblock.obitems.packagebrowser;

import me.ferdithedev.overblock.util.ItemUtil;
import me.ferdithedev.overblock.util.invs.ListInventory;
import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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
    public void onItemClick(ItemStack i, Player clicker, ClickType clickType) {
        if(ItemUtil.hasValue(i,"name", PersistentDataType.STRING)) {
            PackageBrowser.OnlinePackage onlinePackage = ListInventoryManager.packageBrowser.getPackageByName(ItemUtil.getValue(i,"name",PersistentDataType.STRING));
            if(onlinePackage != null) onlinePackage.download(clicker);
        }

    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }
}
