package me.ferdithedev.overblock.obitems;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public record ItemPackage(JavaPlugin plugin, String internalName,
                          String name, Material icon,
                          List<OBItem> items,
                          String... description) {

    public ItemPackage(JavaPlugin plugin, String internalName, String name, Material icon, List<OBItem> items, String... description) {
        this.name = name;
        this.icon = icon;
        this.items = items == null ? new ArrayList<>() : items;
        this.plugin = plugin;
        this.internalName = internalName;

        this.description = description != null ? description : new String[]{};
    }

    public void addItem(OBItem item) {
        if (!items.contains(item)) items.add(item);
    }

    public void removeItem(OBItem item) {
        items.remove(item);
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public Material getIcon() {
        return icon;
    }

    public List<OBItem> getItems() {
        return items;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String[] getDescription() {
        return description;
    }
}
