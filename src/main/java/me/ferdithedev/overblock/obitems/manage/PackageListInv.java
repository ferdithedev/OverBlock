package me.ferdithedev.overblock.obitems.manage;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.ItemPackage;
import me.ferdithedev.overblock.util.ItemUtil;
import me.ferdithedev.overblock.util.invs.ListInventory;
import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PackageListInv extends ListInventory {
    public PackageListInv(List<ItemStack> items) {
        super(items, false, null, "packagelistinv", "Installed Packages");
    }

    @Override
    public void onItemEnable(ItemStack i) {

    }

    @Override
    public void onItemDisable(ItemStack i) {

    }

    @Override
    public void onItemClick(ItemStack i, Player clicker, ClickType clickType) {
        if(ItemUtil.hasValue(i,"packagename", PersistentDataType.STRING)) {
            String packageName = ItemUtil.getValue(i,"packagename",PersistentDataType.STRING);
            for(ItemPackage itemPackage : OverBlock.getOBItemManager().getItemPackages()) {
                assert packageName != null;
                if (packageName.equalsIgnoreCase(itemPackage.getInternalName())) {
                    //open items
                    //System.out.println("Open items for package " + itemPackage.getInternalName());
                    List<ItemStack> items = new ArrayList<>();
                    itemPackage.getItems().forEach(item -> items.add(ItemUtil.setValue(item.getItemStack(),"internalname",PersistentDataType.STRING,item.getInternalName())));
                    List<Boolean> enabled = new ArrayList<>();
                    itemPackage.getItems().forEach(item -> enabled.add(item.isEnabled()));
                    ItemListInv inventory = (ItemListInv) ListInventoryManager.inventories.get("itemlistinv");
                    inventory.updateName(itemPackage.getName());
                    inventory.updateItems(items);
                    inventory.updateEnabled(enabled);
                    inventory.update();
                    inventory.openInventory(clicker,0);
                }
            }
        }
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }
}
