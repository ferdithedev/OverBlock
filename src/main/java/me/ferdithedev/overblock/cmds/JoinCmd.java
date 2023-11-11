package me.ferdithedev.overblock.cmds;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            if(!p.getWorld().equals(OverBlock.settings.LOBBYSPAWNLOCATION.getWorld())) {
                p.teleport(OverBlock.settings.LOBBYSPAWNLOCATION);
            }
        }
        return false;
    }
}
