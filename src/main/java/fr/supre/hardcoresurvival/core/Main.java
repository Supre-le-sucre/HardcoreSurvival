package fr.supre.hardcoresurvival.core;

import fr.supre.hardcoresurvival.commands.CommandHardcore;
import fr.supre.hardcoresurvival.listeners.Listeners;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
    public ConfigManager cfmg;
    public NamespacedKey keyBadOmenRecipe = new NamespacedKey(this, "badOmenRecipe");

    public void onEnable() {
        loadConfigManager();
        saveDefaultConfig();
        System.out.println("§4[§6Hardcore§4] §2Plugin has started §4Good luck >:3");
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getCommand("revive").setExecutor(new CommandHardcore(this));
        getCommand("sacrifice").setExecutor(new CommandHardcore(this));
        getCommand("tpto").setExecutor(new CommandHardcore(this));
        getCommand("start").setExecutor(new CommandHardcore(this));
        getCommand("hardcorereload").setExecutor(new CommandHardcore(this));

        //Recipe (Can be disabled and modified in the completed version of this plugin)
        if(this.getConfig().getBoolean("Gameplay.Recipe.craft-bad-omen")) {
            ItemStack badOmenPotion = new ItemStack(Material.POTION);
            ItemMeta badOmenPotionName = badOmenPotion.getItemMeta();
            badOmenPotionName.setDisplayName(this.getConfig().getString("Gameplay.Recipe.bad-omen-name"));
            badOmenPotion.setItemMeta(badOmenPotionName);
            PotionMeta badOmenPotionMeta = (PotionMeta) badOmenPotion.getItemMeta();
            badOmenPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BAD_OMEN, Integer.MAX_VALUE, this.getConfig().getInt("Gameplay.Recipe.bad-omen-level") - 1), true);
            badOmenPotionMeta.setColor(Color.fromRGB(this.getConfig().getInt("Gameplay.Recipe.bad-omen-color.R"), this.getConfig().getInt("Gameplay.Recipe.bad-omen-color.G"), this.getConfig().getInt("Gameplay.Recipe.bad-omen-color.B")));
            badOmenPotion.setItemMeta(badOmenPotionMeta);

            ShapelessRecipe badOmenRecipe = new ShapelessRecipe(keyBadOmenRecipe, badOmenPotion);
            if(this.getConfig().getList("Gameplay.Recipe.materials").size()<=9) {
                for (String s: this.getConfig().getStringList("Gameplay.Recipe.materials")) {
                    Material mat = Material.getMaterial(s.toUpperCase());
                    if (mat == null)
                        System.out.println("§4[§6Hardcore§4] §4Error while loading configuration, material: §6" + s + " §4is not a proper material and cannot be added to the craft of the bad omen potion \n §4Consider fix this error or this may result to an invalid craft");
                    else badOmenRecipe.addIngredient(mat);
                } getServer().addRecipe(badOmenRecipe);
            } else {
                System.out.println("§4[§6Hardcore§4] §4Error while loading configuration, too many materials are indicated to craft the bad omen potion, only 9 or less can be written, craft has been disabled");
            }
        }


    }

    private void loadConfigManager() {
        cfmg = new ConfigManager();
        cfmg.setup();
    }

    public void onDisable() {
        System.out.println("§4[§6Hardcore§4] Plugin disabled");
    }
}
