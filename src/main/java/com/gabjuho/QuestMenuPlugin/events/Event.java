package com.gabjuho.QuestMenuPlugin.events;

import com.gabjuho.QuestMenuPlugin.ConfigManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.PlayerConversationEndEvent;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.ObjectNotFoundException;
import org.betonquest.betonquest.id.ConditionID;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event implements Listener {
    @EventHandler
    void onChatEvent(AsyncChatEvent event) throws ObjectNotFoundException {
        Player player = event.getPlayer();
        String message = event.message().toString();
        message = message.toLowerCase();

        if (message.contains(":tutorial:")) {
            ConditionID conditionID = new ConditionID(Config.getPackages().get("WildQuest"), "has_tutorial_complete");
            if (!BetonQuest.condition(player.getUniqueId().toString(), conditionID)) {
                event.getPlayer().sendMessage(ChatColor.RED + "칭호를 소유하고 있지않아 해당 문구는 사용 불가능합니다. -> : t u t o r i a l :");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onTutorialStart(PlayerConversationEndEvent event) throws ObjectNotFoundException {
        Player player = event.getPlayer();

        if (event.getConversation().getID().equals("WildQuest.meina")) {
            ConditionID conditionID1 = new ConditionID(Config.getPackages().get("WildQuest"), "has_tutorial_complete"); //튜토리얼을 스킵했는지
            ConditionID conditionID2 = new ConditionID(Config.getPackages().get("WildQuest"), "has_tutorial_start"); //튜토리얼을 start했는지
            if (BetonQuest.condition(player.getUniqueId().toString(), conditionID1) || BetonQuest.condition(player.getUniqueId().toString(), conditionID2)) {// 둘 중 하나라도 했다면
                User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());//포탈이 열려야함 (럭펌으로 권한 부여해주기)
                if (user == null) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "onTutorialStart 이벤트 변수 user가 null입니다.");
                    return;
                }
                if(!user.getNodes().contains(Node.builder("multiverse.portal.access.worldtowild").build())) { //만약에 퀘스트 시작한게 처음이라면
                    user.data().add(Node.builder("multiverse.portal.access.worldtowild").build()); //포탈 권한들을 준다.
                    user.data().add(Node.builder("multiverse.portal.access.wildtoworld").build());
                    user.data().add(Node.builder("multiverse.access.world").build());
                    user.data().add(Node.builder("multiverse.access.wild").build());
                    user.data().add(Node.builder("multiverse.access.wild_nether").build());
                    user.data().add(Node.builder("multiverse.access.wild_end").build());

                    LuckPermsProvider.get().getUserManager().saveUser(user);

                    player.sendMessage(ChatColor.AQUA + "야생으로 갈 수 있는 포탈이 열렸습니다."); //처음 시작한거면, 메시지도 처음 한 번만 보내고 다음부터는 안보낸다.
                }
            }
        }
    }

    @EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!ConfigManager.getInstance().getConfig().contains(player.getUniqueId().toString())) {
            if (player.getWorld().getName().equals("wild") || player.getWorld().getName().equals("wild_end") || player.getWorld().getName().equals("wild_nether")) {
                World world = WorldCreator.name("world").createWorld();
                Location loc = new Location(world, -9, 67, -6);
                player.teleport(loc);
                player.sendMessage(ChatColor.GREEN + "업데이트 공지에 의해 로비로 강제 이동됩니다.");
            }
            ConfigManager.getInstance().getConfig().set(player.getUniqueId().toString(), true);
            ConfigManager.getInstance().saveConfig();
        }
    }
}
