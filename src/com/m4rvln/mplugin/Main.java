package com.m4rvln.mplugin;

import com.m4rvln.mplugin.gamemode.GamemodeObserve;
import com.m4rvln.mplugin.item.ButtonItemEvents;
import com.m4rvln.mplugin.item.Showcase;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;

public class Main extends JavaPlugin
{
    public static Main instance;

    private File banSystemConfigFile = new File(getDataFolder(), "banSystem.yml");

    @Override
    public void onEnable()
    {
        instance = this;

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new EventListener(), this);
        pm.registerEvents(new Showcase(), this);
        pm.registerEvents(new GamemodeObserve(), this);
        pm.registerEvents(new ButtonItemEvents(), this);

        InitializeCmds();

        getLogger().info("Enabled");
    }

    @Override
    public void onDisable()
    {
        getLogger().info("Disabled");
    }

    private void InitializeCmds()
    {
        CmdExecutor executor = new CmdExecutor();

        for (Commands cmd : Commands.values())
        {
            getCommand(cmd.name()).setExecutor(executor);
            getCommand(cmd.name()).setTabCompleter(executor);
        }
    }

    public static Player getPlayerByName(String name)
    {
        for(Player player : Bukkit.getOnlinePlayers())
            if(player.getName().toLowerCase().equals(name.toLowerCase()))
                return player;
        return null;
    }

    public FileConfiguration getCustomConfig(String name)
    {
        FileConfiguration config = null;

        switch (name)
        {
            case "banSystem":
                config = YamlConfiguration.loadConfiguration(banSystemConfigFile);
                break;
        }

        return config;
    }

    public void saveCustomConfig(String name)
    {
        File file = null;

        switch (name)
        {
            case "banSystem":
                file = banSystemConfigFile;
        }

        if (file == null)
            return;

        try
        {
            getCustomConfig(name).save(file);
        }
        catch (IOException ex)
        {
            getLogger().log(Level.SEVERE, "Could not save config to " + file, ex);
        }
    }
}
