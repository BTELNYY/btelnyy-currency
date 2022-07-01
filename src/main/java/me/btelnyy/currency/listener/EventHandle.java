package me.btelnyy.currency.listener;

import java.util.List;
import java.util.logging.Level;

import me.btelnyy.currency.constant.Globals;
import me.btelnyy.currency.CurrencyPlugin;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import me.btelnyy.currency.playerdata.PlayerData;
import me.btelnyy.currency.playerdata.PlayerDataHandler;
import me.btelnyy.currency.utility.Utility;

public class EventHandle implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String UUID = player.getUniqueId().toString();
        try {
            PlayerDataHandler.GetData(UUID);
        } catch (Exception e) {
            PlayerDataHandler.DeleteData(UUID);
            CurrencyPlugin.getInstance().log(Level.WARNING, "Failed loading " + UUID + "'s" + "player data. ");
            e.printStackTrace();
            player.kickPlayer(ChatColor.RED + "Your playerdata failed to load, try rejoining. (it has also been reset, contact server admins for more info.");
            PlayerDataHandler.CreateNewDataFile(player);
        }
    }
    @EventHandler
    public void onItemInteract(PlayerInteractEvent event){
        Action action = event.getAction();
        if(action == Action.PHYSICAL){
            return;
        }
        Player p = event.getPlayer();
        ItemStack main = p.getInventory().getItemInMainHand();
        if(main.getType() != Material.PAPER){
            return;
        }
        ItemMeta meta = main.getItemMeta();
        if(!meta.hasLore()){
            return;
        }
        List<String> lore = meta.getLore();
        if(!(lore.get(1).equals("Bank Note"))){
            return;
        }
        int amount = Integer.parseInt(lore.get(0));
        PlayerData data = PlayerDataHandler.GetData(p);
        Inventory inv = p.getInventory();
        if(Globals.EnforceMaxMoney && data.MaximumBalance > -1){
            if((data.PlayerBalance += amount) >= data.MaximumBalance){
                p.sendMessage(ChatColor.YELLOW + "Warning: Your bank account is full, only a certain portion of the banknote has been redeemed. (Funds not redeemed given as new banknote)");
                int oldamount = amount;
                amount = data.MaximumBalance - data.PlayerBalance;
                ItemStack banknote = Utility.getBankNote(oldamount - amount);
                if(Utility.hasAvaliableSlot(p)){
                    inv.addItem(banknote);
                    p.sendMessage(ChatColor.GRAY + "Withdrew " + Globals.CurrencySymbol + amount);
                }else{
                    //drop the item if the inventory is full
                    p.sendMessage(ChatColor.GRAY + "Inventory full! The item has been thrown on the ground.");
                    p.getLocation().getWorld().dropItem(p.getLocation(), banknote);
                }
            }
        }
        data.PlayerBalance += amount;
        PlayerDataHandler.SaveData(p);
        main.getAmount();
        //remove only one item if there is a stack
        main.setAmount(main.getAmount() - 1);
        p.sendMessage(ChatColor.GRAY + "Redeemed a bank note for " + Globals.CurrencySymbol + amount + ".");
        CurrencyPlugin.getInstance().log(Level.INFO, p.getName() + " redeemed a bank note for " + Globals.CurrencySymbol + amount + ".");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        String UUID = p.getUniqueId().toString();
        try {
            PlayerDataHandler.SaveAndRemoveData(UUID);
        } catch (Exception e) {
            PlayerDataHandler.DeleteData(UUID);
            String UUID2 = p.getUniqueId().toString();
            PlayerDataHandler.SaveAndRemoveData(UUID2);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player deadplayer = (Player) event.getEntity();
        if (event.getEntity().getKiller() == null) {
            handlePlayerNaturalDeath(deadplayer, event);
            return;
        }
        Player killer = (Player) event.getEntity().getKiller();
        PlayerData DeadData = PlayerDataHandler.GetData(deadplayer.getUniqueId().toString());
        PlayerData KillerData = PlayerDataHandler.GetData(killer.getUniqueId().toString());
        int penalty = 0;
        if (DeadData.PlayerBalance > 0) {
            penalty = (int) (DeadData.PlayerBalance * (Globals.DeductAmount / 100.0f));
        }
        if(Globals.EnforceMaxMoney && KillerData.MaximumBalance > -1){
            if((KillerData.PlayerBalance += penalty) >= KillerData.MaximumBalance){
                penalty = KillerData.MaximumBalance - KillerData.PlayerBalance;
                deadplayer.sendMessage(ChatColor.GRAY + "Your killers' bank account was to full to take the full penalty, so a smaller one was subtracted.");
                killer.sendMessage(ChatColor.YELLOW + "Warning: Your bank account is full! You did not collect the maximum amount of money you could from the player you just killed.");
            }
        }
        DeadData.PlayerBalance -= penalty;
        KillerData.PlayerBalance += penalty;
        PlayerDataHandler.SaveData(DeadData);
        PlayerDataHandler.SaveData(KillerData);
        deadplayer.sendMessage(ChatColor.GRAY + "You lost " + Globals.CurrencySymbol + penalty + " becuase you died to " + killer.getName());
        killer.sendMessage(ChatColor.GRAY + "You earned " + Globals.CurrencySymbol + penalty + " for killing " + deadplayer.getName());
    }
    public static void handlePlayerNaturalDeath(Player player, EntityDeathEvent event){
        Location deathlocation = event.getEntity().getLocation();
        EntityDamageEvent damage = event.getEntity().getLastDamageCause();
        DamageCause cause = damage.getCause();
        Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
        if(cause == DamageCause.VOID){
            int penalty = Utility.getPenaltyNaturalDeath(player);
            PlayerData data = PlayerDataHandler.GetData(player);
            data.PlayerBalance = data.PlayerBalance - penalty;
            PlayerDataHandler.SaveData(player);
            ItemStack item = Utility.getBankNote(penalty);
            Item droppeditem = Bukkit.getServer().getWorld("world").dropItem(spawn, item);
            droppeditem.setInvulnerable(true);
            droppeditem.setUnlimitedLifetime(true);
            player.sendMessage(ChatColor.GRAY + "You died and lost " + Globals.CurrencySymbol + penalty + ".");
            player.sendMessage(ChatColor.GRAY + "Your death bank note is at spawn of the overworld.");
            return;
        }
        int penalty = Utility.getPenaltyNaturalDeath(player);
        PlayerData data = PlayerDataHandler.GetData(player);
        data.PlayerBalance = data.PlayerBalance - penalty;
        PlayerDataHandler.SaveData(player);
        ItemStack item = Utility.getBankNote(penalty);
        Item droppeditem = Bukkit.getServer().getWorld("world").dropItem(deathlocation, item);
        droppeditem.setInvulnerable(true);
        droppeditem.setUnlimitedLifetime(true);
        player.sendMessage(ChatColor.GRAY + "You died and lost " + Globals.CurrencySymbol + penalty + ".");
        return;
    }
}
