package btelnyy.plugin.PlayerData;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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
			String yamldata = null;
			try {
				yamldata = Files.readString(Path.of(player_data.toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Main.log(java.util.logging.Level.WARNING, "Can't open read data for UUID: " + UUID + " Message: " + e.getMessage());
			}
			PlayerData data = yaml.load(yamldata);
			Globals.CachedPlayers.put(UUID, data);
			return data;
		}else{
			PlayerData data = CreateNewDataFile(UUID);
			Globals.CachedPlayers.put(UUID, data);
			return data;
		}
	}
	public static PlayerData CreateNewDataFile(String UUID) {
		Yaml yaml = new Yaml();
		File player_data = new File("./plugins/btelnyy/currency_data/" + UUID + ".yml");
		try {
			player_data.createNewFile();
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = new PlayerData();
			pd.PlayerUuid = UUID;
			pd.PlayerBalance = Globals.StartingMoney;
			yaml.dump(pd, writer);
			writer.close();
			return pd;
		} catch (Exception e) {
			PlayerData pd = new PlayerData();
			pd.PlayerUuid = UUID;
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to create playerdata for " + UUID + ": " + e.getMessage());
			return pd;
		}
	}
	public static void SaveAndRemoveData(String UUID) {
		Yaml yaml = new Yaml();
		File player_data = new File("./plugins/btelnyy/currency_data/" + UUID + ".yml");
		try {
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = Globals.CachedPlayers.get(UUID);
			yaml.dump(pd, writer);
			writer.close();
			Globals.CachedPlayers.remove(UUID);
		} catch (Exception e) {
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to save playerdata for " + UUID + ": " + e.getMessage());
		}
	}
}
