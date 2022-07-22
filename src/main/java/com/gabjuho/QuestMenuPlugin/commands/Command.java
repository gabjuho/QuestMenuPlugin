package com.gabjuho.QuestMenuPlugin.commands;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.config.QuestPackage;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.ConditionID;
import org.betonquest.betonquest.id.EventID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("오직 플레이어만 명령어를 사용할 수 있습니다.");
            return true;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("JunaraQuest")) {
            QuestPackage myPackage = Config.getPackages().get("WildQuest");
            try {
                EventID eventID = new EventID(myPackage, "talk_with_meina");
                BetonQuest.event(player.getUniqueId().toString(), eventID);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (cmd.getName().equalsIgnoreCase("tag")) { //칭호 온오프 명령어
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "칭호 명령어 형식: tag [on/off] (on 칭호켜기/off 칭호끄기)");
                return true;
            }
            if (args[0].equals("on")) {
                User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
                if (user == null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "on 명령어 user가 null입니다.");
                    return false;
                }
                try {
                    ConditionID conditionID = new ConditionID(Config.getPackages().get("WildQuest"), "has_tutorial_complete");
                    if (!BetonQuest.condition(player.getUniqueId().toString(), conditionID)) {
                        player.sendMessage("보유하고 있는 칭호가 없습니다.");
                        return true;
                    }
                    user.data().add(Node.builder("group.tutorial").build()); // tutorial 추가
                    user.data().remove(Node.builder("group.default").build()); //default 삭제
                    LuckPermsProvider.get().getUserManager().saveUser(user);
                    player.sendMessage(ChatColor.GREEN + "칭호가 켜졌습니다.");
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equals("off")) {
                User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
                if (user == null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "off 명령어 user가 null입니다.");
                    return false;
                }
                try {
                    ConditionID conditionID = new ConditionID(Config.getPackages().get("WildQuest"), "has_tutorial_complete"); //check if tutorial is completed
                    if (!BetonQuest.condition(player.getUniqueId().toString(), conditionID)) {
                        player.sendMessage("보유하고 있는 칭호가 없습니다.");
                        return true;
                    }
                    user.data().add(Node.builder("group.default").build()); // tutorial 추가
                    user.data().remove(Node.builder("group.tutorial").build()); //default 삭제
                    LuckPermsProvider.get().getUserManager().saveUser(user);
                    player.sendMessage(ChatColor.RED + "칭호가 꺼졌습니다.");
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(ChatColor.RED + "칭호 명령어 형식:tag [on/off] \n(on 칭호켜기/off 칭호끄기)");
            }
        }
        if (cmd.getName().equalsIgnoreCase("quest")) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "명령어 형식: quest [on/off]\n(on 퀘스트 알림 켜기/off 퀘스트 알림 끄기)");
                return true;
            }
            if (args[0].equals("on")) {
                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                Component component = Component.text(ChatColor.GREEN + "퀘스트 이름");

                Objective objective = scoreboard.registerNewObjective("test", "dummy", component);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                Score score = objective.getScore(ChatColor.GRAY + "좀비 처치 수:");
                score.setScore(1);

                player.setScoreboard(scoreboard);
            } else if (args[0].equals("off")) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            } else {
                player.sendMessage(ChatColor.RED + "명령어 형식: quest [on/off]\n(on 퀘스트 알림 켜기/off 퀘스트 알림 끄기)");
            }
        }
        return true;
    }
}
