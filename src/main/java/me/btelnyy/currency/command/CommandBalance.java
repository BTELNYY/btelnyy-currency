package me.btelnyy.currency.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

public class CommandBalance implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        Player sender = (Player) arg0;
        if (arg3.length < 0 && Bukkit.getPlayer(arg3[0]) != null && Bukkit.getPlayer(arg3[0]) != sender) {
            Player Target = Bukkit.getPlayer(arg3[0]);
            PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
            sender.sendMessage(ChatColor.GRAY + Target.getName() + "'s balance: " + Globals.CurrencySymbol + TargetData.PlayerBalance);
            ;
        }
        PlayerData SenderData = PlayerDataHandler.GetPlayerData(sender.getUniqueId().toString());
        sender.sendRawMessage(ChatColor.GRAY + "Current balance: " + Globals.CurrencySymbol + SenderData.PlayerBalance);
        return true;
    }
}
