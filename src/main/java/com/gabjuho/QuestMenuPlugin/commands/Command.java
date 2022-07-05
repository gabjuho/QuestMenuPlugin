package com.gabjuho.QuestMenuPlugin.commands;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.config.QuestPackage;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.EventID;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("오직 플레이어만 명령어를 사용할 수 있습니다.");
            return true;
        }
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("JunaraQuest")){
            QuestPackage myPackage = Config.getPackages().get("WildQuest");
            try {
                EventID eventID = new EventID(myPackage, "talk_with_meina");
                BetonQuest.event(player.getUniqueId().toString(),eventID);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
