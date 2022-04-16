package btelnyy.plugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import btelnyy.plugin.Globals;
import btelnyy.plugin.PlayerData.PlayerData;
import btelnyy.plugin.PlayerData.PlayerDataHandler;

public class CommandBalance implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player sender = (Player) arg0;
		if(arg2 != null && sender.hasPermission("btelnyy.command.balance.other") && Bukkit.getPlayer(arg2) != null) {
			Player Target = Bukkit.getPlayer(arg2);
			PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
			sender.sendMessage(ChatColor.GRAY + Target.getName() + "'s balance: " + Globals.CurrencySymbol + TargetData.PlayerBalance);;
		}
		PlayerData SenderData = PlayerDataHandler.GetPlayerData(sender.getUniqueId().toString());
		sender.sendRawMessage(ChatColor.GRAY + "Current balance: " + Globals.CurrencySymbol + SenderData.PlayerBalance);
		return true;
	}
}
