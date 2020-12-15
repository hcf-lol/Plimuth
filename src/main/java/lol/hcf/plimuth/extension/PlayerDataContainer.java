package lol.hcf.plimuth.extension;

import lol.hcf.antihashmap.extension.annotation.PreLoad;
import lol.hcf.plimuth.player.PlayerData;

import java.util.function.Supplier;

@PreLoad
public class PlayerDataContainer implements Supplier<PlayerData> {

    private PlayerData data;

    @Override
    public PlayerData get() {
        return this.data;
    }

    public PlayerDataContainer set(PlayerData data) {
        this.data = data;
        return this;
    }
}
