package me.ferdithedev.overblock.obitems.turrets.impl;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.obitems.OBItemRarity;
import me.ferdithedev.overblock.obitems.turrets.PlacedTurret;
import me.ferdithedev.overblock.obitems.turrets.Turret;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RapidTurret extends Turret {

    public RapidTurret(JavaPlugin plugin) {
        super(plugin, Material.IRON_HOE, "Rapido", 200, OBItemRarity.SPECIAL, "§7A fast and strong turret");
    }

    @Override
    public void place(Player player, Block clickedBlock) {
        Location spawn = clickedBlock.getLocation().add(0.5,1,0.5);
        String name = player.getName().substring(player.getName().length()-1).equalsIgnoreCase("s") ? player.getName() + "'" : player.getName() + "'s";
        name = name + " §7§lRapido";
        ItemStack display = new ItemStack(Material.COAL_BLOCK);
        new PlacedTurret(this, player, spawn, name, display, 30, 12, 2);
    }

    @Override
    public void shoot(Player attacker, Location turretLoc, Player target) {
        assert turretLoc.getWorld() != null;
        //spawn "rocket"
        ArmorStand armorStand = (ArmorStand) turretLoc.getWorld().spawnEntity(turretLoc, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        OverBlock.getOBItemManager().getTurretManager().getArmorStands().add(armorStand);
        //play sound
        turretLoc.getWorld().getPlayers().stream().filter(p->turretLoc.distance(p.getLocation())<12).forEach(p->{
            turretLoc.getWorld().playSound(p, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST,1,2f);
            turretLoc.getWorld().playSound(p, Sound.BLOCK_STONE_HIT,1,2f);
            turretLoc.getWorld().playSound(p, Sound.ENTITY_BLAZE_HURT,1,2f);
        });
        //start runnables (repeating for following the player and one to kill the armor stand when not finding the target)
        Location targetLoc = target.getLocation();
        BukkitRunnable w = new BukkitRunnable() {
            @Override
            public void run() {
                //moving armorstand towards player
                Vector direction = targetLoc.toVector().subtract(armorStand.getLocation().toVector());
                direction.normalize();
                armorStand.setVelocity(direction.multiply(0.5));
                //spawn particle trail
                armorStand.getWorld().spawnParticle(Particle.SMOKE_NORMAL,armorStand.getLocation().add(0,1,0),10,0,0,0,0);
                //hit execution
                if(armorStand.getLocation().distance(target.getLocation()) < 1) {
                    target.damage(3,attacker);
                    OverBlock.getOBItemManager().getTurretManager().getArmorStands().remove(armorStand);
                    armorStand.remove();
                    cancel();
                }
            }
        };
        w.runTaskTimer(OverBlock.getInstance(),0,1);
        Bukkit.getScheduler().runTaskLater(OverBlock.getInstance(), () -> {
            w.cancel();
            armorStand.remove();
        }, 40);
    }

    double multiplier = 0;
    double r = 0.6;

    @Override
    public void tick(PlacedTurret turret) {
        Location loc = turret.getArmorStand().getLocation();
        loc.setYaw(loc.getYaw()+5f);
        turret.getArmorStand().teleport(loc);

        double sin = r * Math.sin(multiplier);
        assert loc.getWorld() != null;
        //loc.getWorld().spawnParticle(Particle.REDSTONE,particleLocation.clone().add(cos,sin+0.75,sin),5,new Particle.DustOptions(Color.fromRGB(209, 255, 253),1f));
        loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL,turret.getLocation().clone().add(r * Math.cos(multiplier),sin+1,sin),1,0,0,0,0);
        loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL,turret.getLocation().clone().add(r * Math.cos(multiplier + Math.PI),sin+1,r * Math.sin(multiplier + Math.PI)),1,0,0,0,0);
        //loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,turret.getLocation().clone().add(-r * Math.cos(multiplier),sin+1,sin),1,0,0,0,0);
        //loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK,turret.getLocation().clone().add(-r * Math.cos(multiplier + Math.PI),sin+1,r * Math.sin(multiplier + Math.PI)),1,0,0,0,0);
        multiplier += Math.PI/16;
    }

}
