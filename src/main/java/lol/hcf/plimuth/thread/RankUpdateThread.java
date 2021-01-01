package lol.hcf.plimuth.thread;

import lol.hcf.plimuth.player.PlayerData;
import lol.hcf.plimuth.player.PlayerRank;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;
import lol.hcf.plimuth.plugin.config.PluginConfiguration;

public class RankUpdateThread extends Thread {

    private final MessageConfiguration messageConfiguration;

    public RankUpdateThread(PluginConfiguration config) {
        this.messageConfiguration = config.getMessageConfiguration();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (PlayerData data : PlayerData.PLAYERS) {
                    this.processRank(data);
                }
            }

            Thread.sleep(100);
        } catch (InterruptedException ignore) {}
    }

    public void processRank(PlayerData data) {
        PlayerRank rank = data.getRank();
        if (rank == null || System.currentTimeMillis() < rank.getEnd()) return;
        data.setRank(null);
        data.getPlayer().sendMessage(this.messageConfiguration.rankExpiryMessage);
    }
}
