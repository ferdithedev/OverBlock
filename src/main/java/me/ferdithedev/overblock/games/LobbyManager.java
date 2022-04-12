package me.ferdithedev.overblock.games;


import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.util.ChatUtil;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class LobbyManager implements Listener {

    private World lobby;
    private BukkitRunnable countdown;
    private OverBlock plugin;
    private int time = 0;
    private ItemStack iquit;
    private ItemStack ivote;
    private Inventory votingInv = Bukkit.createInventory(null,9, "§6Voting");
    private BossBar votingBar;
    private HashMap<Player, GameManager.GameMode> votes = new HashMap<>();

    public LobbyManager(OverBlock plugin, World lobby) {
        this.lobby = lobby;
        this.plugin = plugin;
        lobby.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        lobby.setClearWeatherDuration(1);
        lobby.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        lobby.setTime(1000);
        iquit = iquit();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        votingBar = Bukkit.createBossBar("§eNormal",BarColor.YELLOW,BarStyle.SOLID);
        votingBar.setVisible(true);
        votingBar.setProgress(1);
        ivote = ivote();
    }

    @EventHandler
    public void onJoinLobby(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        if(e.getTo().getWorld().equals(lobby)) {
            if(lobby.getPlayers().size() >= OverBlock.settings.PLAYERSPERTEAM*4 && !e.getFrom().getWorld().equals(lobby)) {
                e.getPlayer().sendMessage(OverBlock.PREFIX + "§cThe lobby is currently full. Please try again in a few seconds");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1 , 0.8f);
                e.setCancelled(true);
            } else if(!e.getFrom().getWorld().equals(lobby)) {
                    e.getPlayer().getInventory().clear();
                    e.getPlayer().getInventory().setItem(8, iquit);
                    e.getPlayer().getInventory().setItem(0,ivote);
                    e.getPlayer().setHealth(20);
                    e.getPlayer().setFoodLevel(20);
                    votes.put(e.getPlayer(), GameManager.GameMode.NORMAL);
                    votingBar.addPlayer(e.getPlayer());
                    updateBossbar();
                    e.setTo(OverBlock.settings.LOBBYSPAWNLOCATION);
                    playerUpdate(player, null);
            }
        } else if(e.getFrom().getWorld().equals(lobby) && !e.getTo().getWorld().equals(lobby)) {
            playerUpdate(null, player);
            votingBar.removePlayer(e.getPlayer());
        }
    }

    public void playerUpdate(Player join, Player leave) {
        List<Player> players = lobby.getPlayers();
        if(players.contains(leave)) {
            if(leave != null) {
                players.remove(leave);
            }
        }
        if(!players.contains(join)) {
            if(join != null) {
                players.add(join);
                GameManager.gameBroadcast(players, ChatColor.DARK_AQUA + join.getName() + "§e joined the lobby (§3"+players.size()+"§e/§3"+ OverBlock.settings.PLAYERSPERTEAM*4+"§e)");
            }
        }
        int seconds = getSecondsByCount(players.size());
        if(seconds > 0) {
            setCountdown(seconds);
            GameManager.gameBroadcast(players, "§eGame starting in §3" + seconds + "§e seconds");
        } else {
            if(countdown != null) {
                countdown.cancel();
            }
        }
    }

    private int getSecondsByCount(int count) {
        if(count < 2) {
            return -1;
        }
        if(count < OverBlock.settings.PLAYERSPERTEAM*2) {
            return 60;
        }
        if(count < OverBlock.settings.PLAYERSPERTEAM*4) {
            return 30;
        }
        if(count == OverBlock.settings.PLAYERSPERTEAM*4) {
            return 10;
        }
        return -1;
    }

    public void setCountdown(int t) {
        this.time = t;
        if(countdown != null) {
            countdown.cancel();
        }
        countdown = new BukkitRunnable() {
            @Override
            public void run() {
                if(time == 30 || time == 10 || time <= 5 && time > 1) {
                    GameManager.gameBroadcast(lobby.getPlayers(), "§eGame starts in§3 "+time+" §eseconds");
                }

                if(time == 1) {
                    GameManager.gameBroadcast(lobby.getPlayers(), "§eGame starts in§3 1 §esecond");
                }

                if(time == 0) {
                    if(lobby.getPlayers().size() < 2) {
                        GameManager.gameBroadcast(lobby.getPlayers(),"§cStart delayed! There are not enough players in the lobby");
                    } else {
                        GameManager.gameBroadcast(lobby.getPlayers(), "§eGame starting!");
                        createGame();
                    }
                    cancel();
                    return;
                }
                time--;
            }
        };
        countdown.runTaskTimer(plugin, 0, 20);
    }

    private ItemStack iquit() {
        ItemStack i = new ItemStack(Material.OAK_DOOR);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§cClick to leave lobby");
        i.setItemMeta(m);
        return i;
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if(e.getEntity().getWorld().equals(lobby)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity().getWorld().equals(lobby)) {
            if(e.getCause() != EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(e.getPlayer().getWorld().equals(lobby)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().isSimilar(iquit)) {
            e.setCancelled(true);
            e.getWhoClicked().teleport(OverBlock.settings.SPAWNLOCATION);
            e.getWhoClicked().getInventory().clear();
            GameManager.gameBroadcast(lobby.getPlayers(), "§3"+e.getWhoClicked().getName()+"§e has quit");
        }
        if(e.getCurrentItem().isSimilar(ivote)) {
            openVoting((Player) e.getWhoClicked());
            e.setCancelled(true);
        }
        if(e.getCurrentItem().isSimilar(normal(false))) {
            votes.put((Player) e.getWhoClicked(), GameManager.GameMode.NORMAL);
            e.getWhoClicked().sendMessage("§eYou choosed §lNormal §r§emode!");
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK,1,1);
            updateBossbar();
            e.getWhoClicked().closeInventory();
        }
        if(e.getCurrentItem().isSimilar(hardcore(false))) {
            votes.put((Player) e.getWhoClicked(), GameManager.GameMode.HARDCORE);
            e.getWhoClicked().sendMessage("§eYou choosed §c§lHARDCODE §r§emode!");
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.UI_BUTTON_CLICK,1,1);
            updateBossbar();
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onLeftLobby(PlayerQuitEvent e) {
        if(e.getPlayer().getWorld().equals(lobby)) {
            GameManager.gameBroadcast(lobby.getPlayers(), ChatColor.DARK_AQUA + e.getPlayer().getName() + "§e has quit!");
            votingBar.removePlayer(e.getPlayer());
            playerUpdate(null, e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getItem() == null) return;
        if(e.getItem().isSimilar(iquit)) {
            e.getPlayer().teleport(OverBlock.settings.SPAWNLOCATION);
            e.getPlayer().getInventory().clear();
            GameManager.gameBroadcast(lobby.getPlayers(), "§3"+e.getPlayer().getName()+"§e quit");
        }
        if(e.getItem().isSimilar(ivote)) {
            openVoting(e.getPlayer());
        }
    }

    private ItemStack ivote() {
        ItemStack i = new ItemStack(Material.NETHER_STAR);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§6Vote");
        i.setItemMeta(m);
        return i;
    }

    private ItemStack hardcore(boolean selected) {
        ItemStack i = new ItemStack(Material.RED_WOOL);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§cHardcore");
        m.setLore(ChatUtil.lore("§7You have only §c§lONE LIFE§r§7!", "§7You receive §c§lDOUBLED DAMAGE§r§7 from all sources!", "§4§lITS THE ULTIMATE HARDCORE EXPERIENCE"));
        if(selected) {
            m.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        i.setItemMeta(m);
        return i;
    }

    private ItemStack normal(boolean selected) {
        ItemStack i = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName("§eNormal");
        m.setLore(ChatUtil.lore("§7You have§3 3§7 lifes!", "§7You receive §enormal§7 damage", "§7You §adon't§7 receive §efire§7damage and §efall§7damage"));
        if(selected) {
            m.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        i.setItemMeta(m);
        return i;
    }

    public void createGame() {
        int h = 0;
        int n = 0;
        for(GameManager.GameMode m : votes.values()) {
            if(m == GameManager.GameMode.HARDCORE) {
                h++;
            }
            if(m == GameManager.GameMode.NORMAL) {
                n++;
            }
        }
        GameManager.GameMode mode = (h > n) ? GameManager.GameMode.HARDCORE : GameManager.GameMode.NORMAL;
        plugin.getGameManager().createNewGame(lobby.getPlayers(), mode);
    }

    private void updateBossbar() {
        int h = 0;
        int n = 0;
        for(GameManager.GameMode m : votes.values()) {
            if(m == GameManager.GameMode.HARDCORE) {
                h++;
            }
            if(m == GameManager.GameMode.NORMAL) {
                n++;
            }
        }
        if(h > n) {
            votingBar.setColor(BarColor.RED);
            votingBar.setTitle("§c§lHARDCORE");
            votingBar.addFlag(BarFlag.DARKEN_SKY);
            lobby.setTime(18000);
        } else {
            votingBar.setColor(BarColor.YELLOW);
            votingBar.setTitle("§eNormal");
            votingBar.removeFlag(BarFlag.DARKEN_SKY);
            lobby.setTime(1000);
        }
    }

    private void openVoting(Player p) {
        votingInv.clear();
        if(votes.get(p) == GameManager.GameMode.HARDCORE) {
            votingInv.setItem(1,normal(false));
            votingInv.setItem(7,hardcore(true));
        } else {
            votingInv.setItem(1,normal(true));
            votingInv.setItem(7,hardcore(false));
        }
        p.openInventory(votingInv);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public World getLobby() {
        return lobby;
    }
}
