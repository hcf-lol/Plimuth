package lol.hcf.plimuth.listener;

import lol.hcf.plimuth.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        PlayerData.PLAYERS.remove(PlayerData.valueOf(event.getPlayer()));
    }

}
