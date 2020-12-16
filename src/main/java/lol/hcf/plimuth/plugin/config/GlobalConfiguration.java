package lol.hcf.plimuth.plugin.config;

import lol.hcf.foundation.data.impl.yml.ConfigurationFile;
import lol.hcf.plimuth.rank.Rank;
import lol.hcf.plimuth.rank.RankRegistry;

import java.io.File;

public class GlobalConfiguration extends ConfigurationFile {

    public transient final Rank defaultRank;
    private final String defaultRankId = null;

    public GlobalConfiguration(File configFile, RankRegistry rankRegistry) {
        super(configFile);
        super.load();
        this.defaultRank = rankRegistry.getRank(this.defaultRankId);
    }

    public GlobalConfiguration() {
        super(null);
        this.defaultRank = null;
    }
}
