package me.ferdithedev.overblock.obitems.turrets;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TurretManager {

    private final List<PlacedTurret> placedTurrets = new ArrayList<>();
    private final List<ArmorStand> armorStands = new ArrayList<>();
    private long ticksRunning = 0;

    public TurretManager() {
        BukkitRunnable ticker = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < placedTurrets.size(); i++) {
                    PlacedTurret turret = placedTurrets.get(i);
                    turret.getTurret().tick(turret);
                    Player player = getNearestPlayer(turret.getLocation());
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
        };
        ticker.runTaskTimer(OverBlock.getInstance(), 0, 1);
    }

    public void addTurret(PlacedTurret turret) {
        placedTurrets.add(turret);
    }

    public void removeTurret(PlacedTurret turret) {
        placedTurrets.remove(turret);
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    private static Player getNearestPlayer(Location location) {
        World world = location.getWorld();
        assert world != null;
        ArrayList<Player> playersInWorld = new ArrayList<>(world.getEntitiesByClass(Player.class));
        if(playersInWorld.size() == 0) return null;
        playersInWorld.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(location)));
        return playersInWorld.get(0);
    }
}
