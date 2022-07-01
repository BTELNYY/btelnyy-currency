package me.btelnyy.currency.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommandSetMaxBalance implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        Player Sender = (Player) sender;
        if (args.length < 2) {
            Sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage: /setmaxbal <player> <amount>");
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            Sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return true;
        }
        Player Target = Bukkit.getPlayer(args[0]);
        PlayerData TargetData = PlayerDataHandler.GetData(Target);
        int amount = 0;
        try{
            amount = Integer.parseInt(args[1]);
        }catch(Exception e){
            Sender.sendMessage(ChatColor.RED + "Error: Invalid integer format.");
            amount = TargetData.MaximumBalance;
            return true;
        }
        TargetData.MaximumBalance = amount;
        PlayerDataHandler.SaveData(TargetData.PlayerUuid);
        return true;
    }
}