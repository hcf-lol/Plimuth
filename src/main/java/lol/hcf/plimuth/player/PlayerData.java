package lol.hcf.plimuth.player;

import lol.hcf.plimuth.extension.PlayerExtension;
import lol.hcf.plimuth.rank.Rank;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {

    // Thread-Safe Collection
    public static final Set<PlayerData> PLAYERS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private transient final Player player;
    private volatile PlayerRank rank = null;

    public PlayerData(Player player) {
        this.player = player;
    }

    public synchronized PlayerRank getRank() {
        return rank;
    }

    public synchronized PlayerData setRank(PlayerRank rank) {
        this.rank = rank;
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null) return false;
        if (!this.getClass().equals(rhs.getClass())) return false;

        return this.player.getUniqueId().equals(((PlayerData) rhs).player.getUniqueId());
    }

    @Override
    public int hashCode() {
        return this.player.getUniqueId().hashCode();
    }

    public static PlayerData valueOf(Player player) {
        return ((PlayerExtension) player).getPlayerData().get();
    }
}
