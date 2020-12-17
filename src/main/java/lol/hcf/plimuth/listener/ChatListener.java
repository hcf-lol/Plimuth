package lol.hcf.plimuth.listener;

import lol.hcf.plimuth.player.PlayerData;
import lol.hcf.plimuth.plugin.config.PluginConfiguration;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final MessageConfiguration config;

    public ChatListener(PluginConfiguration config) {
        this.config = config.getMessageConfiguration();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        PlayerData data = PlayerData.valueOf(event.getPlayer());
        event.setFormat(this.config.chatFormat);

        if (data.getRank() != null && data.getRank().getPrefix() != null) {
            event.setFormat(String.format(this.config.chatFormat, data.getRank().getPrefix()) + event.getFormat());
        }
    }

}
