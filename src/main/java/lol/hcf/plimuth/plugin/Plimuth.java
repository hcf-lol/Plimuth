package lol.hcf.plimuth.plugin;

import lol.hcf.foundation.database.ConnectionHandler;
import lol.hcf.foundation.plugin.Foundation;
import lol.hcf.plimuth.command.rank.RankCommand;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;
import lol.hcf.plimuth.rank.RankManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Plimuth extends JavaPlugin implements PluginConfiguration {

    private RankManager rankManager;
    private MessageConfiguration messageConfiguration;

    @Override
    public void onEnable() {
        this.messageConfiguration = new MessageConfiguration(new File(this.getDataFolder(), "messages.yml"));
        this.rankManager = new RankManager(new File(this.getDataFolder(), "data/ranks.json"));

        ConnectionHandler handler = Foundation.getConnectionHandler();

        new RankCommand(this, this.rankManager).get().setCommand(this.getCommand("rank"));

        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.rankManager.save();
        super.onDisable();
    }

    @Override
    public MessageConfiguration getMessageConfiguration() {
        return this.messageConfiguration;
    }
}
