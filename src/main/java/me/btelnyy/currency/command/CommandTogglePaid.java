package me.btelnyy.currency.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommandTogglePaid implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        Player Sender = (Player) sender;
        if (args.length < 1) {
            Sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage: /togglepaid <player>");
            return true;
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            Sender.sendMessage(ChatColor.RED + "Error: Player not found.");
            return true;
        }
        Player Target = Bukkit.getPlayer(args[0]);
        PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
        TargetData.PlayerCanBePaid = !TargetData.PlayerCanBePaid;
        if (TargetData.PlayerCanBePaid) {
            Sender.sendMessage(ChatColor.GRAY + "Player " + Target.getName() + " can now be paid by other players.");
        } else {
            Sender.sendMessage(ChatColor.GRAY + "Player " + Target.getName() + " can no longer be paid by other players.");
        }
        PlayerDataHandler.SaveData(TargetData.PlayerUuid);
        return true;
    }
}