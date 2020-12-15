package lol.hcf.plimuth.plugin;

import lol.hcf.foundation.command.config.CommandConfiguration;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;

public interface PluginConfiguration extends CommandConfiguration {
    MessageConfiguration getMessageConfiguration();
}
