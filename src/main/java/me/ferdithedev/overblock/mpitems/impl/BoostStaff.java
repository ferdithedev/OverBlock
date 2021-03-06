package me.ferdithedev.overblock.mpitems.impl;

import me.ferdithedev.overblock.mpitems.OBItem;
import me.ferdithedev.overblock.mpitems.OBItemManager;
import me.ferdithedev.overblock.mpitems.OBItemRarity;
import me.ferdithedev.overblock.mpitems.OBItemType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BoostStaff extends OBItem {

    public BoostStaff(JavaPlugin plugin) {
        super(plugin, Material.STICK, "Boost Staff", 40, OBItemType.WEAPON, OBItemRarity.COMMON, "§7Rush to your opponents (or run away)");
    }

    @Override
    public void function(Player player) {
        Vector v = player.getLocation().getDirection().multiply(2);
        player.setVelocity(v);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    @Override
    public void click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            if(this.noCooldown(e.getPlayer())) {
                function(e.getPlayer());
            } else {
                OBItemManager.cooldownMessage(e.getPlayer());
            }
        }
    }
}
