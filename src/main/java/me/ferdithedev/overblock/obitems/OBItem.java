package me.ferdithedev.overblock.obitems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class OBItem {

    private final JavaPlugin plugin;
    private final Material material;
    private final ItemStack itemStack;
    private final String name;
    private final long cooldown;
    private final OBItemType type;
    private final OBItemRarity rarity;
    private final List<Player> users;
    private final String[] lore;
    private final String internalName;
    private ItemPackage itemPackage = null;

    private boolean enabled;

    public OBItem(JavaPlugin plugin, Material material, String name, long cooldown, OBItemType type, OBItemRarity rarity, String... lore) {
        this.plugin = plugin;
        this.material = material;
        this.cooldown = cooldown;
        this.name = name;
        this.internalName = name.toLowerCase().replaceAll(" ","_");
        this.type = type;
        this.rarity = rarity;
        this.lore = lore;

        this.itemStack = setItemStack();

        this.enabled = true;

        users = new ArrayList<>();
    }

    public boolean noCooldown(Player player) {
        if(cooldown > 0) {
            if(!users.contains(player)) {
                users.add(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> users.remove(player), cooldown);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public abstract void function(Player player);

    public abstract void click(PlayerInteractEvent e);

    private ItemStack setItemStack() {
        ItemStack i = new ItemStack(material);
        ItemMeta m = i.getItemMeta();
        if (m == null) return i;
        m.setUnbreakable(true);
        m.setDisplayName(rarity.format(name));
        m.setLore(rarity.lore(lore));
        NamespacedKey key = new NamespacedKey(plugin,internalName);
        m.getPersistentDataContainer().set(key, PersistentDataType.BYTE, Byte.valueOf("1"));
        i.setItemMeta(m);
        return i;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public OBItemType getType() {
        return type;
    }

    public OBItemRarity getRarity() {return  rarity;}

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getInternalName() {
        return internalName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ItemPackage getItemPackage() {
        return itemPackage;
    }

    public void setItemPackage(ItemPackage itemPackage) {
        this.itemPackage = itemPackage;
    }
}
