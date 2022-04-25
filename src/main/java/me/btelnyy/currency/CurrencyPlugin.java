package me.btelnyy.currency;

import me.btelnyy.currency.command.*;
import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.listener.EventHandle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.btelnyy.currency.playerdata.PlayerDataHandler;

import java.io.File;
import java.util.logging.Level;

public class CurrencyPlugin extends JavaPlugin {

    //meant for the server to know what to call when doing bukkit timers
    private static CurrencyPlugin instance;

    public static CurrencyPlugin getInstance() {
        return instance;
    }

    public static void log(java.util.logging.Level l, String m) {
        instance.getLogger().log(l, m);
    }

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        instance = this;
        //create dir if it doesnt exist
        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }
        File config_yml = new File(instance.getDataFolder() + "/config.yml");
        if (!config_yml.exists()) {
            try {
                instance.saveDefaultConfig();
            } catch (Exception e) {
                log(Level.SEVERE, "Config.yml could not be created. Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        PlayerDataHandler.GenerateFolder();
        LoadConfig();
        //event handle
        getServer().getPluginManager().registerEvents(new EventHandle(), this);
        //set classloader
        Thread.currentThread().setContextClassLoader(this.getClassLoader());
        //commands
        this.getCommand("pay").setExecutor(new CommandPay());
        this.getCommand("bal").setExecutor(new CommandBalance());
        this.getCommand("balance").setExecutor(new CommandBalance());
        this.getCommand("currencyreset").setExecutor(new CommandResetPlayer());
        this.getCommand("setmoney").setExecutor(new CommandSetMoney());
        this.getCommand("togglepay").setExecutor(new CommandTogglePay());
        this.getCommand("togglepaid").setExecutor(new CommandTogglePaid());
        log(Level.INFO, "Check out the project on GitHub!: https://github.com/BTELNYY/btelnyy-currency");
    }

    // Fired when plugin is disabled
    @Override
    public void onDisable() {
        //if the server is closing, do this.
        PlayerDataHandler.ServerShutdown();
    }

    public void LoadConfig() {
        FileConfiguration config = instance.getConfig();
        Globals.DataFolderName = config.getString("currency_save_path");
        Globals.CurrencyPath = instance.getDataFolder().toString() + Globals.DataFolderName;
        Globals.GlobalMaxMoney = config.getInt("global_max_money");
        Globals.StartingMoney = config.getInt("player_starting_money");
        Globals.DeductAmount = config.getInt("deduct_amount_precent");
        Globals.CurrencySymbol = config.getString("currency_symbol");
    }
}