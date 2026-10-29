package ch.tirtho.oopmc;

import ch.tirtho.oopmc.deathcoords.DeathCoordinatesListener;
import ch.tirtho.oopmc.heal.HealCommand;
import ch.tirtho.oopmc.welcome.WelcomeListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OOPMCPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getCommand("heal").setExecutor(new HealCommand());
        getServer().getPluginManager().registerEvents(new DeathCoordinatesListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
