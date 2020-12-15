package lol.hcf.plimuth.rank;

public class Rank {

    private String id;
    private String displayName;
    private String prefix;

    public Rank(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }
}
