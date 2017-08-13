package com.m4rvln.mplugin.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonItemEvents implements Listener
{
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        if(ButtonItem.isButton(event.getCurrentItem()))
        {
            try
            {
                ButtonItem.getAction(event.getCurrentItem()).execute();
            }
            catch (Exception ex)
            {
                System.out.println("Error while trying to execute a ButtonItems action!");
            }
            event.setCancelled(true);
        }
    }
}
