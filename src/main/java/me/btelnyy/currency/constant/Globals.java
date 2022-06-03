package me.btelnyy.currency.constant;

import java.util.Hashtable;

import org.bukkit.ChatColor;

import me.btelnyy.currency.playerdata.PlayerData;

public class Globals {
    public static Hashtable<String, PlayerData> CachedPlayers = new Hashtable<String, PlayerData>();
    //these contain default values
    public static int GlobalMaxMoney = 10000;
    public static int StartingMoney = 0;
    public static String CurrencyPath = "./plugins/btelnyy-currency/CurrencyData/";
    public static int DeductAmount = 70;
    public static int DeductAmountNatural = 20;
    public static String CurrencySymbol = "$";
    public static String DataFolderName = "CurrencyData";
    public static String VoucherName = ChatColor.GOLD + "Money Voucher (Right click to redeem)";
    public static int MaxWithdrawAmount = 0;
    public static int MaxTransactionsShown = 5;
    public static boolean NoPlayerTransactions = false;
    //debug
    public static boolean DebugMode = false;
}
