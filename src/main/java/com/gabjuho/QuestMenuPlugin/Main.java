package com.gabjuho.QuestMenuPlugin;

import com.gabjuho.QuestMenuPlugin.commands.Command;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+ "[QuestMenu]: QuestMenu is enabled");
        getCommand("JunaraQuest").setExecutor(new Command());
    }
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED+ "[QuestMenu]: QuestMenu is disabled");
    }
}
