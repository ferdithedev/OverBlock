package me.ferdithedev.overblock;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        e.getPlayer().getInventory().clear();
        e.getPlayer().teleport(OverBlock.settings.SPAWNLOCATION);
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent e) {
        e.setSpawnLocation(OverBlock.settings.SPAWNLOCATION);
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(20);
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }
    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if(e.getEntity().getWorld().equals(OverBlock.settings.SPAWNLOCATION.getWorld())) {
            e.getEntity().setFoodLevel(20);
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        /*if(e.getEntity().getWorld().equals(Objects.requireNonNull(OverBlock.settings.SPAWNLOCATION.getWorld()))) {
            e.setCancelled(true);
        }*/
    }
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(!Objects.equals(Objects.requireNonNull(e.getTo()).getWorld(), OverBlock.settings.SPAWNLOCATION.getWorld())) return;
        if(!Objects.equals(e.getTo().getWorld(), e.getFrom().getWorld())) {
            e.setTo(OverBlock.settings.SPAWNLOCATION);
        }
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(OverBlock.settings.SPAWNLOCATION);
    }
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(20);
    }
}
