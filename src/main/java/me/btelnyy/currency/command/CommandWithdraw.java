package me.btelnyy.currency.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.playerdata.*;
import me.btelnyy.currency.utility.Utility;

public class CommandWithdraw implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        //withdraw thingy
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Error: You must be a player to run this command.");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage(ChatColor.RED + "Error: Invalid syntax. Usage: /withdraw <amount>");
            return true;
        }
        if(Globals.NoPlayerTransactions){
            sender.sendMessage(ChatColor.RED + "Error: Global player interactions are currnetly disabled.");
            return true;
        }
        //assumes sender check passes
        Player p = (Player) sender;
        PlayerData data = PlayerDataHandler.GetData(p);
        if(!data.PlayerCanWithdraw){
            p.sendMessage(ChatColor.RED + "Error: You cannot withdraw money.");
        }
        try{
            Integer.parseInt(args[0]);
        }catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Error: Invalid integer format.");
            return true;
        }
        int amount = Integer.parseInt(args[0]);
        if(amount > data.PlayerBalance){
            sender.sendMessage(ChatColor.RED + "Error: You cannot withdraw more than you have in your balance.");
            return true;
        }
        if(Globals.MaxWithdrawAmount > 0){
            if(amount > Globals.MaxWithdrawAmount){
                sender.sendMessage(ChatColor.RED + "Error: You cannot withdraw more than " + Globals.CurrencySymbol + Globals.MaxWithdrawAmount + " per interaction.");
                return true;
            }
        }
        //everything checks out
        PlayerInventory inv = p.getInventory();
        ItemStack item = Utility.getBankNote(args[0]);
        if(Utility.hasAvaliableSlot(p)){
            inv.addItem(item);
            p.sendMessage(ChatColor.GRAY + "Withdrew " + Globals.CurrencySymbol + amount);
        }else{
            //drop the item if the inventory is full
            p.sendMessage(ChatColor.GRAY + "Inventory full! The item has been thrown on the ground.");
            p.getLocation().getWorld().dropItem(p.getLocation(), item);
        }
        data.PlayerBalance =  data.PlayerBalance - amount;
        PlayerDataHandler.SaveData(p.getUniqueId().toString());
        return true;
    }
}
