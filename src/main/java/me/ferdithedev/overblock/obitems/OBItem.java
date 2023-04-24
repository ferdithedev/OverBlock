package me.ferdithedev.overblock.obitems;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
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
            return !users.contains(player);
        } else {
            return true;
        }
    }

    public void addCooldown(Player player) {
        if(!users.contains(player)) {
            users.add(player);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> users.remove(player), cooldown);
        }
    }

    public abstract boolean function(Player player);

    public boolean click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            return function(e.getPlayer());
        }
        e.setCancelled(true);
        return false;
    }

    private ItemStack setItemStack() {
        ItemStack i = new ItemStack(material, 1);
        ItemMeta m = i.getItemMeta();
        if (m == null) return i;
        m.setUnbreakable(true);
        m.setDisplayName(rarity.format(name));
        m.setLore(rarity.lore(lore));
        NamespacedKey key = new NamespacedKey(plugin,internalName);
        m.getPersistentDataContainer().set(key, PersistentDataType.BYTE, Byte.valueOf("1"));
        m.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("",0, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
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
