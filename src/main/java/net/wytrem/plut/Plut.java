package net.wytrem.plut;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Plut<P extends PlutPlayer> extends JavaPlugin {
    private Map<UUID, P> onlinePlayers = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();

        getServer().getPluginManager().registerEvents(new BackgroundListener(), this);
        getServer().getOnlinePlayers().forEach(this::registerPlayer);
    }

    public P getPlayer(UUID playerUuid) {
        return onlinePlayers.get(playerUuid);
    }

    public P getPlayer(Player handle) {
        return getPlayer(handle.getUniqueId());
    }

    public P getPlayer(PlayerEvent playerEvent) {
        return getPlayer(playerEvent.getPlayer());
    }

    public SharedInventory createSharedInventory(Inventory inventory) {
        return new SharedInventory(this, inventory);
    }

    protected abstract P createPlayer(Player handle);

    @Override
    public String toString() {
        return "Plut{" +
                "onlinePlayers=" + onlinePlayers +
                '}';
    }

    private void registerPlayer(Player player) {
        onlinePlayers.put(player.getUniqueId(), createPlayer(player));
    }

    public void logInfo(String msg) {
        getLogger().info(msg);
    }

    public void logWarning(String msg) {
        getLogger().warning(msg);
    }

    public void logSevere(String msg) {
        getLogger().severe(msg);
    }

    public class BackgroundListener implements Listener {
        @EventHandler
        public void playerJoin(PlayerJoinEvent joinEvent) {
            registerPlayer(joinEvent.getPlayer());
        }

        @EventHandler
        public void playerQuit(PlayerQuitEvent quitEvent) {
            onlinePlayers.remove(quitEvent.getPlayer().getUniqueId());
        }
    }
}

