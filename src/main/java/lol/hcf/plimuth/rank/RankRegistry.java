package lol.hcf.plimuth.rank;

public interface RankRegistry {
    Rank getRank(String id);
    void addRank(Rank rank);
}
