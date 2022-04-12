package me.ferdithedev.overblock.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BetterTeleport implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length >= 3) {
            Player p1 = Bukkit.getPlayer(args[0]);
            if(p1 != null) {
                if(args.length >= 4) {
                    for(int i = 1; i < 4; i++) if(!isNumeric(args[i])) {
                        commandSender.sendMessage("§cFalse args");
                        return false;
                    }

                    double x = Double.parseDouble(args[1]);
                    double y = Double.parseDouble(args[2]);
                    double z = Double.parseDouble(args[3]);

                    Location loc = new Location(p1.getWorld(),x,y,z);

                    if(args.length >= 5) {
                        World world = Bukkit.getWorld(args[4]);
                        if(world != null) {
                            if(args.length >= 7) {
                                if(isNumeric(args[5]) && isNumeric(args[6])) {
                                    float yaw = Float.parseFloat(args[5]);
                                    float pitch = Float.parseFloat(args[6]);

                                    loc = new Location(world,x,y,z,yaw,pitch);
                                }
                            } else {
                                loc = new Location(world,x,y,z);
                            }
                        }
                    }

                    p1.teleport(loc);
                }
            } else {
                if(commandSender instanceof Player p) {

                    for(int i = 0; i < 3; i++) if(!isNumeric(args[i])) {
                        commandSender.sendMessage("§cFalse args");
                        return false;
                    }

                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);

                    Location loc = new Location(p.getWorld(),x,y,z);

                    if(args.length >= 4) {
                        World world = Bukkit.getWorld(args[3]);
                        if(world != null) {
                            if(args.length >= 6) {
                                if(isNumeric(args[4]) && isNumeric(args[5])) {
                                    float yaw = Float.parseFloat(args[4]);
                                    float pitch = Float.parseFloat(args[5]);

                                    loc = new Location(world,x,y,z,yaw,pitch);
                                }
                            } else {
                                loc = new Location(world,x,y,z);
                            }
                        }
                    }

                    p.teleport(loc);
                }
            }
        } else if(args.length == 1 && commandSender instanceof Player p) {
            Player p2 = Bukkit.getPlayer(args[0]);
            if(p2 != null) p.teleport(p2);
        } else if(args.length == 2) {
            Player p31 = Bukkit.getPlayer(args[0]);
            Player p32 = Bukkit.getPlayer(args[1]);
            if(p31 != null && p32 != null) {
                p31.teleport(p32);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player p) {
            List<String> list = new ArrayList<>();

            if(args.length == 1) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }
                list.remove(p.getName());
                list.add(String.valueOf(p.getLocation().getBlockX()));
                return list;
            }

            if(args.length == 2) {
                Player p1 = Bukkit.getPlayer(args[0]);
                if(p1 == null) {
                    list.add(String.valueOf(p.getLocation().getBlockY()));
                } else {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        list.add(player.getName());
                    }
                    list.add(String.valueOf(p.getLocation().getBlockX()));
                }
                return list;
            }

            if(args.length == 3) {
                Player p1 = Bukkit.getPlayer(args[0]);
                if(p1 == null) {
                    list.add(String.valueOf(p.getLocation().getBlockZ()));
                } else {
                    list.add(String.valueOf(p.getLocation().getBlockY()));
                }
                return list;
            }

            if(args.length == 4) {
                Player p1 = Bukkit.getPlayer(args[0]);
                if(p1 == null) {
                    for(World w : Bukkit.getWorlds()) {
                        list.add(w.getName());
                    }
                } else {
                    list.add(String.valueOf(p.getLocation().getBlockZ()));
                }
                return list;
            }
        }

        return null;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
