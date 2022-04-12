package me.ferdithedev.overblock.games.arena;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.fm.Config;
import me.ferdithedev.overblock.games.arena.better.LocalGameMap;
import me.ferdithedev.overblock.util.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArenaManager {

    private final List<LocalGameMap> allArenas = new ArrayList<>();
    private final List<LocalGameMap> inactiveArenas;

    public ArenaManager(JavaPlugin plugin) {
        new Config(plugin,"arenas.yml",true);
        File file = new File(plugin.getDataFolder(), "arenas.yml");
        YamlConfiguration arenaconfig = FileUtil.getConfigOfFile(file);

        List<Arena> arenas = (ArrayList<Arena>) arenaconfig.get("Arenas");

        assert arenas != null;
        for(Arena a : arenas) {
            allArenas.add(new LocalGameMap(OverBlock.gameMapsFolder,a.getWorldName(),false, a));
        }

        List<LocalGameMap> toRemove = new ArrayList<>();

        for(LocalGameMap map : allArenas) {
            if(!map.load()) {
                map.unload();
                toRemove.add(map);
            }
        }

        allArenas.removeAll(toRemove);

        inactiveArenas = allArenas;
    }

    public LocalGameMap getFreeArena() {
        Collections.shuffle(inactiveArenas);
        if(inactiveArenas.size() > 0) {
            LocalGameMap arena = inactiveArenas.get(0);
            inactiveArenas.remove(arena);
            return arena;
        }
        return null;
    }

    public void inactiveArena(LocalGameMap a) {
        a.restoreFromSource();
        inactiveArenas.add(a);
    }

    public List<LocalGameMap> getAllArenas() {
        return allArenas;
    }
}
