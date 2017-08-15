package com.m4rvln.mplugin.item;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FakeItem implements Listener
{
    public static ItemStack CreateFakeItem(ItemStack itemStack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag())
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null)
            tag = nmsStack.getTag();
        tag.setBoolean("fake", true);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static ItemStack CreateShowcaseItem(ItemStack itemStack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag())
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null)
            tag = nmsStack.getTag();
        tag.setBoolean("fake", true);
        tag.setBoolean("showcase", true);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static boolean isFake(ItemStack itemStack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsStack.hasTag())
        {
            NBTTagCompound tag = nmsStack.getTag();
            if (tag.hasKey("fake"))
                return true;
        }
        return false;
    }

    public static boolean isShowcase(ItemStack itemStack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsStack.hasTag())
        {
            NBTTagCompound tag = nmsStack.getTag();
            if (tag.hasKey("showcase") && tag.hasKey("fake"))
                return true;
        }
        return false;
    }

//Events ------------------------------------------

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidHopperPickup(InventoryPickupItemEvent event)
    {
        if(FakeItem.isFake(event.getItem().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidPlayerFishingItem(PlayerFishEvent event)
    {
        if(event.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY))
            if(event.getCaught() instanceof Item)
                if(FakeItem.isFake(((Item)event.getCaught()).getItemStack()))
                    event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidFakeItemPickup(EntityPickupItemEvent event)
    {
        if(FakeItem.isFake(event.getItem().getItemStack()))
            event.setCancelled(true);
    }
}
