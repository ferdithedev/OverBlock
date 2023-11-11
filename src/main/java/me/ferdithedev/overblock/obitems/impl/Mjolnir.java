package me.ferdithedev.overblock.obitems.impl;

import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.obitems.OBItemRarity;
import me.ferdithedev.overblock.obitems.OBItemType;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;

public class Mjolnir extends OBItem {
    public Mjolnir(JavaPlugin plugin) {
        super(plugin, Material.IRON_INGOT, "Mjolnir", 10, OBItemType.WEAPON, OBItemRarity.EPIC, "Made by the dwarfes Sindri and Brokk", "Weapon of the god of thunder, Thor");
    }

    @Override
    public boolean function(Player player) {
        Block block = blockLookingAt(player);
        if(block == null) return false;
        if(block.isLiquid()) return false;
        if(!block.getType().isSolid()) return false;
        if(block.getLocation().getY() == player.getWorld().getHighestBlockAt(block.getLocation()).getY()) {
            Location loc = block.getLocation().add(0,1,0);
            player.getWorld().strikeLightningEffect(loc);
            for(Entity entity : player.getWorld().getNearbyEntities(block.getLocation(),4,4,4, e->e instanceof Player)) {
                if(entity instanceof Player) {
                    if(!entity.equals(player)) {
                        ((Player) entity).damage(10f,player);
                        entity.setFireTicks(60);
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean click(PlayerInteractEvent e) {
        e.setCancelled(true);
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            return function(e.getPlayer());
        }
        return false;
    }

    private Block blockLookingAt(Player viewer) {
        RayTraceResult result = viewer.getWorld().rayTraceBlocks(viewer.getEyeLocation(),viewer.getEyeLocation().getDirection(),25, FluidCollisionMode.ALWAYS);
        if(result != null) {
            if(result.getHitBlock() != null) {
                return result.getHitBlock();
            }
        }
        return null;
    }
}
