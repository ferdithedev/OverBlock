package me.ferdithedev.overblock.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Messages {

    public static ArrayList<String> GAME_OVER = new ArrayList<>(Arrays.asList("Oh no, what happened here? D:", "Probably a hacker... no one can beat you! \\(^_^)/", "Just a server lag! (*_*)", "Mayby you should buy a RGB setup! ;-)", "Just bad teammates! (>_<)"));
    public static ArrayList<String> FINAL_DEATH = new ArrayList<>(Arrays.asList("%player%§7 is now dead forever.", "%player%§7 died the last time.", "%player%§7 died. (yes, like a rl death)"));
    public static ArrayList<String> FINAL_DEATH_KILLER = new ArrayList<>(Arrays.asList("%player%§7 is now dead forever. (it's %killer%§7's fault)", "%killer%§7 killed %player%§7 the last time."));
    public static ArrayList<String> DEATH = new ArrayList<>(Arrays.asList("%player%§7 died.", "%player%§7 wasn't focused and died.", "§7Tactical death by %player%"));
    public static ArrayList<String> DEATH_KILLER = new ArrayList<>(Arrays.asList("%killer%§7 pushed %player%§7 to death.", "%killer%§7 hit %player%§7 one time to much."));

    public static String getRandomMsg(ArrayList<String> list) {
        int r = new Random().nextInt(list.size());
        return list.get(r);
    }
}
