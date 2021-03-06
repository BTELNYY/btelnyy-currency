package me.btelnyy.currency.command;

import java.io.*;
import java.nio.file.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import me.btelnyy.currency.CurrencyPlugin;
import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

public class CommandBaltop implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.RED + "Error: You must be a player to run this command.");
            return true;
        }
        if(Globals.BaltopWaiters.contains(player.getUniqueId().toString())){
            player.sendMessage(ChatColor.RED + "Error: You are already queued for the Baltop calculation!");
            return true;
        }
        player.sendMessage(ChatColor.GRAY + "You have been added to the Baltop calculation result list. This may take a while.");
        Globals.BaltopWaiters.add(player.getUniqueId().toString());
        if(!Globals.BaltopRunning){
            Bukkit.getScheduler().runTaskAsynchronously(CurrencyPlugin.getInstance(), new Runnable() {
                static Map<String, Integer> Baltop = new Hashtable<String, Integer>();
                @Override
                public void run() {
                    while(Globals.BaltopRunning){
                        
                        Path directory = Path.of(path);
                        File files = directory.toFile();
                        String[] fileList = files.list();
                        //we just got all the data from the files into a hashtable
                        for(String file : fileList){
                            String[] parts = file.split(".");
                            file = parts[0];
                            Baltop.put(file, (Integer) GetData(file).PlayerBalance);
                        }
                        sort();
                    }
                }
                static void sort(){
                    LinkedList<String> s = new LinkedList<String>();
                    for(String UUID : Baltop.keySet()){
                        PlayerData data = GetData(UUID);
                        if(s.isEmpty()){
                            s.push(UUID);
                        }
                        if(GetData(s.getFirst()).PlayerBalance < data.PlayerBalance || GetData(s.getFirst()).PlayerBalance == data.PlayerBalance){
                            s.push(UUID);
                        }
                        if(GetData(s.getLast()).PlayerBalance > data.PlayerBalance || GetData(s.getLast()).PlayerBalance == data.PlayerBalance){
                            int sizecalc = s.size();
                            s.set(sizecalc, UUID);
                        }
                    }
                    display(s);
                }
                static void display(LinkedList<String> list){
                    Globals.BaltopResult = ChatColor.GRAY + "Baltop results: " +  " (" + list.size() + " )";
                    int counter = 0;
                    for(String ID : list){
                        if(counter == 10){
                            break;
                        }
                        UUID UUID = java.util.UUID.fromString(ID);
                        Globals.BaltopResult += ChatColor.GRAY + "\n" + Bukkit.getPlayer(UUID).getName() + " - " + Globals.CurrencySymbol + GetData(ID).PlayerBalance;
                        counter++;
                    }
                    for(String ID : Globals.BaltopWaiters){
                        UUID UUID = java.util.UUID.fromString(ID);
                        Player player = Bukkit.getPlayer(UUID);
                        player.sendMessage(Globals.BaltopResult);
                    }
                    //cleanup
                    Globals.BaltopRunning = false;
                    Globals.BaltopResult = "";
                    Globals.BaltopWaiters.clear();
                    for(String ID : Baltop.keySet()){
                        UUID UUID = java.util.UUID.fromString(ID);
                        if(!Bukkit.getPlayer(UUID).isOnline()){
                            Globals.CachedPlayers.remove(ID);
                        }
                    }
                }
                static String path = Globals.CurrencyPath;
                static PlayerData GetData(String UUID) {
                    if (Globals.CachedPlayers.containsKey(UUID)) {
                        return Globals.CachedPlayers.get(UUID);
                    } //not cached uuid
                    File player_data = new File(path + PlayerDataHandler.generatePlayerFolder(UUID) + "/" + UUID + ".yml");
                    String yamldata = null;
                    try {
                        yamldata = Files.readString(Path.of(player_data.toString()));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (yamldata == "null") {
                        PlayerDataHandler.DeleteData(UUID);
                        PlayerDataHandler.CreateNewDataFile(UUID);
                        PlayerDataHandler.GetData(UUID);
                        return new PlayerData();
                    }
                    Yaml yaml = new Yaml(new Constructor(PlayerData.class));
                    if (player_data.exists()) {
                        yamldata = null;
                        try {
                            yamldata = Files.readString(Path.of(player_data.toString()));
                        } catch (IOException e) {
                            CurrencyPlugin.getInstance().log(java.util.logging.Level.WARNING, "Can't open read data for UUID: " + UUID + " Message: " + e.getMessage());
                        }
                        PlayerData data = yaml.load(yamldata);
                        if (data == null) {
                            PlayerDataHandler.DeleteData(UUID);
                            data = PlayerDataHandler.CreateNewDataFile(UUID);
                        }
                        Globals.CachedPlayers.put(UUID, data);
                        return data;
                    } else {
                        PlayerData data = PlayerDataHandler.CreateNewDataFile(UUID);
                        Globals.CachedPlayers.put(UUID, data);
                        return data;
                    }
                }
            });
        }
        return true;
    }
    
}
