package btelnyy.plugin;
import java.util.Hashtable;

import btelnyy.plugin.PlayerData.PlayerData;
public class Globals {
	public static Hashtable<String, PlayerData> CachedPlayers = new Hashtable<String, PlayerData>();
	public static int GlobalMaxMoney = 10000;
}
