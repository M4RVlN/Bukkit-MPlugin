package com.m4rvln.mplugin.item;

import com.m4rvln.mplugin.BanSystem;
import com.m4rvln.mplugin.api.Executable;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Button;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.List;

public class ButtonItem implements Listener
{
    public static CraftItemStack get(ItemStack stack, String title, List<String> lore, Executable action)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag())
        {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null)
            tag = nmsStack.getTag();

        tag.setString("button", action.toString());
        nmsStack.setTag(tag);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsStack);
        meta.setDisplayName(title);
        if(lore != null)
            meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        CraftItemStack.setItemMeta(nmsStack, meta);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static CraftItemStack get(ItemStack stack, String title, List<String> lore, Executable action, boolean enchanted)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(get(stack, title, lore, action));
        if(enchanted)
        {
            ItemMeta meta = CraftItemStack.getItemMeta(nmsStack);
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);

            CraftItemStack.setItemMeta(nmsStack, meta);
        }
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    public static CraftItemStack get(ItemStack stack, String title, Executable action)
    {
        return get(stack, title, null, action);
    }

    public static boolean isButton(ItemStack stack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (nmsStack.hasTag())
        {
            NBTTagCompound tag = nmsStack.getTag();
            if (tag.hasKey("button"))
                return true;
        }
        return false;
    }

    public static Executable getAction(ItemStack stack)
    {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (nmsStack.hasTag())
        {
            NBTTagCompound tag = nmsStack.getTag();
            if (tag.hasKey("button"))
            {
                String action = tag.getString("button");
                return Executable.fromString(action);
            }
        }
        return null;
    }

//Events ------------------------------------------

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
