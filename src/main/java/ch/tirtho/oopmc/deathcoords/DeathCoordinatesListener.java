package ch.tirtho.oopmc.deathcoords;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathCoordinatesListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        // Get the player's death location
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

        // Send the coordinates to the player
        player.sendMessage(Component.text("Mort aux coords suivantes -> X : " + x + ", Y : " + y + ", Z : " + z, NamedTextColor.RED));
    }

}
