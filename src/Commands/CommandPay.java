package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import btelnyy.plugin.Globals;
import btelnyy.plugin.PlayerData.PlayerData;
import btelnyy.plugin.PlayerData.PlayerDataHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommandPay implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player Sender = (Player) sender;
		Player Target = Bukkit.getPlayer(arg2);
		int PayAmount = Integer.parseInt(args[3]);
		if(args.length < 2) {
			Sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage: /pay <user> <amount>");
			return true;
		}
		if(Bukkit.getPlayer(arg2) == null){
			Sender.sendMessage(ChatColor.RED + "Error: Player not found.");
			return true;
		}
		//moved to prevent massive errors
		PlayerData SenderData = PlayerDataHandler.GetPlayerData(Sender.getUniqueId().toString());
		PlayerData TargetData = PlayerDataHandler.GetPlayerData(Target.getUniqueId().toString());
		if(PayAmount < 1) {
			Sender.sendMessage(ChatColor.RED + "Error: Pay amount is invalid, must be a integer larger than 0");
			return true;
		}
		//assuming all checks have passed
		if(SenderData.PlayerBalance < PayAmount) {
			Sender.sendMessage(ChatColor.RED + "Error: You do not have enough money.");
			return true;
		}
		if(SenderData.PlayerCanPay == false) {
			Sender.sendMessage(ChatColor.RED + "Error: You cannot pay other players.");
			return true;
		}
		SenderData.PlayerBalance -= PayAmount;
		TargetData.PlayerBalance += PayAmount;
		Sender.sendMessage(ChatColor.GREEN + "You have payed " + Target.getName() + " " + Globals.CurrencySymbol + PayAmount);
		Target.sendMessage(ChatColor.GREEN + "You have received  " + " " + Globals.CurrencySymbol + PayAmount + "from:" + Target.getName());
		//save data but do not remove it from the cache
		PlayerDataHandler.SaveData(SenderData.PlayerUuid);
		PlayerDataHandler.SaveData(TargetData.PlayerUuid);
		return true;
	}
}
