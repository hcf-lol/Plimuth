package lol.hcf.plimuth.command.rank;

import lol.hcf.foundation.command.Command;
import lol.hcf.foundation.command.annotation.CommandEntryPoint;
import lol.hcf.foundation.command.parse.CommandTypeAdapter;
import lol.hcf.foundation.gui.InventoryGui;
import lol.hcf.foundation.gui.ItemInfo;
import lol.hcf.plimuth.plugin.config.PluginConfiguration;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

public class RankGrantCommand extends Command<PluginConfiguration> {

    private final RankRegistry rankRegistry;

    public RankGrantCommand(CommandTypeAdapter typeAdapter, PluginConfiguration config, RankRegistry rankRegistry) {
        super(typeAdapter, config, "rank.grant", true, "grant");
        this.rankRegistry = rankRegistry;
    }

    @CommandEntryPoint
    public void onCommand(Player player) {
        Collection<Rank> ranks = this.rankRegistry.getRanks();
        InventoryGui gui = new InventoryGui(player, ((ranks.size() - 1) / 9 + 1) * 9);
        for (Rank rank : ranks) {
            ItemInfo itemInfo = new ItemInfo(Material.BOOK, 1);

        }
    }
}
