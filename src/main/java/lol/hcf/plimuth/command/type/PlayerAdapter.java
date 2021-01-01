package lol.hcf.plimuth.command.type;

import lol.hcf.foundation.command.function.ArgumentParser;
import lol.hcf.plimuth.plugin.config.MessageConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerAdapter implements ArgumentParser<Player> {

    private final MessageConfiguration messageConfiguration;

    public PlayerAdapter(MessageConfiguration messageConfiguration) {
        this.messageConfiguration = messageConfiguration;
    }

    @Override
    public Player apply(String s) {
        Player player = Bukkit.getPlayerExact(s);
        if (player == null) {
            throw new ArgumentParser.Exception(this.messageConfiguration.playerNotFound, true);
        }

        return player;
    }
}
