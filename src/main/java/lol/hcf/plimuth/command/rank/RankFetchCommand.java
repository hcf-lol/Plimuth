package lol.hcf.plimuth.command.rank;

import lol.hcf.foundation.command.Command;
import lol.hcf.foundation.command.annotation.CommandEntryPoint;
import lol.hcf.foundation.command.parse.CommandTypeAdapter;
import lol.hcf.plimuth.plugin.config.PluginConfiguration;
import lol.hcf.plimuth.rank.RankRegistry;
import org.bukkit.command.CommandSender;

public class RankFetchCommand extends Command<PluginConfiguration> {

    private final RankRegistry rankRegistry;

    public RankFetchCommand(CommandTypeAdapter typeAdapter, PluginConfiguration config, RankRegistry rankRegistry) {
        super(typeAdapter, config, "rank.fetch", false, "sync");
        this.rankRegistry = rankRegistry;
    }

    @CommandEntryPoint
    public void onCommand(CommandSender sender) {
        this.rankRegistry.fetchRanks();
        sender.sendMessage(super.config.getMessageConfiguration().rankFetchedMessage);
    }


}
