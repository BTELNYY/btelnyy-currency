package me.btelnyy.currency;

import me.btelnyy.currency.command.*;
import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.listener.EventHandle;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
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

    public void log(java.util.logging.Level l, String m) {
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
        registerCommandExecutor("pay", new CommandPay());
        registerCommandExecutor("bal", new CommandBalance());
        registerCommandExecutor("balance", new CommandBalance());
        registerCommandExecutor("currencyreset", new CommandResetPlayer());
        registerCommandExecutor("setmoney", new CommandSetMoney());
        registerCommandExecutor("togglepay", new CommandTogglePay());
        registerCommandExecutor("togglepaid", new CommandTogglePaid());
        registerCommandExecutor("withdraw", new CommandWithdraw());
        registerCommandExecutor("transactions", new CommandTransactions());
        registerCommandExecutor("togglewithdraw", new CommandToggleWithdraw());
        
        log(Level.INFO, "Check out the project on GitHub!: https://github.com/BTELNYY/btelnyy-currency");
    }

    private void registerCommandExecutor(String commandName, CommandExecutor commandExecutor) {
        PluginCommand command = this.getCommand(commandName);
        if (command == null)
            throw new NullPointerException(String.format("\"%s\" is not registered in the plugin.yml", commandName));
        command.setExecutor(commandExecutor);
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
        Globals.DeductAmountNatural = config.getInt("currency_deduct_amount_natural");
        Globals.MaxWithdrawAmount = config.getInt("max_withdraw_amount");
        Globals.DebugMode = config.getBoolean("debug");
        Globals.EnforceMaxMoney = config.getBoolean("enforce_max_money_player");
        Globals.MaximumBalance = config.getInt("player_max_money");
        log(Level.INFO, "Loaded config.");
    }
}