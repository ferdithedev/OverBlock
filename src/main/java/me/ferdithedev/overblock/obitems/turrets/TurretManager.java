package me.ferdithedev.overblock.obitems.turrets;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TurretManager {

    private static final List<PlacedTurret> placedTurrets = new ArrayList<>();
    private static final List<ArmorStand> armorStands = new ArrayList<>();
    private static long ticksRunning = 0;
    private static boolean running = false;
    private static BukkitTask task;

    public static void addTurret(PlacedTurret turret) {
        if(!running) {
            startTicker();
            running = true;
        }
        placedTurrets.add(turret);
    }

    public static void removeTurret(PlacedTurret turret) {
        placedTurrets.remove(turret);
    }

    public static List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    private static Player getNearestPlayer(PlacedTurret turret, Location location) {
        World world = location.getWorld();
        assert world != null;
        ArrayList<Player> playersInWorld = new ArrayList<>(world.getEntitiesByClass(Player.class));
        playersInWorld.remove(turret.getPlayer());
        if(playersInWorld.size() == 0) return null;
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.get(0);
    }

    private static void startTicker() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < placedTurrets.size(); i++) {
                    PlacedTurret turret = placedTurrets.get(i);
                    turret.getTurret().tick(turret);
                    Player player = getNearestPlayer(turret,turret.getLocation());
                    //if(!player.equals(turret.getPlayer())) {
                    if (player != null) {

                        if (ticksRunning - turret.getLastTimeShooted() >= turret.getShootCooldown()) {
                            if (turret.getLocation().distance(player.getLocation()) < turret.getMaxDistance()) {
                                turret.getTurret().shoot(turret.getPlayer(), turret.getLocation(), player);
                                turret.setLastTimeShooted(ticksRunning);
                            }
                        }
                    }
                    //}
                }
                ticksRunning++;
            }
        }.runTaskTimer(OverBlock.getInstance(), 0, 1);
    }
    public static void stopTicker() {
        if(running) task.cancel();
    }
}
