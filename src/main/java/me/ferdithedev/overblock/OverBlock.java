package me.ferdithedev.overblock;

import me.ferdithedev.overblock.cmds.JoinCmd;
import me.ferdithedev.overblock.cmds.LobbyCmd;
import me.ferdithedev.overblock.fm.Config;
import me.ferdithedev.overblock.games.GameListeners;
import me.ferdithedev.overblock.games.GameManager;
import me.ferdithedev.overblock.games.LobbyManager;
import me.ferdithedev.overblock.games.arena.better.LocalGameMap;
import me.ferdithedev.overblock.games.cmds.SpawnBox;
import me.ferdithedev.overblock.games.teams.TeamObject;
import me.ferdithedev.overblock.obitems.ItemManager;
import me.ferdithedev.overblock.obitems.cmds.GetOBItem;
import me.ferdithedev.overblock.games.arena.Arena;
import me.ferdithedev.overblock.games.arena.Spawnpoint;
import me.ferdithedev.overblock.games.cmds.GetRandomItem;
import me.ferdithedev.overblock.games.cmds.Skip;
import me.ferdithedev.overblock.obitems.cmds.ReloadConfig;
import me.ferdithedev.overblock.obitems.packagebrowser.BrowserCommand;
import me.ferdithedev.overblock.obitems.manage.ItemsCommand;
import me.ferdithedev.overblock.obitems.turrets.TurretManager;
import me.ferdithedev.overblock.util.BetterTeleport;
import me.ferdithedev.overblock.util.invs.ListInventoryManager;
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
    private static ItemManager itemManager;
    public static File gameMapsFolder;
    public static ListInventoryManager listInventoryManager;

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

        itemManager = new ItemManager(this);

        Bukkit.getPluginManager().registerEvents(itemManager, this);
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new GameListeners(), this);
        this.listInventoryManager = new ListInventoryManager(itemManager);
        Bukkit.getPluginManager().registerEvents(listInventoryManager,this);

        getCommand("getobitem").setExecutor(new GetOBItem());
        getCommand("getobitem").setTabCompleter(new GetOBItem());

        getCommand("skip").setExecutor(new Skip());
        getCommand("getrandomitem").setExecutor(new GetRandomItem());

        getCommand("reloaditems").setExecutor(new ReloadConfig());
        getCommand("items").setExecutor(new ItemsCommand());

        getCommand("btp").setExecutor(new BetterTeleport());
        getCommand("btp").setTabCompleter(new BetterTeleport());

        getCommand("spawnbox").setExecutor(new SpawnBox());

        getCommand("lobby").setExecutor(new LobbyCmd());

        getCommand("join").setExecutor(new JoinCmd());

        getCommand("browser").setExecutor(new BrowserCommand());

        if(settings.LOBBY != null) {
            gameManager = new GameManager(this, settings.LOBBY);
        } else {
            printWarn("Can't find lobby world");
        }
    }

    @Override
    public void onDisable() {
        //stop runnables
        TurretManager.stopTicker();

        for(LocalGameMap map : getGameManager().getArenaManager().getAllArenas()) {
            OverBlock.print("Deleting active world: " + map.getWorld().getName());
            map.unload();
        }
        TurretManager.getArmorStands().forEach(a -> a.remove());
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

    public static ItemManager getItemManager() {
        return itemManager;
    }

    public static ListInventoryManager getListInventoryManager() {
        return listInventoryManager;
    }
}
