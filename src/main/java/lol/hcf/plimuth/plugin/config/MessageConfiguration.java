package lol.hcf.plimuth.plugin.config;

import lol.hcf.foundation.data.impl.yml.ConfigurationFile;
import org.bukkit.ChatColor;

import java.io.File;

public class MessageConfiguration extends ConfigurationFile {

    public final String rankTag = ChatColor.GRAY.toString() + ChatColor.BOLD + "[%s" + ChatColor.RESET + ChatColor.GRAY + ChatColor.BOLD + "] ";
    public final String chatFormat = "%s: %s";
    public final String rankExistsError = ChatColor.RED + "A rank with the name %s already exists.";
    public final String rankCreatedMessage = ChatColor.GREEN + "Created new rank %s.";

    public final String rankSyncedMessage = ChatColor.GREEN + "Synced ranks with database.";
    public final String rankFetchedMessage = ChatColor.GREEN + "Fetched ranks from database.";

    public MessageConfiguration(File configFile) {
        super(configFile);
        super.load();
    }

    private MessageConfiguration() {
        super(null);
    }
}
