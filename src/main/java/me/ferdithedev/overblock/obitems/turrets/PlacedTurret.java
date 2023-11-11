package me.ferdithedev.overblock.obitems.turrets;

import me.ferdithedev.overblock.util.Effects;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlacedTurret {

    private final Location location;
    private final double maxDistance;
    private final Turret turret;
    private final Player player;
    private long lastTimeShooted = 0;
    private final int shootCooldown;
    private int timesShooted = 0;
    private final int maxShots;
    private final ArmorStand armorStand;

    //turret player location name displayBlock maxShots maxDistance shootCooldown
    public PlacedTurret(Turret turret, Player player, Location location, String name, ItemStack display, int maxShots, double maxDistance, int shootCooldown) {
        this.location = location;
        this.maxDistance = maxDistance;
        this.turret = turret;
        this.player = player;
        this.shootCooldown = shootCooldown;
        this.maxShots = maxShots;
        assert location.getWorld() != null;
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        assert armorStand.getEquipment() != null;
        armorStand.getEquipment().setHelmet(display);
        TurretManager.getArmorStands().add(armorStand);
        TurretManager.addTurret(this);
    }

    public Location getLocation() {
        return location;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public Turret getTurret() {
        return turret;
    }

    public Player getPlayer() {
        return player;
    }

    public int getShootCooldown() {
        return shootCooldown;
    }

    public long getLastTimeShooted() {
        return lastTimeShooted;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setLastTimeShooted(long lastTimeShooted) {
        this.lastTimeShooted = lastTimeShooted;
        timesShooted++;
        armorStand.teleport(armorStand.getLocation().subtract(0, 0.6 / maxShots, 0));
        if (timesShooted >= maxShots) {
            assert location.getWorld() != null;
            Effects.playSoundDistance(location,maxDistance,Sound.ENTITY_ITEM_BREAK,1,1);
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 200, 0, 0, 0, 0.1);
            player.sendMessage("§c§lYour " + turret.getName() + " fired it's last shot!");
            TurretManager.removeTurret(this);
            TurretManager.getArmorStands().remove(armorStand);
            armorStand.remove();
        }
    }
}
