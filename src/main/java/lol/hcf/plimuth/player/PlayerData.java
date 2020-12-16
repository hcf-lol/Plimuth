package lol.hcf.plimuth.player;

import lol.hcf.plimuth.extension.PlayerExtension;
import lol.hcf.plimuth.rank.Rank;
import org.bukkit.entity.Player;

public class PlayerData {

    private Rank rank = null;

    public Rank getRank() {
        return rank;
    }

    public PlayerData setRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    public static PlayerData valueOf(Player player) {
        return ((PlayerExtension) player).getPlayerData().get();
    }
}
