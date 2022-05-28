package me.btelnyy.currency.command;

import me.btelnyy.currency.constant.Globals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommandPay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        Player Sender = (Player) sender;
        if (args.length < 2) {
            Sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage: /pay <user> <amount>");
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            Sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return true;
        }
        Player Target = Bukkit.getPlayer(args[0]);
        int PayAmount = Integer.parseInt(args[1]);
        if (Sender == Target && Globals.DebugMode == false) {
            Sender.sendMessage(ChatColor.RED + "Error: You cannot pay yourself.");
            return true;
        }
        //moved to prevent massive errors
        PlayerData SenderData = PlayerDataHandler.GetPlayerData(Sender.getUniqueId().toString());
        PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
        if (SenderData.PlayerCanPay == false) {
            Sender.sendMessage(ChatColor.RED + "Error: You cannot pay other players.");
            return true;
        }
        if (!TargetData.PlayerCanBePaid) {
            Sender.sendMessage(ChatColor.RED + "Error: You cannot pay this user.");
            return true;
        }
        if (PayAmount < 1) {
            Sender.sendMessage(ChatColor.RED + "Error: Pay amount is invalid, must be a integer larger than 0");
            return true;
        }
        //assuming all checks have passed
        if (SenderData.PlayerBalance < PayAmount) {
            Sender.sendMessage(ChatColor.RED + "Error: You do not have enough money.");
            return true;
        }
        SenderData.PlayerBalance -= PayAmount;
        TargetData.PlayerBalance += PayAmount;
        
        Sender.sendMessage(ChatColor.GRAY + "You have payed " + Target.getName() + " " + Globals.CurrencySymbol + PayAmount);
        Target.sendMessage(ChatColor.GRAY + "You have received " + Globals.CurrencySymbol + PayAmount + " from: " + Target.getName());
        //save data but do not remove it from the cache
        PlayerDataHandler.SaveData(SenderData.PlayerUuid);
        PlayerDataHandler.SaveData(TargetData.PlayerUuid);
        return true;
    }
}
