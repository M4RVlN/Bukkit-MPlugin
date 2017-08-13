package com.m4rvln.mplugin.gamemode;

import net.minecraft.server.v1_12_R1.EntityExperienceOrb;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class GamemodeObserve implements Listener
{
    private static List<UUID> hiddenPlayers = new ArrayList<>();

    public static boolean isHidden(Player p)
    {
        return hiddenPlayers.contains(p.getUniqueId());
    }

    public static void HidePlayer(Player p)
    {
        if(!hiddenPlayers.contains(p.getUniqueId()))
            hiddenPlayers.add(p.getUniqueId());

        for(Player on : Bukkit.getOnlinePlayers())
        {
            if(isHidden(on)) continue;
            on.hidePlayer(p);
        }
        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(true);
        p.setFlying(true);
    }

    public static void ShowPlayer(Player p)
    {
        if(!hiddenPlayers.contains(p.getUniqueId())) return;

        hiddenPlayers.remove(p.getUniqueId());
        for(Player on : Bukkit.getOnlinePlayers())
        {
            on.showPlayer(p);
        }
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    public static List<UUID> getHiddenPlayers()
    {
        return hiddenPlayers;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidItemPickUp(EntityPickupItemEvent event)
    {
        if(event.getEntity() instanceof Player)
            if(isHidden((Player) event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof Player)
            if(isHidden((Player) event.getRightClicked()))
                event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidEntityTargeting(EntityTargetEvent event) {
        if(event.getTarget() instanceof Player)
            if(isHidden((Player) event.getTarget()))
                event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidEntityDamage(EntityDamageByEntityEvent event)
    {
        if(event.getEntity() instanceof Player)
            if(isHidden((Player) event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player)
            if(isHidden((Player) event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnJoin(PlayerJoinEvent event)
    {
        if(isHidden(event.getPlayer()))
        {
            event.setJoinMessage("");
            event.getPlayer().sendMessage("You are in Observer Mode!");
        }
        else
        {
            for(UUID id : hiddenPlayers)
            {
                Player p = Bukkit.getPlayer(id);
                event.getPlayer().hidePlayer(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnQuit(PlayerQuitEvent event)
    {
        if(isHidden(event.getPlayer()))
            event.setQuitMessage("");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidPlayerInServerList(ServerListPingEvent event)
    {
        if (event == null) return;

        Iterator players;
        players = event.iterator();

        while(players.hasNext())
            if (isHidden((Player)players.next()))
                players.remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidVehicleCollsion(VehicleEntityCollisionEvent event)
    {
        if(event.getEntity() instanceof Player)
            if(isHidden((Player) event.getEntity()))
                event.setCancelled(true);
    }

    @EventHandler
    public void AvoidChatting(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if(isHidden(player))
        {
            String msg = event.getMessage();
            if(msg.contains(player.getDisplayName()))
                event.setMessage(msg.replace(player.getDisplayName(), ""));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ReadChestsSilent(InventoryOpenEvent event)
    {
        if(event.getPlayer() instanceof Player)
        {
            if (isHidden((Player) event.getPlayer()))
            {
                if (event.getInventory() instanceof Chest)
                {
                    Inventory inventory = event.getInventory();
                    event.getPlayer().openInventory(inventory);
                    event.setCancelled(true);
                }
            }
        }
    }
}
