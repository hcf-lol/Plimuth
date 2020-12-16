package lol.hcf.plimuth.plugin.config;

import lol.hcf.foundation.command.config.CommandConfiguration;

public interface PluginConfiguration extends CommandConfiguration {
    MessageConfiguration getMessageConfiguration();
}
