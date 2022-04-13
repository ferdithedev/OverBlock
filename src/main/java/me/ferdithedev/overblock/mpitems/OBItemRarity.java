package me.ferdithedev.overblock.mpitems;

import me.ferdithedev.overblock.util.ChatUtil;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public enum OBItemRarity {

    COMMON(ChatColor.GRAY.toString(), "", 50, ChatColor.GRAY + "" + ChatColor.BOLD + "COMMON"),
    UNIQUE(ChatColor.YELLOW.toString(), "", 30, ChatColor.YELLOW + "" + ChatColor.BOLD + "UNIQUE"),
    EPIC(ChatColor.LIGHT_PURPLE.toString(), "", 15, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "EPIC"),
    ULTIMATE(ChatColor.DARK_RED.toString(), ChatColor.DARK_RED.toString(), 4, ULTIMATES()),
    SPECIAL("", "", 1, ChatUtil.rainbow("SPECIAL"));

    private final String prefix;
    private final String suffix;
    private final double chance;
    private final String name;

    OBItemRarity(String prefix, String suffix, double chance, String name) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.chance = chance;
        this.name = name;
    }

    public double getChance() {
        return chance;
    }

    public String format(String string) {
        return prefix + "§l" + string + suffix;
    }

    public static String RAINBOW(String string) {
        return ChatUtil.rainbow(string);
    }

    public List<String> lore(String[] list) {
        List<String> lore = new ArrayList<>();
        Collections.addAll(lore, list);
        lore.add("");
        lore.add(name);
        return lore;
    }

    private static String ULTIMATES() {
        return MAGICLETTER() + ChatColor.DARK_RED + ChatColor.BOLD + "ULTIMATE" + MAGICLETTER();
    }

    private static String MAGICLETTER() {
        return ChatColor.DARK_RED + ChatColor.BOLD.toString() + ChatColor.MAGIC + "M" + ChatColor.RESET;
    }

}
