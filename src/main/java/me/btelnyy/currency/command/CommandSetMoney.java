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

public class CommandSetMoney implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        CommandSender sender = arg0;
        Player Target;
        if (arg3[0] == null || Bukkit.getPlayer(arg3[0]) == null || arg3[1] == null) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage /setmoney <target> <amount>");
            return true;
        }
        try{
            Integer.parseInt(arg3[1]);
        }catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Error: Invalid integer format.");
            return true;
        }
        Target = Bukkit.getPlayer(arg3[0]);
        PlayerData TargetData = PlayerDataHandler.GetData(Target.getUniqueId().toString());
        int amount = Integer.parseInt(arg3[1]);
        TargetData.PlayerBalance = amount;
        PlayerDataHandler.SaveData(Target.getUniqueId().toString());
        sender.sendMessage(ChatColor.GRAY + "Gave player " + Target.getName() + " " + Globals.CurrencySymbol + amount);
        return true;
    }
}
