package me.ferdithedev.overblock.util.invs;

import me.ferdithedev.overblock.util.ItemUtil;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class InventoryItemCreator {

    private ItemStack itemStack;

    public InventoryItemCreator(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public InventoryItemCreator setDisplayName(String s) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(s);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public InventoryItemCreator setLore(String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public InventoryItemCreator addItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public InventoryItemCreator addByte(String name, int value) {
        itemStack = ItemUtil.setValue(itemStack, name, PersistentDataType.BYTE,Byte.valueOf(String.valueOf(value)));
        return this;
    }

    public InventoryItemCreator addString(String name, String value) {
        itemStack = ItemUtil.setValue(itemStack, name, PersistentDataType.STRING,value);
        return this;
    }

    public InventoryItemCreator addInt(String name, int value) {
        itemStack = ItemUtil.setValue(itemStack, name, PersistentDataType.INTEGER,value);
        return this;
    }

    public ItemStack get() {
        return ItemUtil.setValue(itemStack,"c_inv_item",PersistentDataType.BYTE,Byte.valueOf("1"));
    }
}
