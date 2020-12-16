package lol.hcf.plimuth.extension;

import lol.hcf.antihashmap.extension.ClassExtension;
import lol.hcf.antihashmap.extension.annotation.ClassTarget;

import static lol.hcf.antihashmap.extension.ClassExtension.*;
import static lol.hcf.antihashmap.extension.ClassExtension.END;

@ClassTarget(inject = START + CRAFT_BUKKIT + "entity" + PACKAGE_SEPARATOR + "CraftPlayer" + END, extension = PlayerDataContainer.class)
public interface PlayerExtension extends ClassExtension {
    PlayerDataContainer getPlayerData();
}
