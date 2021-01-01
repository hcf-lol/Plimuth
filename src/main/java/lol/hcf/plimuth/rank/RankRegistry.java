package lol.hcf.plimuth.rank;

import lol.hcf.plimuth.player.PlayerRank;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface RankRegistry {
    Rank getRank(String id);
    void addRank(Rank rank);

    Rank getDefaultRank();

    Collection<Rank> getRanks();

    void syncRanks();

    void fetchRanks();

    void syncFromLocal();
    void fetchToLocal();

    Collection<PlayerRank> getPlayerRanks(Player player);
}
