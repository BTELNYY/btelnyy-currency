package Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import btelnyy.plugin.Globals;
import btelnyy.plugin.PlayerData.PlayerData;
import btelnyy.plugin.PlayerData.PlayerDataHandler;

public class CommandBalance implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player sender = (Player) arg0;
		PlayerData SenderData = PlayerDataHandler.GetPlayerData(sender.getUniqueId().toString());
		sender.sendRawMessage(ChatColor.YELLOW + "Current balance: " + Globals.CurrencySymbol + SenderData.PlayerBalance);
		return true;
	}
}
