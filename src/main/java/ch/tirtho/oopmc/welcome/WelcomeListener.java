package ch.tirtho.oopmc.welcome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class WelcomeListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(Component.text("Bienvenue " + player.getName() + " !", NamedTextColor.GOLD));

        player.showTitle(Title.title(
                Component.text("OOPMC", NamedTextColor.AQUA),
                Component.text("Bon jeu !")
        ));
    }
}
