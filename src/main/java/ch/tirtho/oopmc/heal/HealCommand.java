package ch.tirtho.oopmc.heal;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // On vérifie les arguments pour voir s'il y a un autre joueurs à soigner
        if (args.length > 1) {
            sender.sendMessage(Component.text("Vous ne pouvez soigner qu'un joueur à la fois !", NamedTextColor.RED));
            return true;
        }
        if (args.length == 1) {
            // On récupère l'argument pour heal l'autre joueur et on check s'il est connecté
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Ce joueur n'est pas connecté !", NamedTextColor.YELLOW));
                return true;
            }
            healPlayer(target);
            target.sendMessage(Component.text("Vous avez été soigné par : " + sender.getName(), NamedTextColor.AQUA ));
            sender.sendMessage(Component.text(target.getName() + " a été soigné", NamedTextColor.AQUA));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Vous devez spécifier un joueur ! /heal <joueur>");
            return true;
        }
        healPlayer(player);
        player.sendMessage(Component.text("Vous avez été soigné !", NamedTextColor.GREEN));
        return true;
    }

    private void healPlayer(Player player) {
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();

        //Définit la santé au maximum;
        player.setHealth(maxHealth);

        //Optionnel : Restaurer la barre de faim (20 ? complet)
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
    }

}
