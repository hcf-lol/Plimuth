package lol.hcf.plimuth.rank;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import lol.hcf.foundation.database.ConnectionHandler;
import org.bson.BsonDocument;
import org.bson.Document;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RankManager implements RankRegistry {

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private final Map<String, Rank> ranks = new HashMap<>();

    private ConnectionHandler connectionHandler;
    private MongoCollection<Document> collection;

    public RankManager(ConnectionHandler handler) {
        this.connectionHandler = handler;
        this.collection = handler.getDatabase().getDatabase("plimuth").getCollection("ranks");
        this.fetchRanks();
    }

    @Override
    public Rank getRank(String id) {
        return this.ranks.get(id.toLowerCase());
    }

    @Override
    public void addRank(Rank rank) {
        this.ranks.put(rank.getId().toLowerCase(), rank);
    }

    public void fetchRanks() {
        this.ranks.clear();
        Map<Rank, Map.Entry<Set<String>, Set<String>>> rankPermissionMap = new HashMap<>();
        for (Document document : this.collection.find()) {
            Rank rank = new Rank(document.getString("_id"));
            rank.setPrefix(document.getString("prefix"));

            Set<String> parents = new HashSet<>(document.getList("inherits", String.class, Collections.emptyList()));
            Set<String> permissions = new HashSet<>(document.getList("permissions", String.class, Collections.emptyList()));
            rankPermissionMap.put(rank, new AbstractMap.SimpleEntry<>(parents, permissions));

            this.ranks.put(rank.getId(), rank);
        }

        for (Map.Entry<Rank, Map.Entry<Set<String>, Set<String>>> entry : rankPermissionMap.entrySet()) {
            Rank rank = entry.getKey();
            Set<String> parents = entry.getValue().getKey();
            Set<String> permissions = entry.getValue().getValue();

            Set<Rank> parentRanks = new HashSet<>(parents.size());
            parents.forEach((s) -> parentRanks.add(Objects.requireNonNull(RankManager.this.getRank(s))));
            rank.setParents(parentRanks);

            parentRanks.forEach((r) -> permissions.addAll(r.getPermissions()));

        }
    }

    public void postRanks() {
        List<Document> ranks = new ArrayList<>(this.ranks.size());

        for (Rank rank : this.ranks.values()) {
            Set<String> parents = rank.getParents() == null ? null : rank.getParents().stream().map(Rank::getId).collect(Collectors.toSet());

            Document object = new Document()
                    .append("_id", rank.getId())
                    .append("prefix", rank.getPrefix())
                    .append("inherits", parents)
                    .append("permissions", new BasicDBList().addAll(rank.getPermissions()));

            ranks.add(object);
        }

        this.collection.deleteMany(new BsonDocument());
        this.collection.insertMany(ranks);
    }
}
