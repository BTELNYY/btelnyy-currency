package btelnyy.plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import btelnyy.plugin.PlayerData.PlayerDataHandler;
public class EventHandle implements Listener{
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String UUID = player.getUniqueId().toString();
		PlayerDataHandler.GetPlayerData(UUID);
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		String UUID = p.getUniqueId().toString();
		PlayerDataHandler.SaveAndRemoveData(UUID);
	}
	@EventHandler
	public void onPlayerKicked(PlayerKickEvent event) {
		Player p = event.getPlayer();
		String UUID = p.getUniqueId().toString();
		PlayerDataHandler.SaveAndRemoveData(UUID);
	}
}
