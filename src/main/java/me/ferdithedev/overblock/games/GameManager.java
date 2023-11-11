package me.ferdithedev.overblock.games;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.games.arena.ArenaManager;
import me.ferdithedev.overblock.games.arena.better.LocalGameMap;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements Listener {

    private final ArenaManager arenaManager;
    private final static ArrayList<Game> activeGames = new ArrayList<>();
    private final LobbyManager lobbyManager;
    private final JavaPlugin plugin;

    public GameManager(OverBlock plugin, World lobby) {
        this.plugin = plugin;
        this.arenaManager = new ArenaManager(plugin);
        lobbyManager = new LobbyManager(plugin, lobby);
    }

    public void createNewGame(List<Player> participants, GameMode mode) {
        gameBroadcast(participants,"§7Preparing world...");
        LocalGameMap arena = arenaManager.getFreeArena();

        if(arena != null && arena.load()) {
            Game newGame = new Game(arena,participants,mode,plugin);
            newGame.start();
            activeGames.add(newGame);
        } else {
            gameBroadcast(participants, "§cNo free arena try again");
            for(Player p : participants) {
                p.getInventory().clear();
                p.teleport(OverBlock.settings.SPAWNLOCATION);
            }
        }
    }

    public Game gameOfPlayer(Player p) {
        if(playerIsIngame(p)) {
            for(Game g : activeGames) {
                if(g.getParticipants().contains(p)) {
                    return g;
                }
            }
        }
        return null;
    }

    public boolean playerIsIngame(Player p) {
        for(Game g : activeGames) {
            if(g.getParticipants().contains(p)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Game> getActiveGames() {
        return activeGames;
    }

    public void removeGame(Game game) {
        activeGames.remove(game);
    }

    public static void gameBroadcast(List<Player> participants, String str) {
        participants.forEach(player -> player.sendMessage(OverBlock.PREFIX + str));
    }

    public enum GameMode {
        NORMAL,
        HARDCORE
    }

    public enum GameState {
        START,
        INGAME,
        ENDING
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
