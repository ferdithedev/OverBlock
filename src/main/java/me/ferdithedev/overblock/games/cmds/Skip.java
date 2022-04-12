package me.ferdithedev.overblock.games.cmds;

import me.ferdithedev.overblock.OverBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Skip implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            if(commandSender instanceof Player) {
                Player player = (Player)commandSender;
                if(OverBlock.getInstance().getGameManager().getLobbyManager().getLobby().getPlayers().contains(player)) {
                    OverBlock.getInstance().getGameManager().getLobbyManager().setTime(5);
                    player.sendMessage("§aGame starts in 5 seconds");
                }
            }
        }
        return false;
    }
}
