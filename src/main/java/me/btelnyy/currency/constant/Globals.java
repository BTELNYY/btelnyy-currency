package me.btelnyy.currency.constant;

import java.util.Hashtable;

import me.btelnyy.currency.playerdata.PlayerData;

public class Globals {
    public static Hashtable<String, PlayerData> CachedPlayers = new Hashtable<String, PlayerData>();
    //these contain default values
    public static int GlobalMaxMoney = 10000;
    public static int StartingMoney = 0;
    public static String CurrencyPath = "./plugins/btelnyy-currency/CurrencyData/";
    public static int DeductAmount = 70;
    public static String CurrencySymbol = "$";
    public static String DataFolderName = "CurrencyData";
}
