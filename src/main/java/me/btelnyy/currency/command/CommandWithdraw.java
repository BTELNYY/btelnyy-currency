package me.btelnyy.currency.command;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

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
        //assumes sender check passes
        Player p = (Player) sender;
        PlayerData data = PlayerDataHandler.GetPlayerData(p.getUniqueId().toString());
        int amount = Integer.parseInt(args[0]);
        if(amount > data.PlayerBalance){
            sender.sendMessage(ChatColor.RED + "Error: You cannot withdraw more than you have in your balance.");
            return true;
        }
        //everything checks out
        PlayerInventory inv = p.getInventory();
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Money Voucher (Right click to redeem)");
        ArrayList<String> lore = new ArrayList<String>();
        //add the amount
        lore.add(args[0]);
        meta.setLore(lore);
        
        //update meta
        item.setItemMeta(meta);
        if(Utility.hasAvaliableSlot(p)){
            inv.addItem(item);
        }else{
            //drop the item if the inventory is full
            p.getLocation().getWorld().dropItemNaturally(p.getLocation(), item);
        }
        data.PlayerBalance =  data.PlayerBalance - amount;
        PlayerDataHandler.SaveData(p.getUniqueId().toString());
        return true;
    }
}
