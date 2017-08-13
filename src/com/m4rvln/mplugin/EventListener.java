package com.m4rvln.mplugin;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EventListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidSkeletonRiderSpawn(CreatureSpawnEvent event)
    {
        if(Main.instance.getConfig().getBoolean("spawnhorserider")) return;

        if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.LIGHTNING)
            if(event.getEntity().getType() == EntityType.SKELETON_HORSE || event.getEntity().getType() == EntityType.SKELETON)
                event.setCancelled(true);
    }
}
