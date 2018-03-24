package net.wytrem.plut;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SharedInventory implements Listener {

    protected Inventory content;
    protected List<UUID> viewers;
    protected Plut<?> plugin;

    public SharedInventory(Plut<?> plugin, Inventory inventory) {
        this.content = inventory;
        this.viewers = new ArrayList<>();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player player) {
        player.openInventory(content);
        viewers.add(player.getUniqueId());
    }

    public void sendUpdates() {
        this.viewers.stream().map(Bukkit::getPlayer).forEach(Player::updateInventory);
    }

    public Inventory getContent() {
        return content;
    }

    public boolean isViewing(Player player) {
        return viewers.contains(player.getUniqueId());
    }

    @EventHandler
    public final void playerClick(InventoryClickEvent clickEvent) {
        HumanEntity clickyGuy = clickEvent.getWhoClicked();

        if (clickyGuy instanceof Player) {
            Player player = (Player) clickyGuy;

            if (isViewing(player)) {
                onPlayerClick(player, clickEvent);
            }
        }
    }

    public void onPlayerClick(Player player, InventoryClickEvent clickEvent) {
    }

    @EventHandler
    public final void playerClose(InventoryCloseEvent closeEvent) {

        // If the closing player was concerned by this inventory, we remove him from the viewers.
        this.viewers.remove(closeEvent.getPlayer().getUniqueId());
    }

    public void destroy() {
        this.viewers.stream().map(Bukkit::getPlayer).forEach(Player::closeInventory);
        HandlerList.unregisterAll(this);
    }
}
