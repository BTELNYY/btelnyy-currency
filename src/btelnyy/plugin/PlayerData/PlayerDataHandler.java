package btelnyy.plugin.PlayerData;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.logging.Level;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import btelnyy.plugin.Globals;
import btelnyy.plugin.Main;
public class PlayerDataHandler {
	static String path = Globals.CurrencyPath;
	public static void GenerateFolder() {
    	Path cur_config = Path.of(path);
    	if(Files.notExists(cur_config, LinkOption.NOFOLLOW_LINKS)) {
    		try {
				Files.createDirectory(cur_config);
			} catch (Exception e) {
				Main.log(Level.SEVERE, "Can't create currency folder! Folder path: " + path + "Error: " + e.getMessage());
				e.printStackTrace();
			}
    	}
	}
	public static PlayerData GetPlayerData(String UUID) {
		if(Globals.CachedPlayers.contains(UUID)) {
			return Globals.CachedPlayers.get(UUID);
		} //not cached uuid
		File player_data = new File(path + UUID + ".yml");
		String yamldata = null;
		try {
			yamldata = Files.readString(Path.of(player_data.toString()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(yamldata == "null") {
			DeleteData(UUID);
			CreateNewDataFile(UUID);
			GetPlayerData(UUID);
			return new PlayerData();
		}
		Yaml yaml = new Yaml(new Constructor(PlayerData.class));
		if(player_data.exists()) {
			yamldata = null;
			try {
				yamldata = Files.readString(Path.of(player_data.toString()));
			} catch (IOException e) {
				Main.log(java.util.logging.Level.WARNING, "Can't open read data for UUID: " + UUID + " Message: " + e.getMessage());
			}
			PlayerData data = yaml.load(yamldata);
			if(data == null) {
				DeleteData(UUID);
				data = CreateNewDataFile(UUID);
			}
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
		File player_data = new File(path + UUID + ".yml");
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
		File player_data = new File(path + UUID + ".yml");
		try {
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = Globals.CachedPlayers.get(UUID);
			yaml.dump(pd, writer);
			writer.close();
			Main.log(Level.INFO, "Saving " + UUID + "'s data");
			Globals.CachedPlayers.remove(UUID);
		} catch (Exception e) {
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to save playerdata for " + UUID + ": " + e.getMessage());
		}
	}
	public static void SaveData(String UUID) {
		Yaml yaml = new Yaml();
		File player_data = new File(path + UUID + ".yml");
		try {
			FileWriter writer = new FileWriter(player_data);
			PlayerData pd = Globals.CachedPlayers.get(UUID);
			yaml.dump(pd, writer);
			writer.close();
			Main.log(Level.INFO, "Saving " + UUID + "'s data");
		} catch (Exception e) {
			Main.log(java.util.logging.Level.WARNING, "An error occured when trying to save playerdata for " + UUID + ": " + e.getMessage());
		}
	}
	public static void ResetData(String UUID) {
		PlayerData data = Globals.CachedPlayers.get(UUID);
		data.PlayerBalance = 0;
		data.PlayerCanPay = true;
		data.PlayerUuid = UUID;
		Globals.CachedPlayers.remove(UUID);
		Globals.CachedPlayers.put(UUID, data);
		SaveData(UUID);
	}
	public static void DeleteData(String UUID) {
		File player_data = new File(path + UUID + ".yml");
		player_data.delete();
	}
	public static void SaveAll() {
		Main.log(Level.INFO, "Saving all players data....");
		for(String UUID : Globals.CachedPlayers.keySet()) {
			SaveData(UUID);
		}
	}
	public static void ServerShutdown() {
    	for(String key : Globals.CachedPlayers.keySet()) {
    		PlayerDataHandler.SaveAndRemoveData(key);
    	}
	}
}
