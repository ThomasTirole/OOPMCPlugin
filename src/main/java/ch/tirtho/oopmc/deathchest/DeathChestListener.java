package ch.tirtho.oopmc.deathchest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DeathChestListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        // Create a chest at the death location
        Block block = player.getLocation().getBlock();
        block.setType(Material.CHEST);
        // Retrieve the chest and set its inventory
        if (block.getState() instanceof Chest chest) {
            // Add the player's dropped items to the chest inventory
            Map<Integer, ItemStack> leftovers = chest.getBlockInventory().addItem(event.getDrops().toArray(new ItemStack[0]));

            for (ItemStack item : leftovers.values()) {
                // If there are items that couldn't be added to the chest, drop them on the ground
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }

        }
        // Delete what's actually being dropped on the ground to avoid duplication of items
        event.getDrops().clear();
    }
}
