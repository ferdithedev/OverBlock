package me.ferdithedev.overblock.util;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {

    public static <T, Z> ItemStack setValue(ItemStack input, String keyString, PersistentDataType<T, Z> type, Z value) {
        ItemStack newItem = input.clone();
        ItemMeta fillerItemMeta = newItem.getItemMeta();

        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), keyString);
        if (fillerItemMeta != null) {
            fillerItemMeta.getPersistentDataContainer().set(key, type, value);
        }

        newItem.setItemMeta(fillerItemMeta);
        return newItem;
    }

    public static <T,Z> boolean hasValue(ItemStack i, String what, PersistentDataType<T,Z> type) {
        if(!i.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), what);
        ItemMeta itemMeta = i.getItemMeta();
        PersistentDataContainer container;
        if (itemMeta != null) {
            return itemMeta.getPersistentDataContainer().has(key,type);
        }
        return false;
    }

    public static <T,Z> Z getValue(ItemStack i, String what, PersistentDataType<T,Z> type) {
        if(!i.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), what);
        ItemMeta itemMeta = i.getItemMeta();
        PersistentDataContainer container;
        if (itemMeta != null) {
            container = itemMeta.getPersistentDataContainer();
            if(container.has(key,type)) {
                return container.get(key,type);
            }
        }
        return null;
    }

}
