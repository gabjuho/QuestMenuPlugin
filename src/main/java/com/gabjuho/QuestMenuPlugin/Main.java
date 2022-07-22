package com.gabjuho.QuestMenuPlugin;

import com.gabjuho.QuestMenuPlugin.commands.Command;
import com.gabjuho.QuestMenuPlugin.events.Event;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new Event(),this);

        getCommand("JunaraQuest").setExecutor(new Command());
        getCommand("tag").setExecutor(new Command());
        getCommand("quest").setExecutor(new Command());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+ "[QuestMenu]: QuestMenu is enabled");
    }
    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.RED+ "[QuestMenu]: QuestMenu is disabled");
    }
}
