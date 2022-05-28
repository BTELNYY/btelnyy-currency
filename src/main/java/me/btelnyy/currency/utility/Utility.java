package me.btelnyy.currency.utility;

import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;

public class Utility {
    public static String getDate(){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");  
        String strDate = dateFormat.format(date);  
        return strDate;
    }
    public static boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        if(inv.firstEmpty() != -1){
            return true;
        }else{
            return false;
        }
        /*
        for(ItemStack item: inv.getContents()) {
             if(item == null) {
                     return true;
             }
         }
         
    return false;
    */
    }

    public static ItemStack getBankNote(Integer amount){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Globals.VoucherName);
        ArrayList<String> lore = new ArrayList<String>();
        //add the amount
        lore.add(amount.toString());
        lore.add("Bank Note");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getBankNote(String amount){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Globals.VoucherName);
        ArrayList<String> lore = new ArrayList<String>();
        //add the amount
        lore.add(amount.toString());
        lore.add("Bank Note");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static Integer getPenaltyNaturalDeath(Player p){
        PlayerData DeadData = PlayerDataHandler.GetPlayerData(p);
        int penalty = 0;
        if (DeadData.PlayerBalance > 0) {
            penalty = (int) (DeadData.PlayerBalance * (Globals.DeductAmountNatural / 100.0f));
        }
        return penalty;
    }
    public static String transactionBuilder(Player Sender, Player Target, Integer Amount, String Date, String TransactionType){
        String SenderName = Sender.getName();
        String TargetName = Target.getName();
        Date = Utility.getDate();
        /*
            1. Date
            2. Type
            3. Amount
            4. Sender
            5. Target
        */
        String Transaction = Date + " " + TransactionType + " " + Amount.toString() + " " + SenderName + " " + TargetName;
        return Transaction;
    }
}