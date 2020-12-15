package lol.hcf.plimuth.command.rank;

import lol.hcf.foundation.command.tree.CommandTree;
import lol.hcf.plimuth.command.type.RankAdapter;
import lol.hcf.plimuth.plugin.PluginConfiguration;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;

import java.util.function.Supplier;

public class RankCommand implements Supplier<CommandTree<PluginConfiguration>> {

    private final CommandTree<PluginConfiguration> tree;

    public RankCommand(PluginConfiguration config, RankRegistry rankRegistry) {
        this.tree = CommandTree.builder(config)
                .setAliases("rank")
                .registerTypeMapping(Rank.class, new RankAdapter(rankRegistry))
                .registerSubcommand((o1, o2) -> new RankCreateCommand(o1, o2, rankRegistry), "Utility Commands", "Create a new rank")
                .build();
    }

    @Override
    public CommandTree<PluginConfiguration> get() {
        return this.tree;
    }
}
