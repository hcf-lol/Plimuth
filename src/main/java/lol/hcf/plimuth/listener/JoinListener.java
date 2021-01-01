package lol.hcf.plimuth.listener;

import com.mongodb.client.MongoCollection;
import lol.hcf.foundation.database.ConnectionHandler;
import lol.hcf.plimuth.agent.PlimuthAgent;
import lol.hcf.plimuth.extension.PlayerDataContainer;
import lol.hcf.plimuth.extension.PlayerExtension;
import lol.hcf.plimuth.player.PlayerData;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JoinListener implements Listener {

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private final RankRegistry rankRegistry;
    private final ConnectionHandler connectionHandler;
    private final MongoCollection<Document> playerRanks;

    public JoinListener(RankRegistry rankRegistry, ConnectionHandler connectionHandler) {
        this.rankRegistry = rankRegistry;
        this.connectionHandler = connectionHandler;
        this.playerRanks = connectionHandler.getDatabase().getDatabase("plimuth").getCollection("players");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerDataContainer container = ((PlayerExtension) event.getPlayer()).getPlayerData();
        PlayerData data = new PlayerData(event.getPlayer());

        String uuid = event.getPlayer().getUniqueId().toString();
        String rank;
        try (Jedis redis = this.connectionHandler.getRedis()) {
            rank = redis.hget("plimuth:players", uuid);
        }

        if (rank == null) {
            Document player = this.playerRanks.find(new BsonDocument().append("_id", new BsonBinary(event.getPlayer().getUniqueId()))).first();
            if (player != null && ((rank = player.getString("rank")) != null)) {
                String finalRank = rank;

                JoinListener.EXECUTOR.execute(() -> {
                    try (Jedis redis = JoinListener.this.connectionHandler.getRedis()) {
                        redis.hset("plimuth:players", uuid, finalRank);
                    }
                });
            }
        }

        Rank rankHandle = rank == null ? this.rankRegistry.getDefaultRank() : this.rankRegistry.getRank(rank);

        data.setRank(rankHandle);
        PlimuthAgent.getConsumer().accept(event.getPlayer(), rankHandle);
        container.set(data);

        PlayerData.PLAYERS.add(data);
    }

}
