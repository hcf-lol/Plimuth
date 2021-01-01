package lol.hcf.plimuth.plugin;

import lol.hcf.foundation.database.ConnectionHandler;
import lol.hcf.foundation.plugin.Foundation;
import lol.hcf.plimuth.command.rank.RankCommand;
import lol.hcf.plimuth.listener.ChatListener;
import lol.hcf.plimuth.listener.JoinListener;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;
import lol.hcf.plimuth.plugin.config.PluginConfiguration;
import lol.hcf.plimuth.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Plimuth extends JavaPlugin implements PluginConfiguration {

    private RankManager rankManager;
    private MessageConfiguration messageConfiguration;

    @Override
    public void onEnable() {
        this.messageConfiguration = new MessageConfiguration(new File(this.getDataFolder(), "messages.yml"));
        this.rankManager = new RankManager(Foundation.getConnectionHandler(), new File(this.getDataFolder(), "cache/ranks"));

        ConnectionHandler handler = Foundation.getConnectionHandler();

        Bukkit.getPluginManager().registerEvents(new JoinListener(this.rankManager, handler), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        new RankCommand(this, this.rankManager).get().setCommand(this.getCommand("rank"));

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public MessageConfiguration getMessageConfiguration() {
        return this.messageConfiguration;
    }
}
