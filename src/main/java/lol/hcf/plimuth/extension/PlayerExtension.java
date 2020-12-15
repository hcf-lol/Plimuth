package lol.hcf.plimuth.extension;

import lol.hcf.antihashmap.extension.annotation.ClassTarget;
import lol.hcf.plimuth.player.PlayerData;

import static lol.hcf.antihashmap.extension.ClassExtension.*;
import static lol.hcf.antihashmap.extension.ClassExtension.END;

@ClassTarget(inject = START + CRAFT_BUKKIT + "entity" + PACKAGE_SEPARATOR + "CraftPlayer" + END, extension = PlayerData.class)
public interface PlayerExtension {
    PlayerDataContainer getPlayerData();
}
