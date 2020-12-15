package lol.hcf.plimuth.command.type;

import lol.hcf.foundation.command.function.ArgumentParser;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;

public class RankAdapter implements ArgumentParser<Rank> {

    private final RankRegistry registry;

    public RankAdapter(RankRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Rank apply(String s) {
        Rank rank = this.registry.getRank(s);
        if (rank == null) throw new ArgumentParser.Exception("No rank with the name " + s + " exists.", false);
        return rank;
    }
}
