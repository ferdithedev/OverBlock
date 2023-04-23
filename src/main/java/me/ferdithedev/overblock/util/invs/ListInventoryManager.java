package me.ferdithedev.overblock.util.invs;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.ItemPackage;
import me.ferdithedev.overblock.obitems.ItemManager;
import me.ferdithedev.overblock.obitems.manage.ItemListInv;
import me.ferdithedev.overblock.obitems.manage.PackageListInv;
import me.ferdithedev.overblock.obitems.packagebrowser.PackageBrowser;
import me.ferdithedev.overblock.obitems.packagebrowser.PackageBrowserInv;
import me.ferdithedev.overblock.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ListInventoryManager implements Listener {

    public static Map<String, ListInventory> inventories = new HashMap<>();
    public static PackageBrowser packageBrowser;

    public ListInventoryManager(ItemManager itemManager) {
        initPackageList(itemManager);
        inventories.put("itemlistinv", new ItemListInv());
        inventories.put("packagebrowser",new PackageBrowserInv());

        packageBrowser = new PackageBrowser(OverBlock.getInstance());
    }

    public void initPackageList(ItemManager itemManager) {
        List<ItemPackage> itemPackages = new ArrayList<>(OverBlock.getItemManager().getItemPackages());
        List<ItemStack> itemStacks = new ArrayList<>();
        for (ItemPackage itemPackage : itemPackages) {
            List<String> description = new ArrayList<>(Arrays.asList(itemPackage.getDescription()));
            description.add(0, "");
            description.add(0, ChatColor.DARK_GRAY + "(" + itemPackage.getPlugin().getName() + ")");
            itemStacks.add(
                    new InventoryItemCreator(new ItemStack(itemPackage.getIcon()))
                            .setDisplayName(itemPackage.getName())
                            .setLore(description.toArray(new String[0]))
                            .addString("packagename", itemPackage.getInternalName()).get()
            );
        }
        inventories.put("packagelistinv", new PackageListInv(itemStacks));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        ItemStack clicked = e.getCurrentItem();
        if (ItemUtil.hasValue(clicked, "c_inv_item", PersistentDataType.BYTE)) {
            //close inv


            ListInventory inventory = inventories.get(ItemUtil.getValue(clicked, "inventoryname", PersistentDataType.STRING));

            //close
            if (ItemUtil.hasValue(clicked, "close", PersistentDataType.BYTE)) e.getWhoClicked().closeInventory();
            //changepage
            if (ItemUtil.hasValue(clicked, "changepage", PersistentDataType.BYTE)) {
                byte value = ItemUtil.getValue(clicked, "changepage", PersistentDataType.BYTE);
                int currentPage = ItemUtil.getValue(clicked, "currentpage", PersistentDataType.INTEGER);
                int nextPage = Byte.valueOf(value).compareTo(Byte.valueOf("1")) == 0 ? 1 : -1;
                inventory.openInventory((Player) e.getWhoClicked(), currentPage + nextPage);
            }
            //enable/disable
            if (ItemUtil.hasValue(clicked, "enabled", PersistentDataType.BYTE)) {
                int index = ItemUtil.getValue(clicked, "enableitem_id", PersistentDataType.INTEGER);
                inventory.toggleEnabled(index);
                //reopen
                if (e.getClickedInventory().getItem(22) != null) {
                    if (ItemUtil.hasValue(e.getClickedInventory().getItem(22), "currentpage", PersistentDataType.INTEGER)) {
                        int currentPage = ItemUtil.getValue(e.getClickedInventory().getItem(22), "currentpage", PersistentDataType.INTEGER);
                        inventory.openInventory((Player) e.getWhoClicked(), currentPage);
                    }
                }

            }
            //trigger on item click
            if (ItemUtil.hasValue(clicked, "contentitem_id", PersistentDataType.INTEGER))
                inventory.onItemClick(clicked, (Player) e.getWhoClicked(), e.getClick());

            inventory.handleClick(e);
            e.setCancelled(true);
        }
    }
}
