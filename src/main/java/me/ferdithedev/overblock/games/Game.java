package me.ferdithedev.overblock.games;

import me.ferdithedev.overblock.OverBlock;
import me.ferdithedev.overblock.games.arena.Spawnpoint;
import me.ferdithedev.overblock.games.arena.better.LocalGameMap;
import me.ferdithedev.overblock.games.teams.Team;
import me.ferdithedev.overblock.games.teams.TeamObject;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.ferdithedev.overblock.games.ItemSpawner.spawnItem;

public class Game {

    private BukkitRunnable itemSpawner;
    private final List<Player> participants;
    private final LocalGameMap arena;
    private final GameManager.GameMode mode;
    private final List<Team> teams = new ArrayList<>();
    private final HashMap<Player, Integer> lifes = new HashMap<>();
    private final HashMap<Player, Integer> kills = new HashMap<>();
    private final Scoreboard gameScoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
    private BukkitRunnable runnable;
    private int time;
    private GameManager.GameState state;
    private final JavaPlugin plugin;

    public Game(LocalGameMap arena, List<Player> participants, GameManager.GameMode mode, JavaPlugin plugin) {
        this.plugin = plugin;
        this.participants = participants;
        this.arena = arena;
        this.mode = mode;
        this.time = OverBlock.settings.ROUNDTIME*60;
    }

    public boolean isHardcore() {
        return mode == GameManager.GameMode.HARDCORE;
    }

    public void death(Player p) {
        if(!(lifes.get(p) > 0)) return;
        if(playerIsNearDeath(p)) {
            GameManager.gameBroadcast(participants,deathMessage(p,true));
            p.setGameMode(GameMode.SPECTATOR);
            lifes.put(p,0);
            p.sendMessage("§cThat was your last life. You died and you won't respawn!");
            if(aliveTeams().size() == 1) end(aliveTeams().get(0));
        } else {
            GameManager.gameBroadcast(participants,deathMessage(p,false));
            lifes.put(p,lifes.get(p)-1);
            p.sendMessage("§eYou died! §c"+lifes.get(p)+" §elifes remaining!");
        }
        Spawnpoint spawnpoint = arena.getArena().getSpawnpoint(teamOfPlayer(p));
        assert spawnpoint != null;
        Location loc = new Location(arena.getWorld(), spawnpoint.getCord()[0],spawnpoint.getCord()[1],spawnpoint.getCord()[2],spawnpoint.getLooking()[0],spawnpoint.getLooking()[1]);
        p.teleport(loc);
        p.setHealth(20);
        p.setFoodLevel(20);
    }

    public void death(Player p, Player killer) {
        kills.put(killer,kills.get(killer)+1);
        if(playerIsNearDeath(p)) {
            GameManager.gameBroadcast(participants,deathMessage(p,killer,true));
            p.setGameMode(GameMode.SPECTATOR);
            lifes.put(p,0);
            p.sendMessage("§cThat was your last life. You died and you won't respawn!");
            if(aliveTeams().size() == 1) end(teamOfPlayer(killer));
        } else {
            GameManager.gameBroadcast(participants,deathMessage(p,killer,false));
            lifes.put(p,lifes.get(p)-1);
            p.sendMessage("§eYou died! §c"+lifes.get(p)+" §elifes remaining!");
        }
        Spawnpoint spawnpoint = arena.getArena().getSpawnpoint(teamOfPlayer(p));
        Location loc = new Location(arena.getWorld(), spawnpoint.getCord()[0],spawnpoint.getCord()[1],spawnpoint.getCord()[2],spawnpoint.getLooking()[0],spawnpoint.getLooking()[1]);
        p.teleport(loc);
        p.setHealth(20);
        p.setFoodLevel(20);
    }

    private String deathMessage(Player p, boolean finaldeath) {
        String str;
        if(finaldeath) {
            str = Messages.getRandomMsg(Messages.FINAL_DEATH);
        } else {
            str = Messages.getRandomMsg(Messages.DEATH);
        }
        return str.replace("%player%",coloredName(p));
    }

    private String deathMessage(Player p, Player killer, boolean finaldeath) {
        String str;
        if(finaldeath) {
            str = Messages.getRandomMsg(Messages.FINAL_DEATH_KILLER);
        } else {
            str = Messages.getRandomMsg(Messages.DEATH_KILLER);
        }
        return str.replace("%player%",coloredName(p)).replace("%killer%",coloredName(killer));
    }

    public boolean playerIsNearDeath(Player p) {
        return lifes.get(p) == 1;
    }

    public void removePlayer(Player p) {
        Objects.requireNonNull(teamOfPlayer(p)).removeMember(p);
        if(aliveTeams().size() == 1) {
            end(aliveTeams().get(0));
        }
    }

    private List<Team> aliveTeams() {
        List<Team> at = new ArrayList<>();
        for(Team t : teams) {
            if(!t.isEmpty()) {
                at.add(t);
            }
        }
        return at;
    }

    public Team teamOfPlayer(Player p) {
       for(Team t : teams) {
           if(t.getMembers().contains(p)) {
               return t;
           }
       }
       return null;
    }

    public boolean playerAlive(Player p) {
        return lifes.get(p) >= 1;
    }

    public void start() {
        state = GameManager.GameState.START;

        for(Entity e : arena.getWorld().getEntities()) {
            if(e instanceof Item item) {
                item.remove();
            }
        }

        itemSpawner = new BukkitRunnable() {
            @Override
            public void run() {
               spawnItem(-1,arena.getCuboid().getRandomSolidLocation());
            }
        };
        itemSpawner.runTaskTimer(plugin,0,200);

        List<TeamObject> objs = OverBlock.teamObjects;
        for(TeamObject tobj : objs) {
            this.teams.add(new Team(this,tobj.getName(), tobj.getColor()));
        }
        int i = 0;
        for(Player p : participants) {
            this.teams.get(i).addMember(p);
            i++;
            if(i > this.teams.size()) {
                i = 0;
            }
        }
        for(Team t : this.teams) {
            for(Player p : t.getMembers()) {
                Spawnpoint spawnpoint = arena.getArena().getSpawnpoint(t);
                Location loc = new Location(arena.getWorld(), spawnpoint.getCord()[0],spawnpoint.getCord()[1],spawnpoint.getCord()[2],spawnpoint.getLooking()[0],spawnpoint.getLooking()[1]);
                p.teleport(loc);
                if(isHardcore()) {
                    p.sendTitle("§c§lHARDCORE","§7You only have 1 life!",5,40,5);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,1,0.5f);
                    lifes.put(p,1);
                } else {
                    p.sendTitle("§e§lSTART","",5,40,5);
                    lifes.put(p,3);
                }
                kills.put(p,0);
                p.getInventory().clear();
                p.setFoodLevel(20);
                p.setHealth(20);
                String creator = arena.getArena().creator();
                if(creator != null && !creator.strip().isEmpty()) p.sendMessage("§eMap build by: §c" + arena.getArena().creator());
            }
        }
        state = GameManager.GameState.INGAME;
        initTimer();
    }

    private void initTimer() {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(time % 60 == 0) {
                    if(time/60 <= 5 && time/60 > 1) {
                        GameManager.gameBroadcast(participants, "§eGame ends in §3" + time/60 + "§e minutes.");
                    }
                    if(time/60==1) {
                        GameManager.gameBroadcast(participants,"§eGame ends in §31 §eminute.");
                    }
                    if(time == 10 || time <= 5 && time > 1) {
                        GameManager.gameBroadcast(participants,"§eGame ends in §3" + time + "§e seconds");
                    }
                    if(time == 0) {
                        GameManager.gameBroadcast(participants,"§31 §esecond left!");
                    }
                }
                if(time == 0) {
                    end(null);
                }
                time--;
            }
        };
        this.runnable.runTaskTimer(OverBlock.getInstance(), 0, 20);
    }

    private void end(Team winner) {
        state = GameManager.GameState.ENDING;
        runnable.cancel();

        itemSpawner.cancel();
        for(Entity e : arena.getWorld().getEntities()) {
            if(e instanceof Item item) {
                item.remove();
            }
        }

        if(winner == null) {
            GameManager.gameBroadcast(participants,"§e§lWinner: §c§lNO WINNER");
        } else {
            GameManager.gameBroadcast(participants,"§e§lWinner: " + winner.getColor()+winner.getName());
        }
        for(Player p : participants) {
            if(kills.get(p) > 0) p.sendMessage("§e§lYour kills: "+kills.get(p));
            if(lifes.get(p) > 0) p.sendMessage("§e§lYour remaining lifes: "+lifes.get(p));
            p.getInventory().clear();
        }
        Bukkit.getScheduler().runTaskLater(OverBlock.getInstance(), () -> {
            for(Team t : teams) {
                t.clear();
            }
            for(Player p : participants) {
                p.teleport(OverBlock.settings.SPAWNLOCATION);
                p.setGameMode(GameMode.ADVENTURE);
            }
            OverBlock.getGameManager().getArenaManager().inactiveArena(arena);

            OverBlock.getGameManager().removeGame(this);
        }, 100);
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public LocalGameMap getArena() {
        return arena;
    }

    public Scoreboard getGameScoreboard() {
        return gameScoreboard;
    }

    public String coloredName(Player p) {
        return Objects.requireNonNull(teamOfPlayer(p)).getColor() + p.getName() + "§r";
    }

    public GameManager.GameState getState() {
        return state;
    }
}
