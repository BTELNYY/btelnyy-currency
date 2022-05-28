package me.btelnyy.currency.command;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

public class CommandTransactions implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Error: You must be a player to run this command.");
            return true;
        }
        Player player = (Player) sender;
        PlayerData pdata = PlayerDataHandler.GetPlayerData(player);
        //transaction data
        ArrayList<String> tdata = pdata.Transactions;
        if(tdata.isEmpty()){
            player.sendMessage(ChatColor.GRAY + "You have no transactions.");
            return true;
        }
        boolean ignoreLimit = false;
        Integer specificInt = -1;
        if(args.length >= 1){
            for(String s : args){
                if(s.equals("all")){
                    ignoreLimit = true;
                    break;
                }else{
                    try{
                        specificInt = Integer.parseInt(s);
                    }catch(Exception e){
                        player.sendMessage(ChatColor.RED + "Error: Invalid integer after command.");
                        return true;
                    }
                }
            }
        }
        Integer size = tdata.size();
        ArrayList<String> organizeddata = new ArrayList<String>();
        int count = 0;
        if(specificInt != -1){
            try{
                tdata.get(specificInt);
            }catch(Exception e){
                player.sendMessage(ChatColor.RED + "Error: Invalid index specified.");
                return true;
            }
            String data = tdata.get(specificInt);
            String[] datadecomp = data.split(" ");
            player.sendMessage(
                ChatColor.GRAY + "Transaction Info\nID: " + specificInt.toString() + "\nDate: " + datadecomp[0] + "\nType: " + datadecomp[1] + "\nAmount: " + datadecomp[2] + "\nFrom: " + datadecomp[3] + "\nTo: " + datadecomp[4]
            );
            return true;
        }
        for (int counter = size - 1; counter >= 0; counter--) {
            if(count >= Globals.MaxTransactionsShown && !ignoreLimit){
                break;
            }
            count++;
            organizeddata.add(tdata.get(counter));
        }
        player.sendMessage(ChatColor.GRAY + "Recent transactions\n <date> <type> <amount> <from> <to>" );
        for(String s : organizeddata){
            player.sendMessage(ChatColor.GRAY + s);
        }
        return true;
    }
}
