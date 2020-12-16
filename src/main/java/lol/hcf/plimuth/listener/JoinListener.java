package lol.hcf.plimuth.listener;

import lol.hcf.plimuth.extension.PlayerDataContainer;
import lol.hcf.plimuth.extension.PlayerExtension;
import lol.hcf.plimuth.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerDataContainer container = ((PlayerExtension) event.getPlayer()).getPlayerData();
        container.set(new PlayerData());
    }

}
