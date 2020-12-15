package lol.hcf.plimuth.command.rank;

import lol.hcf.foundation.command.Command;
import lol.hcf.foundation.command.annotation.Argument;
import lol.hcf.foundation.command.annotation.CommandEntryPoint;
import lol.hcf.foundation.command.parse.CommandTypeAdapter;
import lol.hcf.plimuth.plugin.PluginConfiguration;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;
import org.bukkit.command.CommandSender;

public class RankCreateCommand extends Command<PluginConfiguration> {

    private final RankRegistry rankRegistry;

    public RankCreateCommand(CommandTypeAdapter typeAdapter, PluginConfiguration config, RankRegistry rankRegistry) {
        super(typeAdapter, config, "rank.create", false, "create");
        this.rankRegistry = rankRegistry;
    }

    @CommandEntryPoint
    public void onCommand(CommandSender sender, @Argument("rank id") String id) {
        Rank rank = this.rankRegistry.getRank(id);
        if (rank != null) {
            sender.sendMessage(String.format(super.config.getMessageConfiguration().rankExistsError, id));
            return;
        }

        this.rankRegistry.addRank(new Rank(id));
        sender.sendMessage(String.format(super.config.getMessageConfiguration().rankCreatedMessage, id));
    }

}
