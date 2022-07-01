package me.btelnyy.currency.constant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.ChatColor;

import me.btelnyy.currency.playerdata.PlayerData;

public class Globals {
    public static Hashtable<String, PlayerData> CachedPlayers = new Hashtable<String, PlayerData>();
    //baltop
    public static List<String> BaltopWaiters = new ArrayList<String>();
    public static Hashtable<String, Integer> Baltop  = new Hashtable<String, Integer>();
    public static String BaltopResult = "";
    public static Boolean BaltopRunning = false;
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
    public static int MaximumBalance = -1;
    public static boolean EnforceMaxMoney = true;
    //debug
    public static boolean DebugMode = false;
}
