package me.ferdithedev.overblock.games;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.mpitems.MPItem;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class GameListeners implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if(OverBlock.getGameManager().playerIsIngame(e.getPlayer())) {
            if(OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()) == null) return;
            if(OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()).getArena() == null) return;
            if(OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()).getArena().getWorld() == null) return;
            if(OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()).getArena().getWorld().getName().equalsIgnoreCase(Objects.requireNonNull(e.getFrom().getWorld()).getName())) {
                if(!e.getFrom().getWorld().equals(Objects.requireNonNull(e.getTo()).getWorld())) {
                    quit(e.getPlayer(), OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(OverBlock.getGameManager().playerIsIngame(e.getPlayer())) {
            quit(e.getPlayer(), OverBlock.getGameManager().gamePlayerIsIn(e.getPlayer()));
        }
    }

    @EventHandler
    public void damge(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            if(e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                Player p = (Player)e.getEntity();
                if(OverBlock.getGameManager().playerIsIngame(p)) {
                    if(OverBlock.getGameManager().gamePlayerIsIn(p).isHardcore()) {
                        e.setDamage(e.getFinalDamage()*2);
                    }
                    if(p.getHealth() - e.getFinalDamage() <= 0) {
                        Game g = OverBlock.getGameManager().gamePlayerIsIn(p);
                        e.setCancelled(true);
                        if(e.getDamager() instanceof Player) {
                            g.death(p, (Player) e.getDamager());
                        } else {
                            g.death(p);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(OverBlock.getGameManager().playerIsIngame(p)) {
                if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                        OverBlock.getGameManager().gamePlayerIsIn(p).death(p);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private void quit(Player p, Game g) {
        if(g.getState() != GameManager.GameState.ENDING) {
            GameManager.gameBroadcast(g.getParticipants(),g.coloredName(p)+" §eleft. §r" + g.teamOfPlayer(p).getColor() + g.teamOfPlayer(p).getName() + " §ehas now §c"+(g.teamOfPlayer(p).getMembers().size()-1)+" §eplayer(s)");
            g.removePlayer(p);
        }
    }

    //items

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();

                ItemStack eventItem = e.getItem().getItemStack();

                NamespacedKey boxkey = new NamespacedKey(OverBlock.getInstance(), "boxtype");
                ItemMeta itemMeta = eventItem.getItemMeta();
                if (itemMeta != null) {
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                    if(container.has(boxkey, PersistentDataType.INTEGER)) {
                        int boxtype = container.get(boxkey,PersistentDataType.INTEGER);
                        MPItem item = null;
                        for(int i = 0; i < eventItem.getAmount(); i++) {
                            switch (boxtype) {
                                case 1: {
                                    item = ItemSpawner.getItem(0);
                                    break;
                                }
                                case 2: {
                                    item = ItemSpawner.getItem(25);
                                    break;
                                }
                                case 3: {
                                    item = ItemSpawner.getItem(100);
                                    break;
                                }
                            }
                            if(item==null) {
                                e.setCancelled(true);
                                return;
                            }
                            p.getInventory().addItem(item.getItemStack());
                        }

                        p.playSound(e.getEntity().getLocation(), Sound.ENTITY_ITEM_PICKUP,1f, 1.5f);
                        p.spawnParticle(Particle.END_ROD,p.getLocation(),20,0.01f,0.01f,0.01f, 0.1f);
                        e.setCancelled(true);
                        e.getItem().remove();
                    }
                }

        }
    }
}
