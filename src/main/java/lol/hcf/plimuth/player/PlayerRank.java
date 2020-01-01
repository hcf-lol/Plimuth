package lol.hcf.plimuth.player;

import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;
import org.bson.Document;

public class PlayerRank {

    private final String sponsor;

    private final String rankId;
    private final transient Rank rank;

    private final long start;
    private long end;

    public PlayerRank(String sponsor, String rankId, long start, long end, RankRegistry rankRegistry) {
        this(sponsor, rankRegistry.getRank(rankId), start, end);
    }

    public PlayerRank(String sponsor, Rank rank, long start, long end) {
        this.sponsor = sponsor;
        this.rankId = rank.getId();
        this.rank = rank;
        this.start = start;
        this.end = end;
    }

    public PlayerRank setEnd(long end) {
        this.end = end;
        return this;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getRankId() {
        return rankId;
    }

    public Rank getRank() {
        return rank;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Document toBson() {
        return new Document()
                .append("sponsor", this.sponsor)
                .append("rankId", this.rank == null ? this.rankId : this.rank.getId())
                .append("start", this.start)
                .append("end", this.end);
    }

    public static PlayerRank valueOf(Document document, RankRegistry rankRegistry) {
        return new PlayerRank(document.getString("sponsor"), document.getString("rankId"), document.getLong("start"), document.getLong("end"), rankRegistry);
    }
}
