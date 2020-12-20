package lol.hcf.plimuth.rank;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import lol.hcf.foundation.database.ConnectionHandler;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

public class RankManager implements RankRegistry {

    private final Map<String, Rank> ranks = new HashMap<>();
    private final Rank defaultRank;

    private final MongoCollection<Document> collection;

    public RankManager(ConnectionHandler handler) {
        this.collection = handler.getDatabase().getDatabase("plimuth").getCollection("ranks");
        this.fetchRanks();

        Rank rank = this.ranks.get("default");
        rank = this.ranks.get("default");
        if (rank == null) {
            rank = new Rank("default");
            this.addRank(rank);
            this.syncRanks();
        }

        this.defaultRank = rank;
    }

    @Override
    public Rank getRank(String id) {
        return this.ranks.get(id.toLowerCase());
    }

    @Override
    public void addRank(Rank rank) {
        this.ranks.put(rank.getId().toLowerCase(), rank);
    }

    @Override
    public Rank getDefaultRank() {
        return this.defaultRank;
    }

    @Override
    public void syncRanks() {
        List<Document> ranks = new ArrayList<>(this.ranks.size());

        for (Rank rank : this.ranks.values()) {
            Set<String> parents = rank.getParents() == null ? null : rank.getParents().stream().map(Rank::getId).collect(Collectors.toSet());
            BasicDBList list = new BasicDBList();
            list.addAll(rank.getPermissions());

            Document object = new Document()
                    .append("_id", rank.getId())
                    .append("prefix", rank.getPrefix())
                    .append("inherits", parents)
                    .append("permissions", list);

            ranks.add(object);
        }

        this.collection.deleteMany(new BsonDocument());
        this.collection.insertMany(ranks);
    }

    @Override
    public void fetchRanks() {
        this.ranks.clear();

        Map<Rank, Set<String>> rankParentMap = new HashMap<>();

        for (Document document : this.collection.find()) {
            Rank rank = new Rank(document.getString("_id"));
            rank.setPrefix(document.getString("prefix"));

            Set<String> parents = new HashSet<>(document.getList("inherits", String.class, Collections.emptyList()));

            Set<String> permissions = new HashSet<>(document.getList("permissions", String.class, Collections.emptyList()));
            rank.setPermissions(permissions);

            rankParentMap.put(rank, parents);
            this.ranks.put(rank.getId(), rank);
        }

        for (Map.Entry<Rank, Set<String>> entry : rankParentMap.entrySet()) {
            Rank rank = entry.getKey();
            Set<String> parents = entry.getValue();

            Set<Rank> parentRanks = new HashSet<>(parents.size(), 1.0f);  //ensuring buckets
            parentRanks.addAll(parents.stream().map(this::getRank).collect(Collectors.toSet()));

            rank.setParents(parentRanks);
        }

        Set<Rank> set = new HashSet<>();
        this.ranks.values().forEach((rank) -> this.resolveInheritance(rank, set));
    }

    private void resolveInheritance(Rank rank, Set<Rank> parentLookupSet) {
        rank.getParents().forEach((o) -> this.resolveInheritance(o, parentLookupSet));
        if (parentLookupSet.contains(rank)) return;
        rank.getParents().forEach((parent) -> rank.getPermissions().addAll(parent.getPermissions()));
        parentLookupSet.add(rank);
    }
}
