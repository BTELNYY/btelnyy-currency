package btelnyy.plugin;
import java.util.logging.Level;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import btelnyy.plugin.PlayerData.PlayerData;
import btelnyy.plugin.PlayerData.PlayerDataHandler;
import net.md_5.bungee.api.ChatColor;
public class EventHandle implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String UUID = player.getUniqueId().toString();
		try {
			PlayerDataHandler.GetPlayerData(UUID);
		}catch(Exception e) {
			PlayerDataHandler.DeleteData(UUID);
			Main.log(Level.WARNING, "Failed loading " + UUID + "'s" + "player data. ");
			e.printStackTrace();
			player.kickPlayer(ChatColor.RED + "Your playerdata failed to load, try rejoining");
		}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		String UUID = p.getUniqueId().toString();
		try {
			PlayerDataHandler.SaveAndRemoveData(UUID);
		}catch(Exception e) {
			PlayerDataHandler.DeleteData(UUID);
			String UUID2 = p.getUniqueId().toString();
			PlayerDataHandler.SaveAndRemoveData(UUID2);
		}
	}
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Player deadplayer = (Player) event.getEntity();
		if(event.getEntity().getKiller() == null) {
			return;
		}
		Player killer = (Player) event.getEntity().getKiller();
		PlayerData DeadData = PlayerDataHandler.GetPlayerData(deadplayer.getUniqueId().toString());
		PlayerData KillerData = PlayerDataHandler.GetPlayerData(killer.getUniqueId().toString());
		int penalty = 0;
		if(DeadData.PlayerBalance > 0) {
			penalty = (int)(DeadData.PlayerBalance*(Globals.DeductAmount/100.0f));
		}
		DeadData.PlayerBalance -= penalty;
		KillerData.PlayerBalance += penalty;
		PlayerDataHandler.SaveData(DeadData.PlayerUuid);
		PlayerDataHandler.SaveData(KillerData.PlayerUuid);
		deadplayer.sendMessage(ChatColor.GRAY + "You lost " + Globals.CurrencySymbol + penalty + " becuase you died to " + killer.getName());
		killer.sendMessage(ChatColor.GRAY + "You earned " + Globals.CurrencySymbol + penalty + " for killing " + deadplayer.getName());
	}
}
