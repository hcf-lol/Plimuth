package lol.hcf.plimuth.rank;

import com.google.gson.*;
import lol.hcf.foundation.data.impl.json.DataFile;
import lol.hcf.foundation.data.impl.json.DataTypeAdapter;
import lol.hcf.foundation.data.impl.json.TypeAdapterContainer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RankManager extends DataFile implements RankRegistry {

    private final Map<String, Rank> ranks = new HashMap<>();

    public RankManager(File targetFile) {
        super(targetFile, new TypeAdapterContainer<>(RankManager.class, new TypeAdapter()));
    }

    private RankManager() {
        super(null);
    }

    @Override
    public Rank getRank(String id) {
        return this.ranks.get(id.toLowerCase());
    }

    @Override
    public void addRank(Rank rank) {
        this.ranks.put(rank.getId().toLowerCase(), rank);
    }

    public static class TypeAdapter implements DataTypeAdapter<RankManager> {
        @Override
        public RankManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            RankManager manager = new RankManager();
            Rank[] ranks = context.deserialize(json, Rank[].class);
            for (Rank rank : ranks) manager.addRank(rank);
            return manager;
        }

        @Override
        public JsonElement serialize(RankManager src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.ranks.values());
        }
    }
}
