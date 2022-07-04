package com.gabjuho.QuestMenuPlugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+ "[System]: QuestMenu is enabled");
    }
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED+ "[System]: QuestMenu is disabled");
    }
}
