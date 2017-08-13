package com.m4rvln.mplugin.debug;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Debug
{
    public static void SendPlayerDebugMessage(Player player, String message)
    {
        player.sendMessage(ChatColor.DARK_RED + "MPlugin:" + message);
    }
}
