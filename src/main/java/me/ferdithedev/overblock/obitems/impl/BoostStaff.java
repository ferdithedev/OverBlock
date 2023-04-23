package me.ferdithedev.overblock.obitems.impl;

import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.obitems.OBItemRarity;
import me.ferdithedev.overblock.obitems.OBItemType;
import me.ferdithedev.overblock.util.Effects;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BoostStaff extends OBItem {

    public BoostStaff(JavaPlugin plugin) {
        super(plugin, Material.STICK, "Boost Staff", 40, OBItemType.WEAPON, OBItemRarity.COMMON, "§7Rush to your opponents (or run away)");
    }

    @Override
    public boolean function(Player player) {
        Vector v = player.getLocation().getDirection().multiply(2);
        player.setVelocity(v);
        Effects.playSoundDistance(player.getLocation(),5f,Sound.ENTITY_ENDERMAN_TELEPORT,1,1);
        return true;
    }

}
