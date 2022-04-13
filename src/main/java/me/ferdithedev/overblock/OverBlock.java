package me.ferdithedev.overblock;

import me.ferdithedev.overblock.cmds.LobbyCmd;
import me.ferdithedev.overblock.fm.Config;
import me.ferdithedev.overblock.games.GameListeners;
import me.ferdithedev.overblock.games.GameManager;
import me.ferdithedev.overblock.games.arena.better.LocalGameMap;
import me.ferdithedev.overblock.games.cmds.SpawnBox;
import me.ferdithedev.overblock.games.teams.TeamObject;
import me.ferdithedev.overblock.mpitems.OBItemManager;
import me.ferdithedev.overblock.mpitems.cmds.GetMPItem;
import me.ferdithedev.overblock.games.arena.Arena;
import me.ferdithedev.overblock.games.arena.Spawnpoint;
import me.ferdithedev.overblock.games.cmds.GetRandomItem;
import me.ferdithedev.overblock.games.cmds.Skip;
import me.ferdithedev.overblock.mpitems.cmds.ReloadConfig;
import me.ferdithedev.overblock.mpitems.utils.EnablingUtils;
import me.ferdithedev.overblock.mpitems.utils.UtilCommand;
import me.ferdithedev.overblock.util.BetterTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public final class OverBlock extends JavaPlugin {

    private static OverBlock instance;
    private static GameManager gameManager;
    public static Settings settings;
    public static List<TeamObject> teamObjects;
    private static OBItemManager itemManager;
    public static File gameMapsFolder;

    public static final String PREFIX = ChatColor.DARK_GREEN + "[" + ChatColor.GOLD + "OverBlock" + ChatColor.DARK_GREEN + "]" + ChatColor.RESET + " ";

    static {
        ConfigurationSerialization.registerClass(Arena.class, "Arena");
        ConfigurationSerialization.registerClass(Spawnpoint.class, "Spawnpoint");
        ConfigurationSerialization.registerClass(TeamObject.class, "Team");
    }

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdirs();

        gameMapsFolder = new File(getDataFolder(), "gameMaps");
        if(!gameMapsFolder.exists()) {
            gameMapsFolder.mkdirs();
        }

        new Config(this, "config.yml", true);
        settings = new Settings(this);

        Config teams =  new Config(this,"teams.yml", true);
        teamObjects = (List<TeamObject>) teams.get().get("Teams");

        itemManager = new OBItemManager(this);

        Bukkit.getPluginManager().registerEvents(itemManager, this);
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new GameListeners(), this);
        Bukkit.getPluginManager().registerEvents(new EnablingUtils(),this);

        getCommand("getmpitem").setExecutor(new GetMPItem());
        getCommand("getmpitem").setTabCompleter(new GetMPItem());

        getCommand("skip").setExecutor(new Skip());
        getCommand("getrandomitem").setExecutor(new GetRandomItem());

        getCommand("reloaditems").setExecutor(new ReloadConfig());
        getCommand("items").setExecutor(new UtilCommand());

        getCommand("btp").setExecutor(new BetterTeleport());
        getCommand("btp").setTabCompleter(new BetterTeleport());

        getCommand("spawnbox").setExecutor(new SpawnBox());

        getCommand("lobby").setExecutor(new LobbyCmd());

        if(settings.LOBBY != null) {
            gameManager = new GameManager(this, settings.LOBBY);
        } else {
            printWarn("Can't find lobby world");
        }
    }

    @Override
    public void onDisable() {
        for(LocalGameMap map : getGameManager().getArenaManager().getAllArenas()) {
            OverBlock.print("Deleting active world: " + map.getArena().getName());
            map.unload();
        }
    }

    public static OverBlock getInstance() {
        return instance;
    }

    public static void print(Object str) {
        getInstance().getLogger().log(Level.INFO, str.toString());
    }

    public static void printWarn(String str) {
        getInstance().getLogger().log(Level.WARNING, str);
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static OBItemManager getMPItemManager() {
        return itemManager;
    }

}
