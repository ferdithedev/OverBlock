package me.ferdithedev.overblock.obitems.manage;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.util.ItemUtil;
import me.ferdithedev.overblock.util.invs.ListInventory;
import me.ferdithedev.overblock.util.invs.ListInventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class ItemListInv extends ListInventory {

    public ItemListInv() {
        super(null, true, null, "itemlistinv", "n");
    }

    @Override
    public void onItemEnable(ItemStack i) {
        if(!ItemUtil.hasValue(i,"internalname", PersistentDataType.STRING)) return;
        String internalName = ItemUtil.getValue(i,"internalname",PersistentDataType.STRING);
        OBItem item = OverBlock.getItemManager().getOBItemByInternalName(internalName);
        if(item != null) {
            OverBlock.getItemManager().changeItemEnabling(item,true);
        }
    }

    @Override
    public void onItemDisable(ItemStack i) {
        if(!ItemUtil.hasValue(i,"internalname", PersistentDataType.STRING)) return;
        String internalName = ItemUtil.getValue(i,"internalname",PersistentDataType.STRING);
        OBItem item = OverBlock.getItemManager().getOBItemByInternalName(internalName);
        if(item != null) {
            OverBlock.getItemManager().changeItemEnabling(item,false);
        }
    }

    @Override
    public void onItemClick(ItemStack i, Player clicker, ClickType clickType) {
        if(clickType == ClickType.DOUBLE_CLICK) {
            clicker.getInventory().addItem(OverBlock.getItemManager().getOBItemByItemStack(i).getItemStack());
        }
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(ItemUtil.hasValue(e.getCurrentItem(),"close",PersistentDataType.BYTE)) {
            ListInventoryManager.inventories.get("packagelistinv").openInventory((Player) e.getWhoClicked(),0);
        }
    }

}
