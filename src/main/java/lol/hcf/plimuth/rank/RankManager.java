package lol.hcf.plimuth.rank;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;
import lol.hcf.foundation.database.ConnectionHandler;
import lol.hcf.plimuth.player.PlayerData;
import lol.hcf.plimuth.player.PlayerRank;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RankManager implements RankRegistry {

    private final Map<String, Rank> ranks = new HashMap<>();

    private final MongoCollection<Document> collection;
    private final File cacheDir;

    private Rank defaultRank;

    public RankManager(ConnectionHandler handler, File cacheDir) {
        this.collection = handler.getDatabase().getDatabase("plimuth").getCollection("ranks");
        this.fetchRanks();

        this.checkDefaultRank();

        this.cacheDir = cacheDir;
        try {
            if (!cacheDir.exists() && !this.cacheDir.mkdirs())
                throw new IOException("directory could not be made");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public Collection<Rank> getRanks() {
        return this.ranks.values();
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerData.valueOf(player);
            if (data.getRank() != null) {
                PlayerRank oldRank = data.getRank();
                Rank newRank = this.ranks.get(oldRank.getRankId());
                if (newRank == null) newRank = this.defaultRank;
                data.setRank(new PlayerRank());
            }
        }
    }

    @Override
    public void syncFromLocal() {
        File[] files = this.cacheDir.listFiles();
        if (files == null) throw new RuntimeException("failed to list files in directory");
        List<Document> ranks = new ArrayList<>(files.length);
        for (File file : files) {
            if (!file.getName().endsWith(".json")) continue;
            String content;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }

                content = builder.toString();
            } catch (IOException e) {
                throw new RuntimeException("failed to read from file: " + e.getMessage());
            }

            Document document;
            try {
                document = Document.parse(content);
            } catch (Exception e) {
                throw new RuntimeException("Invalid JSON File: " + file.getName());
            }

            // Integrity Check
            if (document == null) throw new RuntimeException("Invalid Document: " + file.getName());
            if (!(document.get("_id") instanceof String)) throw new RuntimeException("\"_id\" must be a string value");
            if (!(document.get("prefix") == null || document.get("prefix") instanceof String)) throw new RuntimeException("\"prefix\" must be a string or null");
            if (!(document.get("inherits") instanceof List)) throw new RuntimeException("\"inherits\" must be a list");
            if (!(document.get("permissions") instanceof List)) throw new RuntimeException("\"permissions\" must be a list");
            ranks.add(document);
        }

        this.collection.deleteMany(new BsonDocument());
        if (ranks.size() != 0) this.collection.insertMany(ranks);
        this.fetchRanks();
        this.checkDefaultRank();    // Fetch, then check if default doesn't exist.
    }

    @Override
    public void fetchToLocal() {
        JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();

        this.collection.find().forEach((Consumer<? super Document>) document -> {
            try (FileWriter writer = new FileWriter(new File(this.cacheDir, document.getString("_id")) + ".json")) {
                writer.write(document.toJson(settings));
            } catch (IOException e) {
                throw new RuntimeException("failed to write to file: " + e.getMessage());
            }
        });
    }

    @Override
    public Collection<PlayerRank> getPlayerRanks(Player player) {
        return null;
    }

    private void checkDefaultRank() {
        Rank rank = this.ranks.get("default");
        if (rank == null) {
            rank = new Rank("default");
            this.addRank(rank);
            this.syncRanks();
        }

        this.defaultRank = rank;
    }

    private void resolveInheritance(Rank rank, Set<Rank> parentLookupSet) {
        rank.getParents().forEach((o) -> this.resolveInheritance(o, parentLookupSet));
        if (parentLookupSet.contains(rank)) return;
        rank.getParents().forEach((parent) -> rank.getPermissions().addAll(parent.getPermissions()));
        parentLookupSet.add(rank);
    }
}
