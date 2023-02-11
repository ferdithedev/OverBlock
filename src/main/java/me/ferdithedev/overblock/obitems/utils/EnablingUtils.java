package me.ferdithedev.overblock.obitems.utils;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.ItemPackage;
import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnablingUtils implements Listener {

    private static final List<InventoryPages> openInventories = new ArrayList<>();

    public static void openMenu(Player p) {
        List<ItemStack []> pages = new ArrayList<>();
        Inventory inventory = Bukkit.createInventory(null, 3*9,"Packages");
        List<ItemPackage> itemPackages = new ArrayList<>(OverBlock.getOBItemManager().getItemPackages());

        do {
            Inventory inventory_1 = Bukkit.createInventory(null,3*9," ");
            int i = 0;
            List<ItemPackage> toRemove = new ArrayList<>();
            for(ItemPackage itemPackage : itemPackages) {
                if(i > 17) break;
                toRemove.add(itemPackage);
                ItemStack itemStack = new ItemStack(itemPackage.getIcon());
                ItemMeta m = itemStack.getItemMeta();
                if(m != null) {
                    m.setDisplayName(ChatColor.RESET+itemPackage.getName());
                    List<String> lore = new ArrayList<>();
                    lore.add("§8("+itemPackage.getPlugin().getName()+")");
                    lore.add(" ");
                    if(itemPackage.getDescription().length > 0) {
                        lore.addAll(Arrays.asList(itemPackage.getDescription()));
                    }
                    m.setLore(lore);
                    m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);

                    itemStack.setItemMeta(m);

                    itemStack = ItemUtil.editPersistentDataContainerI(itemStack, "packagename", PersistentDataType.STRING, itemPackage.getInternalName());
                    itemStack = ItemUtil.editPersistentDataContainerI(itemStack,"menuitem",PersistentDataType.BYTE,Byte.valueOf("1"));
                }
                inventory_1.addItem(itemStack);
                i++;
            }

            ItemStack close = new ItemStack(Material.BARRIER);
            ItemMeta meta = close.getItemMeta();
            if(meta != null) {
                meta.setDisplayName(ChatColor.RESET + "Close");
                close.setItemMeta(meta);
            }
            close = ItemUtil.editPersistentDataContainerI(close,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
            close = ItemUtil.editPersistentDataContainerI(close,"close", PersistentDataType.BYTE,Byte.valueOf("1"));
            inventory_1.setItem(22,close);

            itemPackages.removeAll(toRemove);
            pages.add(inventory_1.getContents());
        } while (itemPackages.size() > 0);

        List<ItemStack[]> newPages = new ArrayList<>();
        if(pages.size() > 1) {
            int i = 0;
            for(ItemStack[] items : pages) {
                if(i-1 >= 0 && pages.get(i-1) != null) {
                    ItemStack back = new ItemStack(Material.ARROW);
                    ItemMeta meta = back.getItemMeta();
                    if(meta != null) {
                        meta.setDisplayName(ChatColor.RESET+"Back");
                        back.setItemMeta(meta);
                    }
                    back = ItemUtil.editPersistentDataContainerI(back,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    back = ItemUtil.editPersistentDataContainerI(back,"back", PersistentDataType.BYTE,Byte.valueOf("1"));
                    Inventory inventory1 = Bukkit.createInventory(null,3*9," ");
                    inventory1.setContents(items);
                    inventory1.setItem(18,back);
                    items = inventory1.getContents();
                }
                if(pages.size() > i+1) {
                    ItemStack next = new ItemStack(Material.ARROW);
                    ItemMeta meta = next.getItemMeta();
                    if(meta != null) {
                        meta.setDisplayName(ChatColor.RESET+"Next");
                        next.setItemMeta(meta);
                    }
                    next = ItemUtil.editPersistentDataContainerI(next,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    next = ItemUtil.editPersistentDataContainerI(next,"next", PersistentDataType.BYTE,Byte.valueOf("1"));
                    Inventory inventory1 = Bukkit.createInventory(null,3*9," ");
                    inventory1.setContents(items);
                    inventory1.setItem(26,next);
                    items = inventory1.getContents();
                }
                newPages.add(items);
                i++;
            }
        } else {
            newPages = pages;
        }

        openInventories.add(new InventoryPages(p,0,inventory,newPages));
        openInventory(p,0);
    }

    private static void openItemList(Player p, ItemPackage itemPackage) {
        List<ItemStack[]> pages = new ArrayList<>();
        List<OBItem> items = new ArrayList<>(itemPackage.getItems());

        Inventory inventory = Bukkit.createInventory(null,3*9,itemPackage.getName());

        do {
            Inventory inventory_1 = Bukkit.createInventory(null,3*9," ");
            int i = 0;
            List<OBItem> toRemove = new ArrayList<>();
            for(OBItem item : items) {
                if(i > 8) break;
                toRemove.add(item);
                inventory_1.setItem(i,ItemUtil.editPersistentDataContainerI(item.getItemStack(),"menuitem",PersistentDataType.BYTE,Byte.valueOf("1")));
                ItemStack toggleItem = item.isEnabled() ? new ItemStack(Material.LIME_DYE) : new ItemStack(Material.GRAY_DYE);
                ItemMeta meta = toggleItem.getItemMeta();
                if(meta != null) {
                    String name = item.isEnabled() ? "§aEnabled" : "§7Disabled";
                    meta.setDisplayName(name);
                    toggleItem.setItemMeta(meta);
                }
                String itemType = item.isEnabled() ? "enabled" : "disabled";
                toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,itemType, PersistentDataType.BYTE,Byte.valueOf("1"));
                toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"toggleitem", PersistentDataType.STRING,item.getInternalName());
                inventory_1.setItem(i+9,toggleItem);
                i++;
            }
            items.removeAll(toRemove);

            ItemStack menu = new ItemStack(Material.BARRIER);
            ItemMeta meta = menu.getItemMeta();
            if(meta != null) {
                meta.setDisplayName(ChatColor.RESET + "Menu");
                menu.setItemMeta(meta);
            }
            menu = ItemUtil.editPersistentDataContainerI(menu,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
            menu = ItemUtil.editPersistentDataContainerI(menu,"menu", PersistentDataType.BYTE,Byte.valueOf("1"));
            inventory_1.setItem(22,menu);

            pages.add(inventory_1.getContents());
        } while (items.size() > 0);

        List<ItemStack[]> newPages = new ArrayList<>();
        if(pages.size() > 1) {
            int i = 0;
            for(ItemStack[] pageItems : pages) {
                if(i-1 >= 0 && pages.get(i-1) != null) {
                    ItemStack back = new ItemStack(Material.ARROW);
                    ItemMeta meta = back.getItemMeta();
                    if(meta != null) {
                        meta.setDisplayName(ChatColor.RESET+"Back");
                        back.setItemMeta(meta);
                    }
                    back = ItemUtil.editPersistentDataContainerI(back,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    back = ItemUtil.editPersistentDataContainerI(back,"back", PersistentDataType.BYTE,Byte.valueOf("1"));
                    Inventory inventory1 = Bukkit.createInventory(null,3*9," ");
                    inventory1.setContents(pageItems);
                    inventory1.setItem(18,back);
                    pageItems = inventory1.getContents();
                }
                if(pages.size() > i+1) {
                    ItemStack next = new ItemStack(Material.ARROW);
                    ItemMeta meta = next.getItemMeta();
                    if(meta != null) {
                        meta.setDisplayName(ChatColor.RESET+"Next");
                        next.setItemMeta(meta);
                    }
                    next = ItemUtil.editPersistentDataContainerI(next,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    next = ItemUtil.editPersistentDataContainerI(next,"next", PersistentDataType.BYTE,Byte.valueOf("1"));
                    Inventory inventory1 = Bukkit.createInventory(null,3*9," ");
                    inventory1.setContents(pageItems);
                    inventory1.setItem(26,next);
                    pageItems = inventory1.getContents();
                }

                newPages.add(pageItems);
                i++;
            }
        } else {
            newPages = pages;
        }

        openInventories.add(new InventoryPages(p,0,inventory,newPages));
        openInventory(p,0);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        ItemStack i = e.getCurrentItem();
        if(i.getItemMeta() == null) return;
        if(!(e.getWhoClicked() instanceof Player player)) return;
        List<OBItem> list = null;
        for(ItemPackage itemPackage : OverBlock.getOBItemManager().getItemPackages()) {
            String s = ItemUtil.getToggledItem(i,"packagename");
            if (s != null && s.equalsIgnoreCase(itemPackage.getInternalName())) {
                openItemList(player, itemPackage);
            }
        }
        if(ItemUtil.isByte(i,"next")) {
            openNextPage(player);
        }
        if(ItemUtil.isByte(i,"back")) {
            openPreviousPage(player);
        }
        if(ItemUtil.isByte(i,"menu")) {
            openMenu(player);
        }
        if(ItemUtil.isByte(i,"close")) {
            player.closeInventory();
        }
        if(ItemUtil.isByte(i,"enabled")) {
            String internalItemName = ItemUtil.getToggledItem(i, "toggleitem");
            if(internalItemName != null) {
                OBItem item = OverBlock.getOBItemManager().getOBItemByInternalName(internalItemName);
                if(item != null) {
                    OverBlock.getOBItemManager().changeItemEnabling(item,false);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,0.5f);

                    ItemStack toggleItem = new ItemStack(Material.GRAY_DYE);
                    ItemMeta meta = toggleItem.getItemMeta();
                    if(meta != null) {
                        String name = "§7Disabled";
                        meta.setDisplayName(name);
                        toggleItem.setItemMeta(meta);
                    }
                    String itemType = "disabled";
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,itemType, PersistentDataType.BYTE,Byte.valueOf("1"));
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"toggleitem", PersistentDataType.STRING,item.getInternalName());

                    if(e.getClickedInventory() != null) {
                        e.getClickedInventory().setItem(e.getSlot(),toggleItem);
                    }
                }
            }
        }

        if(ItemUtil.isByte(i,"disabled")) {
            String internalItemName = ItemUtil.getToggledItem(i, "toggleitem");
            if(internalItemName != null) {
                OBItem item = OverBlock.getOBItemManager().getOBItemByInternalName(internalItemName);
                if(item != null) {
                    OverBlock.getOBItemManager().changeItemEnabling(item,true);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1.5f);

                    ItemStack toggleItem = new ItemStack(Material.LIME_DYE);
                    ItemMeta meta = toggleItem.getItemMeta();
                    if(meta != null) {
                        String name = "§aEnabled";
                        meta.setDisplayName(name);
                        toggleItem.setItemMeta(meta);
                    }
                    String itemType = "enabled";
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,itemType, PersistentDataType.BYTE,Byte.valueOf("1"));
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"menuitem", PersistentDataType.BYTE,Byte.valueOf("1"));
                    toggleItem = ItemUtil.editPersistentDataContainerI(toggleItem,"toggleitem", PersistentDataType.STRING,item.getInternalName());

                    if(e.getClickedInventory() != null) {
                        e.getClickedInventory().setItem(e.getSlot(),toggleItem);
                    }
                }
            }
        }
        if(ItemUtil.isByte(i,"menuitem")) e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getPlayer() instanceof Player p) {
            removePlayerFromList(p);
        }
    }

    private static InventoryPages containsPlayer(Player player) {
        for(InventoryPages pages : EnablingUtils.openInventories) {
            if(player.equals(pages.getPlayer())) {
                return pages;
            }
        }
        return null;
    }

    private void openNextPage(Player p) {
        InventoryPages pages = containsPlayer(p);
        if(pages != null) {
            if(pages.getIndex() < pages.getItems().size()-1) {
                openInventory(p, pages.getIndex()+1);
            }
        }
    }

    private void openPreviousPage(Player p) {
        InventoryPages pages = containsPlayer(p);
        if(pages != null) {
            if(pages.getIndex() > 0) {
                openInventory(p, pages.getIndex()-1);
            }
        }
    }

    private void removePlayerFromList(Player p) {
        InventoryPages inventoryPages = containsPlayer(p);
        if(inventoryPages != null) {
            openInventories.remove(inventoryPages);
        }
    }

    private static void openInventory(Player p, int index) {
        InventoryPages inventoryPages = containsPlayer(p);
        if(inventoryPages != null) {
            inventoryPages.getInventory().setContents(inventoryPages.getItems().get(index));
            inventoryPages.setIndex(index);
        }
    }
}
