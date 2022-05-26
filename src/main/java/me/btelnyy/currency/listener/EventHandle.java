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
            PlayerDataHandler.GetPlayerData(UUID);
        } catch (Exception e) {
            PlayerDataHandler.DeleteData(UUID);
            CurrencyPlugin.log(Level.WARNING, "Failed loading " + UUID + "'s" + "player data. ");
            e.printStackTrace();
            player.kickPlayer(ChatColor.RED + "Your playerdata failed to load, try rejoining");
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
        PlayerData data = PlayerDataHandler.GetPlayerData(p);
        //Inventory inv = p.getInventory();
        data.PlayerBalance += amount;
        PlayerDataHandler.SaveData(p);
        main.getAmount();
        //remove only one item if there is a stack
        main.setAmount(main.getAmount() - 1);
        p.sendMessage(ChatColor.GRAY + "Redeemed a bank note for " + Globals.CurrencySymbol + amount + ".");
        CurrencyPlugin.log(Level.INFO, p.getName() + " redeemed a bank note for " + Globals.CurrencySymbol + amount + ".");
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
        PlayerData DeadData = PlayerDataHandler.GetPlayerData(deadplayer.getUniqueId().toString());
        PlayerData KillerData = PlayerDataHandler.GetPlayerData(killer.getUniqueId().toString());
        int penalty = 0;
        if (DeadData.PlayerBalance > 0) {
            penalty = (int) (DeadData.PlayerBalance * (Globals.DeductAmount / 100.0f));
        }
        DeadData.PlayerBalance -= penalty;
        KillerData.PlayerBalance += penalty;
        PlayerDataHandler.SaveData(DeadData.PlayerUuid);
        PlayerDataHandler.SaveData(KillerData.PlayerUuid);
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
            PlayerData data = PlayerDataHandler.GetPlayerData(player);
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
        PlayerData data = PlayerDataHandler.GetPlayerData(player);
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
