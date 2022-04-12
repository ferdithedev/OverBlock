package me.ferdithedev.overblock.mpitems.impl;


import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.mpitems.MPItem;
import me.ferdithedev.overblock.mpitems.MPItemManager;
import me.ferdithedev.overblock.mpitems.MPItemRarity;
import me.ferdithedev.overblock.mpitems.MPItemType;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Flamethrower extends MPItem {

    public Flamethrower(JavaPlugin plugin) {
        super(plugin, Material.IRON_AXE, "Flamethrower", 80, MPItemType.WEAPON, MPItemRarity.ULTIMATE, "§7Burn your opponents to §4DEATH");
    }

    @Override
    public void function(Player player) {
        int taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(OverBlock.getInstance(), () -> {
            if(player.getInventory().getItemInMainHand().isSimilar(getItemStack())) {
                int distance = 10;
                Location origin = player.getEyeLocation();
                Vector direction = origin.getDirection();
                direction.multiply(distance);
                direction.normalize();

                Objects.requireNonNull(origin.getWorld()).playSound(origin, Sound.ITEM_FIRECHARGE_USE, 1, 0);
                for (int i = 0; i < distance; i++) {
                    Location loc = origin.add(direction);
                    if(loc.getBlock().getType() != Material.AIR) {
                        break;
                    }
                    int multiply = i*2;
                    Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.FLAME, loc, 5*multiply, 0.05d*multiply, 0.05d*multiply, 0.05d*multiply, 0.01d);
                    Location loc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    if(loc1.subtract(0, 1, 0).getBlock().getType().equals(Material.AIR) && loc1.subtract(0, 1, 0).getBlock().getType().isSolid() ) {
                        loc1.add(0, 1, 0);
                        if(player.getLocation().distance(origin) > 3) {
                            placeTempFire(loc1, 50 + (int)(Math.random() * ((70 - 50) + 1)));
                        }
                    }
                    Location loc2 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    if(loc2.getBlock().getType().equals(Material.AIR) && loc2.subtract(0,1,0).getBlock().getType().isSolid()) {
                        loc2.add(0,1,0);
                        if(player.getLocation().distance(origin) > 3) {
                            placeTempFire(loc2, 50 + (int)(Math.random() * ((70 - 50) + 1)));
                        }
                    }

                    loc.getWorld().getNearbyEntities(loc, 1,1,1).forEach(entity -> {
                        if(!entity.equals(player)) {
                            setVisualFireForTicks(entity, 20);
                            if(entity instanceof LivingEntity e) {
                                e.damage(2d, player);
                            }
                        }
                    });
                }
            }
        }, 0, 10);
        Bukkit.getScheduler().scheduleSyncDelayedTask(OverBlock.getInstance(), () -> Bukkit.getScheduler().cancelTask(taskid), 40);
    }

    @Override
    public void click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(this.noCooldown(e.getPlayer())) {
                this.function(e.getPlayer());
            } else {
                MPItemManager.cooldownMessage(e.getPlayer());
            }
        }
        e.setCancelled(true);
    }

    public void placeTempFire(Location l, long t) {
        int random = (int) (Math.random() * ((10) + 1));
        if(random > 3) {
            l.getBlock().setType(Material.FIRE);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(OverBlock.getInstance(), () -> {
                if(l.getBlock().getType().equals(Material.FIRE)) {
                    l.getBlock().setType(Material.AIR);
                }
            }, t);
        }
    }

    public void setVisualFireForTicks(Entity entity, int ticks) {
        entity.setVisualFire(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(OverBlock.getInstance(), () -> entity.setVisualFire(false), ticks);
    }
}
