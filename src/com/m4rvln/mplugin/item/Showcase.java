package com.m4rvln.mplugin.item;

import com.m4rvln.mplugin.Main;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Showcase implements Listener
{
    public static CraftItemStack GetShowcaseItem(Material material,  int amount, short metavalue)
    {
        if(material == Material.STICK)
        {
            ItemStack item = new ItemStack(Material.STICK);
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();

            lore.add("§r§9[Right Click] Make slab showcase");
            lore.add("§r§9[Shift + Right Click] Remove showcase from slab");
            meta.setDisplayName("§2§lShowcase Stick");
            meta.setLore(lore);
            item.setItemMeta(meta);

            return CraftItemStack.asCraftCopy(item);
        }
        else if(material == Material.STEP || material == Material.WOOD_STEP)
        {
            net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(Material.STEP, amount, metavalue));
            NBTTagCompound tag = null;
            if (!nmsStack.hasTag())
            {
                tag = new NBTTagCompound();
                nmsStack.setTag(tag);
            }
            if (tag == null)
                tag = nmsStack.getTag();
            tag.setBoolean("showcase", true);
            nmsStack.setTag(tag);

            ItemMeta meta = CraftItemStack.getItemMeta(nmsStack);
            meta.setDisplayName("§6Showcase");
            CraftItemStack.setItemMeta(nmsStack, meta);

            return CraftItemStack.asCraftMirror(nmsStack);
        }
        return CraftItemStack.asCraftCopy(new ItemStack(Material.AIR));
    }

    public static ItemStack GetShowcaseItem(Material material,  int amount)
    {
        return GetShowcaseItem(material, amount, (byte)0);
    }

    public static ItemStack GetShowcaseItem(ItemStack itemStack)
    {
        return GetShowcaseItem(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability());
    }

    @EventHandler
    public void MakeShowcase(PlayerInteractEvent event)
    {
        if(event.getHand() == EquipmentSlot.HAND)
        {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
                Block block = event.getClickedBlock();
                if (itemStack.getType() == Material.AIR) return;
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName().equals("§2§lShowcase Stick"))
                {
                    if (block.getType() == Material.STEP || block.getType() == Material.WOOD_STEP)
                    {
                        if (!block.hasMetadata("showcase"))
                            block.setMetadata("showcase", new FixedMetadataValue(Main.instance, new ItemStack(Material.AIR)));
                        else if(event.getPlayer().isSneaking())
                            RemoveShowcaseTag(block, event.getPlayer().getWorld());
                    }
                }
                else if (block.hasMetadata("showcase"))
                {
                    if (((ItemStack) block.getMetadata("showcase").get(0).value()).getType() == Material.AIR)
                    {
                        Location location = block.getLocation().add(0.5D, 0.5D, 0.5D);
                        ItemStack fakeItem = FakeItem.CreateShowcaseItem(new ItemStack(itemStack.getType(), 1, itemStack.getDurability()));
                        Item i = event.getPlayer().getWorld().dropItem(location, fakeItem);

                        itemStack.setAmount(itemStack.getAmount() - 1);
                        block.setMetadata("showcase", new FixedMetadataValue(Main.instance, i.getItemStack()));
                        i.setVelocity(new Vector(0, 0, 0));
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    private void RemoveShowcaseTag(Block block, World world)
    {
        if (block.hasMetadata("showcase"))
        {
            ItemStack slot = (ItemStack)block.getMetadata("showcase").get(0).value();

            if (slot.getType() != Material.AIR)
            {
                world.dropItemNaturally(block.getLocation().add(0.5D, 0.5D, 0.5D), new ItemStack(slot.getType(), slot.getAmount(), slot.getDurability()));
                slot.setAmount(0);
            }
            block.removeMetadata("showcase", Main.instance);
        }
    }

//Events ------------------------------------------

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnBlockPlace(BlockPlaceEvent event)
    {
        if(event.getBlock().hasMetadata("showcase"))
        {
            event.setCancelled(true);
            return;
        }

        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(event.getItemInHand());
        if (nmsStack.hasTag())
        {
            NBTTagCompound tag = nmsStack.getTag();
            if (tag.hasKey("showcase"))
                event.getBlockPlaced().setMetadata("showcase", new FixedMetadataValue(Main.instance, new ItemStack(Material.AIR)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnShowcaseBreak(BlockBreakEvent event)
    {
        Block block = event.getBlock();
        if (block.hasMetadata("showcase"))
        {
            RemoveShowcaseTag(event.getBlock(), event.getPlayer().getWorld());

            ItemStack[] blocks = new ItemStack[block.getDrops().size()];
            blocks = block.getDrops().toArray(blocks);

            event.setDropItems(false);
            event.getPlayer().getWorld().dropItemNaturally(block.getLocation(), GetShowcaseItem(blocks[0]));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BreakShowcaseOnExplosion(EntityExplodeEvent event)
    {
        for (Block block : event.blockList())
        {
            if(block.hasMetadata("showcase"))
            {
                RemoveShowcaseTag(block, event.getLocation().getWorld());

                ItemStack[] blocks = new ItemStack[block.getDrops().size()];
                blocks = block.getDrops().toArray(blocks);

                event.getLocation().getWorld().dropItemNaturally(block.getLocation(), GetShowcaseItem(blocks[0]));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidShowcaseItemDespawn(ItemDespawnEvent event)
    {
        if(FakeItem.isShowcase(event.getEntity().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidPistionPushShowcase(BlockPistonExtendEvent event)
    {
        for (Block block : event.getBlocks())
        {
            if(block.hasMetadata("showcase"))
                event.setCancelled(true);
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void AvoidPistionPullShowcase(BlockPistonRetractEvent event)
    {
        for (Block block : event.getBlocks())
        {
            if(block.hasMetadata("showcase"))
                event.setCancelled(true);
        }
    }
}
