package me.btelnyy.currency.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.btelnyy.currency.playerdata.PlayerDataHandler;

public class CommandResetPlayer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        Player sender = (Player) arg0;
        Player Target;
        if (arg3[0] == null || Bukkit.getPlayer(arg3[0]) == null) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage /currencyreset <player>");
            return true;
        }
        Target = Bukkit.getPlayer(arg3[0]);
        PlayerDataHandler.ResetData(Target.getUniqueId().toString());
        sender.sendMessage(ChatColor.GRAY + "Reset players " + Target.getName() + " currency stats.");
        return true;
    }
}