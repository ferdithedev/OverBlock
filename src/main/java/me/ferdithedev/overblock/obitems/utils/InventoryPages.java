package me.ferdithedev.overblock.obitems.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryPages {

    private final Player player;
    private int index;
    private final List<ItemStack []> items;
    private final Inventory inventory;

    public InventoryPages(Player p, int index, Inventory inventory, List<ItemStack[]> items) {
        this.player = p;
        this.items = items;
        this.inventory = inventory;
        p.openInventory(inventory);
    }

    public Player getPlayer() {
        return player;
    }

    public List<ItemStack []> getItems() {
        return items;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
