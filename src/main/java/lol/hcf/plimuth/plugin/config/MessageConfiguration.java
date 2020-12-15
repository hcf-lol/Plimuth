package lol.hcf.plimuth.plugin.config;

import lol.hcf.foundation.data.impl.yml.ConfigurationFile;
import org.bukkit.ChatColor;

import java.io.File;

public class MessageConfiguration extends ConfigurationFile {

    public final String rankExistsError = ChatColor.RED + "A rank with the name %s already exists.";
    public final String rankCreatedMessage = ChatColor.GREEN + "Created new rank %s.";

    public MessageConfiguration(File configFile) {
        super(configFile);
        super.load();
    }

    private MessageConfiguration() {
        super(null);
    }
}
