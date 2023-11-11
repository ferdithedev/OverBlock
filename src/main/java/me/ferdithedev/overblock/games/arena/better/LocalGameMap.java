package me.ferdithedev.overblock.games.arena.better;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.games.arena.Arena;
import me.ferdithedev.overblock.games.arena.Cuboid;
import me.ferdithedev.overblock.util.FileUtil;
import org.bukkit.*;

import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap{

    private final File sourceWorldFolder;
    private File activeWorldFolder;
    private final Arena arena;

    private World bukkitWorld;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit, Arena a) {
        this.arena = a;
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if(loadOnInit) load();
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public boolean load() {
        if(isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis()
        );

        try {
            if(!FileUtil.copy(sourceWorldFolder,activeWorldFolder)) return false;
        } catch (IOException ignored) {
            OverBlock.printWarn("Failed to load GameMap!");
            return false;
        }

        WorldCreator c = new WorldCreator(activeWorldFolder.getName()).generator("VoidGen").type(WorldType.FLAT);

        this.bukkitWorld = Bukkit.createWorld(
                c
        );

        if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
        return isLoaded();
    }

    @Override
    public void unload() {
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld,false);
        if(activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }

    public Cuboid getCuboid() {
        Location loc1 = new Location(bukkitWorld,arena.cuboidArenaCords()[0],arena.cuboidArenaCords()[1],arena.cuboidArenaCords()[2] );
        Location loc2 = new Location(bukkitWorld,arena.cuboidArenaCords()[3],arena.cuboidArenaCords()[4],arena.cuboidArenaCords()[5] );
        return new Cuboid(loc1,loc2);
    }
}
