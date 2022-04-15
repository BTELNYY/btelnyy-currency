package btelnyy.plugin.PlayerData;
import java.io.*;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import btelnyy.plugin.Globals;
import btelnyy.plugin.Main;
public class PlayerDataHandler {
	public static PlayerData GetPlayerData(String UUID) {
		if(Globals.CachedPlayers.contains(UUID)) {
			return Globals.CachedPlayers.get(UUID);
		} //not cached uuid
		File player_data = new File("./plugins/btelnyy/currency_data/" + UUID + ".yml");
		Yaml yaml = new Yaml(new Constructor(PlayerData.class));
		if(player_data.exists()) {
			Map<String, Object> obj = yaml.load(player_data.toString());
			PlayerData data = new PlayerData();
			data.player_uuid = (String) obj.get("player_uuid");
			data.player_balance = (int) obj.get("player_balance");
			Globals.CachedPlayers.put(UUID, data);
			return data;
		}else{
			CreateNewDataFile(UUID);
			Map<String, Object> obj = yaml.load(player_data.toString());
			PlayerData data = new PlayerData();
			data.player_uuid = (String) obj.get("player_uuid");
			data.player_balance = (int) obj.get("player_balance");
			Globals.CachedPlayers.put(UUID, data);
			return data;
		}
	}
	public static void CreateNewDataFile(String UUID) {
		Yaml yaml = new Yaml();
		File player_data = new File("./plugins/btelnyy/currency_data/" + UUID + ".yml");
		try {
			player_data.createNewFile();
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = new PlayerData();
			pd.setplayer_balance(0);
			pd.setplayer_uuid(UUID);
			yaml.dump(pd, writer);
		} catch (Exception e) {
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to create playerdata for " + UUID + ": " + e.getMessage());
		}
	}
	public static void SaveAndRemoveData(String UUID) {
		Yaml yaml = new Yaml();
		File player_data = new File("./plugins/btelnyy/currency_data/" + UUID + ".yml");
		try {
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = Globals.CachedPlayers.get(UUID);
			yaml.dump(pd, writer);
			Globals.CachedPlayers.remove(UUID);
		} catch (Exception e) {
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to save playerdata for " + UUID + ": " + e.getMessage());
		}
	}
}
