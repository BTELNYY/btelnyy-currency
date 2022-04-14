package btelnyy.plugin;
import java.io.*;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
public class PlayerDataHanlder {
	public static PlayerData GetPlayerData(String UUID) {
		if(Globals.CachedPlayers.contains(UUID)) {
			return Globals.CachedPlayers.get(UUID);
		} //not cached uuid
		File player_data = new File("./plugins/btelnyy/player_data/" + UUID + ".yml");
		Yaml yaml = new Yaml(new Constructor(PlayerData.class));
		if(player_data.exists()) {
			Map<String, Object> obj = yaml.load(player_data.toString());
			PlayerData data = new PlayerData();
			data.player_uuid = (String) obj.get("player_uuid");
			data.player_balance = (int) obj.get("player_balance");
			return data;
		}else {
			CreateNewDataFile(UUID);
			Map<String, Object> obj = yaml.load(player_data.toString());
			PlayerData data = new PlayerData();
			data.player_uuid = (String) obj.get("player_uuid");
			data.player_balance = (int) obj.get("player_balance");
			return data;
		}
	}
	public static void CreateNewDataFile(String UUID) {
		File player_data = new File("./plugins/btelnyy/player_data/" + UUID + ".yml");
		try {
			player_data.createNewFile();
			FileWriter writer = new FileWriter(player_data);
			writer.append("player_uuid: \"0\"\n");
			writer.append("player_balance: \"0\"\n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
