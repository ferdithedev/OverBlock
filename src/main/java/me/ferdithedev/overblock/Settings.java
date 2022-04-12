package me.ferdithedev.overblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class Settings {

    public World LOBBY;
    public int PLAYERSPERTEAM;
    public Location SPAWNLOCATION;
    public Location LOBBYSPAWNLOCATION;
    public int ROUNDTIME;

    public Settings(OverBlock plugin) {
        LOBBY = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("lobby")));
        PLAYERSPERTEAM = plugin.getConfig().getInt("playersperteam");
        SPAWNLOCATION = new Location(Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("Spawnlocation.world"))),
                plugin.getConfig().getDouble("Spawnlocation.x"),
                plugin.getConfig().getDouble("Spawnlocation.y"),
                plugin.getConfig().getDouble("Spawnlocation.z"),
                (float)plugin.getConfig().getDouble("Spawnlocation.yaw"),
                (float)plugin.getConfig().getDouble("Spawnlocation.pitch"));
        LOBBYSPAWNLOCATION = new Location(Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("Lobbyspawnlocation.world"))),
                plugin.getConfig().getDouble("Lobbyspawnlocation.x"),
                plugin.getConfig().getDouble("Lobbyspawnlocation.y"),
                plugin.getConfig().getDouble("Lobbyspawnlocation.z"),
                (float)plugin.getConfig().getDouble("Lobbyspawnlocation.yaw"),
                (float)plugin.getConfig().getDouble("Lobbyspawnlocation.pitch"));
        ROUNDTIME = plugin.getConfig().getInt("roundtime");
    }
}
