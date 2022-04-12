package me.ferdithedev.overblock.games.teams;

import me.ferdithedev.overblock.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {

    private final String name;
    private final ChatColor color;
    private final List<Player> members = new ArrayList<>();
    private final Game game;
    private org.bukkit.scoreboard.Team sTeam;

    public Team(Game g, String name, ChatColor color) {
        this.name = name;
        this.color = color;
        this.game = g;
        this.sTeam = game.getGameScoreboard().getTeam(name);
        if(sTeam == null) {
            sTeam = game.getGameScoreboard().registerNewTeam(name);
            sTeam.setColor(color);
            sTeam.setPrefix(color+"§l"+name.toUpperCase()+" : §r");
        }
    }

    public List<Player> getMembers() {
        return members;
    }

    public void removeMember(Player p) {
        this.members.remove(p);
        sTeam.removeEntry(p.getName());
        p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
    }

    public void addMember(Player p) {
        this.members.add(p);
        sTeam.addEntry(p.getName());
        p.setScoreboard(game.getGameScoreboard());
    }

    public boolean isEmpty() {
        for(Player p : members) {
            if(game.playerAlive(p)) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        for (Player p : members) {
            sTeam.removeEntry(p.getName());
            p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
        }
        members.clear();
        sTeam.unregister();
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
}
