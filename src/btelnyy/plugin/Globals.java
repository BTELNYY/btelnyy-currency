package btelnyy.plugin;
import java.util.Hashtable;

import btelnyy.plugin.PlayerData.PlayerData;
public class Globals {
	public static Hashtable<String, PlayerData> CachedPlayers = new Hashtable<String, PlayerData>();
	//these contain default values
	public static int GlobalMaxMoney = 10000;
	public static int StartingMoney = 0;
	public static String CurrencyPath = "./plugins/btelnyy-social-currency-expierement/CurrencyData/";
	public static int DeductAmount = 70;
	public static String CurrencySymbol = "$";
}
