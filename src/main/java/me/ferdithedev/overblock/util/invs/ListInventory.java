package me.ferdithedev.overblock.util.invs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ListInventory {

    private Inventory inventory;
    private List<ItemStack[]> pages;
    private List<ItemStack> items;
    private List<Boolean> enabled;
    private final String id;
    private final boolean enableable;

    public ListInventory(List<ItemStack> items, boolean enableable, List<Boolean> enabled, String id, String name) {
        inventory = Bukkit.createInventory(null,3*9,name);
        this.enableable = enableable;
        if(items == null) {
            items = new ArrayList<>();
        }
        this.items = items;
        this.id = id;
        if(enabled == null || enabled.size() < items.size()) {
            enabled = new ArrayList<>();
            while (enabled.size()<items.size()) {
                enabled.add(false);
            }
        }
        this.enabled = enabled;
        createPages();
    }

    private void createPages() {
        pages = new ArrayList<>();
        int amount = (int) Math.ceil((float) items.size()/9);
        List<ItemStack> itemsCopy = new ArrayList<>(items);
        List<Boolean> enabledCopy = new ArrayList<>(enabled);
        for(int i = 0; i<amount; i++) {
            List<ItemStack> contents = new ArrayList<>();

            //content items
            while (contents.size() < 27) {
                contents.add(new ItemStack(Material.AIR));
            }

            for(int j = 0; j<9; j++) {
                if(!(itemsCopy.size() > 0)) break;
                contents.set(j,
                        new InventoryItemCreator(itemsCopy.get(0))
                                .addString("inventoryname",id)
                                .addInt("contentitem_id",i*9+j).get()
                        );
                itemsCopy.remove(0);
                if(enableable) {
                    ItemStack enabledItem;
                    InventoryItemCreator item;
                    if(enabledCopy.get(0)) {
                        item = new InventoryItemCreator(new ItemStack(Material.LIME_DYE))
                                .setDisplayName(ChatColor.GREEN + "Enabled")
                                .addByte("enabled",1);
                    } else {
                        item = new InventoryItemCreator(new ItemStack(Material.GRAY_DYE))
                                .setDisplayName(ChatColor.RED + "Disabled")
                                .addByte("enabled",0);
                    }

                    item = item.addInt("enableitem_id", i*9+j)
                                    .addString("inventoryname",id);
                    contents.set(j+9,item.get());
                    enabledCopy.remove(0);
                }
            }

            //page change
            //data container change page item 1 = next, 0 = back
            if(amount>i+1) {
                contents.set(26,
                        new InventoryItemCreator(new ItemStack(Material.ARROW))
                                .setDisplayName(ChatColor.GRAY + "Next ->")
                                .addInt("currentpage",i)
                                .addString("inventoryname",id)
                                .addByte("changepage",1).get()
                        );
            }

            if(i>0) {
                contents.set(18,
                        new InventoryItemCreator(new ItemStack(Material.ARROW))
                                .setDisplayName(ChatColor.GRAY + "<- Back")
                                .addInt("currentpage",i)
                                .addString("inventoryname",id)
                                .addByte("changepage",0).get()
                        );
            }

            //close
            contents.set(22,
                    new InventoryItemCreator(new ItemStack(Material.BARRIER))
                            .setDisplayName(ChatColor.RED + "Back")
                            .addByte("close",1)
                            .addInt("currentpage",i)
                            .addString("inventoryname",id).get()
                    );

            //finish
            ItemStack[] contentsArray = contents.toArray(new ItemStack[0]);
            pages.add(contentsArray);
        }
    }

    public void openInventory(Player player, int page) {
        inventory.setContents(getContents(page));
        player.openInventory(inventory);
    }

    private ItemStack[] getContents(int page) {
        return pages.get(page);
    }

    public void updateItems(List<ItemStack> items) {
        this.items = items;
    }

    public void updateEnabled(List<Boolean> enabled) {
        this.enabled = enabled;
    }

    public void update() {
        createPages();
    }

    public void toggleEnabled(int index) {
        if(enabled.size() < index+1) return;
        if(enabled.get(index)) {
            onItemDisable(items.get(index));
            enabled.set(index,false);
        } else {
            onItemEnable(items.get(index));
            enabled.set(index,true);
        }
        createPages();
    }

    public void updateName(String name) {
        inventory = Bukkit.createInventory(null, 3*9, name);
        createPages();
    }

    public abstract void onItemEnable(ItemStack i);

    public abstract void onItemDisable(ItemStack i);

    public abstract void onItemClick(ItemStack i, Player clicker);

    public abstract void handleClick(InventoryClickEvent e);
}
