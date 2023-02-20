package me.ferdithedev.overblock.obitems.turrets;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
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
                    assert turret.getLocation().getWorld() != null;
                    List<Player> players = turret.getLocation().getWorld().getPlayers();
                    for (Player player : players) {
                        if(ticksRunning - turret.getLastTimeShooted() >= turret.getShootCooldown()) {
                            //if(!player.equals(turret.getPlayer())) {
                                if (turret.getLocation().distance(player.getLocation()) < turret.getMaxDistance()) {
                                    turret.getTurret().shoot(turret.getPlayer(), turret.getLocation(), player);
                                    turret.setLastTimeShooted(ticksRunning);
                                    break;
                                }
                            //}
                        }
                    }
                }
                ticksRunning++;
            }
        };
        ticker.runTaskTimer(OverBlock.getInstance(),0,1);
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
}
