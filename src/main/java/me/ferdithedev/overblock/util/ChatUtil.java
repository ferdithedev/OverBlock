package me.ferdithedev.overblock.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatUtil {

    private static List<ChatColor> colors;

    public static String rainbow(String text) {
        if(colors == null) colors = new ArrayList<>(Arrays.asList(ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE));
        int i = 0;
        String[] chars = text.split("");
        List<String> stringAsList = new ArrayList<>();
        for(String s : chars) {
            stringAsList.add(colors.get(i)+ChatColor.BOLD.toString()+s);
            i++;
            if(i >= colors.size()) {
                i = 0;
            }
        }
        StringBuilder builder = new StringBuilder();
        for(String s : stringAsList) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static List<String> lore(String... strings) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, strings);
        return list;
    }
}
