package com.m4rvln.mplugin;

import com.m4rvln.mplugin.gamemode.GamemodeObserve;
import com.m4rvln.mplugin.item.FakeItem;
import com.m4rvln.mplugin.item.Showcase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CmdExecutor implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            switch (Commands.valueOf(cmd.getName().toLowerCase()))
            {
                default:
                    return false;
                case mplugin:
                    player.sendMessage(ChatColor.GOLD + Main.instance.getDescription().getFullName() + ": " + Main.instance.getDescription().getDescription());
                    return true;
                case showcase:
                    int amount = 1;
                    short data = 0;

                    if(args.length >= 2)
                    {
                        if(StringUtils.isNumeric(args[1]))
                            data = Short.parseShort(args[1]);
                    }
                    if(args.length == 3)
                    {
                        if(StringUtils.isNumeric(args[2]))
                            amount = Integer.parseInt(args[2]);
                        if(amount <= 0)
                            amount = 1;
                    }
                    if(args.length >= 1)
                    {
                        ItemStack item;
                        switch (args[0].toLowerCase())
                        {
                            default:
                                return false;
                            case "stick":
                                item = Showcase.GetShowcaseItem(Material.STICK, amount);
                                break;
                            case "stone":
                                if(data > 15 || data < 0)
                                    data = 0;
                                item = Showcase.GetShowcaseItem(Material.STEP, amount, data);
                                break;
                            case "wood":
                                if(data > 7 || data < 0)
                                    data = 0;
                                item = Showcase.GetShowcaseItem(Material.WOOD_STEP, amount, data);
                                break;
                        }
                        player.getInventory().addItem(item);
                        return true;
                    }
                    return false;
                case togglehorserider:
                    Main.instance.getConfig().set("spawnhorserider", !Main.instance.getConfig().getBoolean("spawnhorserider"));
                    Main.instance.saveConfig();
                    sender.sendMessage("Horse Rider spawning: " + Main.instance.getConfig().getBoolean("spawnhorserider"));
                    return true;
                case fakeitem:
                    if (args.length == 2)
                    {
                        if (args[0].equals("remove"))
                        {
                            if (StringUtils.isNumeric(args[1]))
                            {
                                int count = 0;
                                int radius = Integer.parseInt(args[1]);
                                Entity tmp = player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);

                                List<Entity> entities = tmp.getNearbyEntities(radius, radius, radius);
                                tmp.remove();

                                for (Entity entity : entities)
                                {
                                    if (entity instanceof Item)
                                    {
                                        if (FakeItem.isFake(((Item) entity).getItemStack()))
                                        {
                                            entity.remove();
                                            count++;
                                        }
                                    }
                                }
                                sender.sendMessage("Cleared " + count + " fake item(s)!");
                                return true;
                            }
                        }
                        sender.sendMessage(ChatColor.DARK_RED +  "/fakeitem remove <radius>");
                    }
                    return false;
                case tempban:
                    if(args.length == 1)
                    {
                        Player pToBan = Main.getPlayerByName(args[0]);
                        String name = args[0];
                        if(pToBan != null)
                            name = pToBan.getName();
                        player.openInventory(BanSystem.getGUI(player, name));
                    }
                    return true;
                case observe:
                    if(GamemodeObserve.isHidden(player))
                        GamemodeObserve.HidePlayer(player);
                    else
                        GamemodeObserve.ShowPlayer(player);
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> result = new ArrayList<>();

        if(sender instanceof Player)
        {
            Player player = (Player)sender;
            switch (Commands.valueOf(cmd.getName().toLowerCase()))
            {
                default:
                    break;
                case fakeitem:
                    if (args.length == 1)
                        if("remove".startsWith(args[0].toLowerCase()))
                            result.add("remove");
                    break;
                case tempban:
                    if (args.length == 1)
                        for (Player p : Bukkit.getOnlinePlayers())
                            result.add(p.getName());
                    break;
            }
        }
        return result;
    }
}
