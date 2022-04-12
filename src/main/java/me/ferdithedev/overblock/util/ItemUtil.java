package me.ferdithedev.overblock.util;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemUtil {

    public static <T, Z> ItemStack editPersistentDataContainerI(ItemStack input, String keyString, PersistentDataType<T, Z> type, Z value) {
        ItemStack newItem = input.clone();
        ItemMeta fillerItemMeta = newItem.getItemMeta();

        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), keyString);
        if (fillerItemMeta != null) {
            fillerItemMeta.getPersistentDataContainer().set(key, type, value);
        }

        newItem.setItemMeta(fillerItemMeta);
        return newItem;
    }

    public static boolean isByte(ItemStack i, String what) {
        if(!i.hasItemMeta()) return false;
        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), what);
        ItemMeta itemMeta = i.getItemMeta();
        PersistentDataContainer container;
        if (itemMeta != null) {
            container = itemMeta.getPersistentDataContainer();
            return container.has(key, PersistentDataType.BYTE);
        }
        return false;
    }

    public static String getToggledItem(ItemStack i, String what) {
        if(!i.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(OverBlock.getInstance(), what);
        ItemMeta itemMeta = i.getItemMeta();
        PersistentDataContainer container;
        if (itemMeta != null) {
            container = itemMeta.getPersistentDataContainer();
            if(!container.has(key,PersistentDataType.STRING)) return null;
            return container.get(key, PersistentDataType.STRING);
        }
        return null;
    }

}
