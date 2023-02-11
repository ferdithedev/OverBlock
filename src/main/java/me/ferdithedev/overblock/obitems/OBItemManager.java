package me.ferdithedev.overblock.obitems;

import me.ferdithedev.overblock.fm.Config;
import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.impl.BoostStaff;
import me.ferdithedev.overblock.obitems.impl.Flamethrower;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class OBItemManager implements Listener {

    private final Random random = new Random();

    private final List<ItemPackage> itemPackages = new ArrayList<>();
    private final List<OBItem> allItems = new ArrayList<>();
    private YamlConfiguration itemsConfig;
    private final File itemsFile;

    public OBItemManager(JavaPlugin plugin) {
        new Config(plugin,"items.yml", true);
        itemsFile = new File(plugin.getDataFolder(), "items.yml");
        itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);

        registerPresetOBItems(plugin);
    }

    public List<OBItem> getOBItems() {
        return allItems;
    }

    public List<ItemPackage> getItemPackages() {
        return itemPackages;
    }

    private void registerPresetOBItems(JavaPlugin plugin) {
        ItemPackage overBlock = new ItemPackage(plugin, "OverBlock","§e§kM§r§d§lOverBlock§r§e§kM§r", Material.IRON_AXE, "§eDefault ItemPackage of OverBlock","§eJust the basics");
        overBlock.addItem(new BoostStaff(plugin));
        overBlock.addItem(new Flamethrower(plugin));
        registerItemPackage(overBlock);
    }

    public void changeItemEnabling(OBItem item, boolean enable) {
        ItemPackage itemPackage = item.getItemPackage();
        List<OBItem> items = itemPackage.getItems();
        for(OBItem item1 : items) {
            if(item1.getInternalName().equals(item.getInternalName())) {
                item1.setEnabled(enable);
                getItemsConfig().set(item1.getItemPackage().getInternalName()+"."+item1.getInternalName()+".enabled",enable);
                saveConfig();
                itemPackage.addItem(item1);
            }
        }
        unregisterItemPackage(itemPackage);

        itemPackage = new ItemPackage(itemPackage.getPlugin(), itemPackage.getInternalName(), itemPackage.getName(), itemPackage.getIcon(), items, itemPackage.getDescription());

        registerItemPackage(itemPackage);
    }

    public void registerItemPackage(ItemPackage itemPackage) {
        itemPackages.add(itemPackage);
        for(OBItem item : itemPackage.getItems()) {
            String configPath = itemPackage.getInternalName()+"."+item.getInternalName()+".enabled";

            if(this.itemsConfig.contains(configPath)) {
                item.setEnabled(this.itemsConfig.getBoolean(configPath));
                getItemsConfig().set(configPath,this.itemsConfig.getBoolean(configPath));
            } else {
                this.itemsConfig.set(configPath,true);
            }

            item.setItemPackage(itemPackage);
            if(!allItems.contains(item)) allItems.add(item);
        }

        saveConfig();

    }

    public void unregisterItemPackage(ItemPackage itemPackage) {
        for(OBItem item : itemPackage.getItems()) {
            allItems.remove(item);
            item.setItemPackage(null);
        }
        itemPackages.remove(itemPackage);

    }

    public void reregisterItemPackages() {
        for(ItemPackage itemPackage : itemPackages) {
            unregisterItemPackage(itemPackage);
            registerItemPackage(itemPackage);
        }

    }

    public static void cooldownMessage(Player player) {
        player.sendMessage("§cDon't try this too fast!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1 , 0.8f);
    }

    public OBItem getOBItemByInternalName(String internalName) {
        for(OBItem item : allItems) {
            if(item.getInternalName().equalsIgnoreCase(internalName)) return item;
        }
        return null;
    }

    public OBItem getOBItemByItemStack(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return null;
        for (OBItem obitem : allItems) {
            NamespacedKey key = new NamespacedKey(obitem.getPlugin(),obitem.getInternalName());
            if(meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return obitem;
        }
        return null;
    }

    public OBItem getRandomOBItemByRarity(OBItemRarity rarity) {
        List<OBItem> items = new ArrayList<>(allItems);
        Collections.shuffle(items);
        for(OBItem item : items) {
            if(item.isEnabled() && item.getRarity() == rarity) {
                return item;
            }
        }

        return null;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        OBItem obitem = getOBItemByItemStack(e.getItem());
        if(obitem != null) {
            obitem.click(e);
        }
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        OverBlock.print("================");
        OverBlock.print("Registered Items: ");
        for(ItemPackage itemPackage : itemPackages) {
            OverBlock.print("- " + itemPackage.getInternalName() + ":");
            for(OBItem item : itemPackage.getItems()) {
                OverBlock.print("   " + item.getName());
            }
        }
        OverBlock.print("================");
    }

    public void reloadConfig() {
        this.itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);
        reregisterItemPackages();
    }

    public void saveConfig() {
        try {
            this.itemsConfig.save(itemsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getItemsConfig() {
        return itemsConfig;
    }
}
