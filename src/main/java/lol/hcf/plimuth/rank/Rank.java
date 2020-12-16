package lol.hcf.plimuth.rank;

import java.util.HashSet;
import java.util.Set;

public class Rank {

    private final String id;

    private String prefix;
    private Set<Rank> parents;

    private Set<String> permissions;

    public Rank(String id) {
        this.id = id;
        this.permissions = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public Rank setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Set<Rank> getParents() {
        return parents;
    }

    public Rank setParents(Set<Rank> parents) {
        this.parents = parents;
        return this;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public Rank setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
