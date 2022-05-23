package me.btelnyy.currency.utility;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utility {
    public static boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        if(inv.firstEmpty() == 0){
            return false;
        }
        for (ItemStack item: inv.getContents()) {
             if(item == null) {
                     return true;
             }
         }
    return false;
    }
}
