package com.m4rvln.mplugin.item;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class FakeItem
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
}
