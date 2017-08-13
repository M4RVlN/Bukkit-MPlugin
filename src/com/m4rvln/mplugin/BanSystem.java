package com.m4rvln.mplugin;

import com.m4rvln.mplugin.api.Executable;
import com.m4rvln.mplugin.item.ButtonItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BanSystem
{
    public static Inventory getGUI(Player sender, String pToBan)
    {
        FileConfiguration config = Main.instance.getConfig();
        String[] duration = new String[]
        {
            config.getString("tempban_xray"),
            config.getString("tempban_fly"),
            config.getString("tempban_pvp"),
            config.getString("tempban_exploit"),
            config.getString("tempban_grief"),
            config.getString("tempban_racism"),
            config.getString("tempban_disrespect"),
            config.getString("tempban_advertisement"),
            config.getString("tempban_minor"),
            config.getString("tempban_custom1"),
            config.getString("tempban_custom2"),
            config.getString("tempban_custom3"),
            config.getString("tempban_custom4"),
            config.getString("tempban_custom5"),
            config.getString("tempban_custom6"),
            config.getString("tempban_custom7")
        };

        Inventory inv = Bukkit.createInventory(sender, 18, "Ban: " + pToBan);
        inv.addItem(ButtonItem.get(new ItemStack(Material.DIAMOND_ORE),                 "XRay-Hacking",                        Arrays.asList(BanSystem.shortToPrettyString(duration[0])), new Executable(BanSystem.class, "Ban", pToBan, duration[0], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.FEATHER),                     "Fly/Speed-Hacking",                   Arrays.asList(BanSystem.shortToPrettyString(duration[1])), new Executable(BanSystem.class, "Ban", pToBan, duration[1], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.DIAMOND_SWORD),               "PvP/Kill Aura-Hacking",               Arrays.asList(BanSystem.shortToPrettyString(duration[2])), new Executable(BanSystem.class, "Ban", pToBan, duration[2], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.PISTON_BASE),                 "Exploiting",                          Arrays.asList(BanSystem.shortToPrettyString(duration[3])), new Executable(BanSystem.class, "Ban", pToBan, duration[3], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.TNT),                         "Griefing",                            Arrays.asList(BanSystem.shortToPrettyString(duration[4])), new Executable(BanSystem.class, "Ban", pToBan, duration[4], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.BOOK_AND_QUILL),              "Racism/Sexism/Threats/Harassment",    Arrays.asList(BanSystem.shortToPrettyString(duration[5])), new Executable(BanSystem.class, "Ban", pToBan, duration[5], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.TOTEM),                       "Staff Impersonation/Disrespect",      Arrays.asList(BanSystem.shortToPrettyString(duration[6])), new Executable(BanSystem.class, "Ban", pToBan, duration[6], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.EMPTY_MAP),                   "Advertising",                         Arrays.asList(BanSystem.shortToPrettyString(duration[7])), new Executable(BanSystem.class, "Ban", pToBan, duration[7], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.GOLD_NUGGET),                 "Minor Offense",                       Arrays.asList(BanSystem.shortToPrettyString(duration[8])), new Executable(BanSystem.class, "Ban", pToBan, duration[8], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 0),  duration[9],                                                                                      new Executable(BanSystem.class, "Ban", pToBan, duration[9], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 1),  duration[10],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[10], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 2),  duration[11],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[11], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 3),  duration[12],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[12], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 4),  duration[13],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[13], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 5),  duration[14],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[14], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.STAINED_CLAY, 1, (short) 6),  duration[15],                                                                                     new Executable(BanSystem.class, "Ban", pToBan, duration[15], "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.PAPER),                       "Custom Ban",                                                                                     new Executable(BanSystem.class, "Ban", pToBan, "1m", "Hacking")));
        inv.addItem(ButtonItem.get(new ItemStack(Material.BARRIER),                     "Perm Ban",                                                                                       new Executable(BanSystem.class, "Ban", pToBan, "", "Hacking")));
        return inv;
    }

    public static Duration getDurationFromString(String s)
    {
        if(s.endsWith("s"))
        {
            s = s.replace("s", "");
            if(StringUtils.isNumeric(s))
                return Duration.ofSeconds(Long.parseLong(s));
        }
        if(s.endsWith("m"))
        {
            s = s.replace("m", "");
            if(StringUtils.isNumeric(s))
                return Duration.ofMinutes(Long.parseLong(s));
        }
        if(s.endsWith("h"))
        {
            s = s.replace("h", "");
            if(StringUtils.isNumeric(s))
                return Duration.ofHours(Long.parseLong(s));
        }
        if(s.endsWith("d"))
        {
            s = s.replace("d", "");
            if(StringUtils.isNumeric(s))
                return Duration.ofDays(Long.parseLong(s));
        }
        return null;
    }

    public static String shortToPrettyString(String s)
    {
        String end;
        if(s.endsWith("s"))
        {
            s = s.replace("s", "");
            end = " Second";
        }
        else if(s.endsWith("m"))
        {
            s = s.replace("m", "");
            end = " Minute";
        }
        else if(s.endsWith("h"))
        {
            s = s.replace("h", "");
            end = " Hour";
        }
        else if(s.endsWith("d"))
        {
            s = s.replace("d", "");
            end = " Day";
        }
        else
            return "";

        if(StringUtils.isNumeric(s))
        {
            if (Integer.parseInt(s) != 1)
                end += "s";

            s += end;
            return s;
        }
        else
            return "";
    }

    public static void Ban(String player, String duration, String reason)
    {
        Ban(player, getDurationFromString(duration), reason);
    }

    public static void Ban(String player, Duration duration, String reason)
    {
        Date date = new Date();
        if(duration == null)
            date = null;
        else
            date = Date.from(date.toInstant().plus(duration));

        Player p = Main.getPlayerByName(player);
        String message = String.format(Main.instance.getConfig().getString("ban_message"), reason, date);

        Bukkit.getBanList(BanList.Type.NAME).addBan(player, reason, date, null);
        Bukkit.getBanList(BanList.Type.IP).addBan(player, reason, date, null);
        if(p != null)
        {
            if(message == null)
                message = "You are banned!";
            p.kickPlayer(message);
        }
    }

    public static void Unban(String player)
    {
        Bukkit.getBanList(BanList.Type.NAME).pardon(player);
    }
}
