package fr.supre.hardcoresurvival.core;

import fr.supre.hardcoresurvival.commands.CommandHardcore;
import fr.supre.hardcoresurvival.listeners.Listeners;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public ConfigManager cfmg;

    public void onEnable() {
        loadConfigManager();
        System.out.println("§4[§6Hardcore§4] §2Plugin lancé §4Bonne chance >:3");
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getCommand("revive").setExecutor(new CommandHardcore(this));
        getCommand("sacrifice").setExecutor(new CommandHardcore(this));
        getCommand("tpto").setExecutor(new CommandHardcore(this));
        getCommand("start").setExecutor(new CommandHardcore(this));


        //TODO Gérer la mort du joueur: Sa tombe, spectateur
        //TODO /revive
        //TODO Créer une commande de démmarrage avec un tp au quatre coin de la map


    }

    private void loadConfigManager() {
        cfmg = new ConfigManager();
        cfmg.setup();
    }

    public void onDisable() {
        System.out.println("§4[§6Hardcore§4] Plus désactivé");
    }
}
