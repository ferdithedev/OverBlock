package me.ferdithedev.overblock.obitems.turrets;

import me.ferdithedev.overblock.obitems.OBItem;
import me.ferdithedev.overblock.obitems.OBItemRarity;
import me.ferdithedev.overblock.obitems.OBItemType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Turret extends OBItem {
    public Turret(JavaPlugin plugin, Material material, String name, long cooldown, OBItemRarity rarity, String... lore) {
        super(plugin, material, name, cooldown, OBItemType.TURRET, rarity, lore);
    }

    @Override
    public boolean function(Player player) {
        return true;
    }

    public abstract void place(Player player, Block clickedBlock);

    public abstract void shoot(Player attacker, Location turretLoc, Player victim);

    public abstract void tick(PlacedTurret turret);

    @Override
    public boolean click(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getBlockFace().equals(BlockFace.UP)) {
                e.setCancelled(true);
                place(e.getPlayer(),e.getClickedBlock());
                return true;
            }
        }
        return false;
    }
}
