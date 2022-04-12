package me.ferdithedev.overblock.mpitems;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemPackage {

    private final String name;
    private final Material icon;
    private final List<MPItem> items;
    private final JavaPlugin plugin;
    private final String[] description;
    private final String internalName;

    public ItemPackage(JavaPlugin plugin, String internalName, String name, Material icon, String... description) {
        this.name = name;
        this.icon = icon;
        this.items = new ArrayList<>();
        this.plugin = plugin;
        this.internalName = internalName;

        this.description = description != null ? description : new String[]{};
    }

    public ItemPackage(JavaPlugin plugin, String internalName, String name, Material icon, List<MPItem> items, String... description) {
        this.name = name;
        this.icon = icon;
        this.items = items;
        this.plugin = plugin;
        this.internalName = internalName;

        this.description = description != null ? description : new String[]{};
    }

    public void addItem(MPItem item) {
        if(!items.contains(item))items.add(item);
    }

    public void removeItem(MPItem item) {
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

    public List<MPItem> getItems() {
        return items;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String[] getDescription() {
        return description;
    }
}
