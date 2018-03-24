package net.wytrem.plut;

import org.bukkit.entity.Player;

public class PlutPlayer {
    protected final Player handle;

    public PlutPlayer(Player handle) {
        this.handle = handle;
    }

    public Player getHandle() {
        return handle;
    }
}
