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

public class CommandSetMoney implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player sender = (Player) arg0;
		Player Target;
		if(arg3[0] == null || Bukkit.getPlayer(arg3[0]) == null || arg3[1] == null) {
			sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage /setmoney <target> <amount>");
			return true;
		}
		Target = Bukkit.getPlayer(arg3[0]);
		PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
		int amount = Integer.parseInt(arg3[1]);
		TargetData.PlayerBalance = amount;
		PlayerDataHandler.SaveData(Target.getUniqueId().toString());
		sender.sendMessage(ChatColor.GRAY + "Gave player " + Target.getName() + " " + Globals.CurrencySymbol + amount);
		return true;
	}
}
