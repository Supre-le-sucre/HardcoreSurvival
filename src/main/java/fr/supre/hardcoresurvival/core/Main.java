package fr.supre.hardcoresurvival.core;

import fr.supre.hardcoresurvival.commands.CommandHardcore;
import fr.supre.hardcoresurvival.listeners.Listeners;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

        //Recipe (Can be disabled and modified in the completed version of this plugin)
        ItemStack badOmenPotion = new ItemStack(Material.POTION);
        ItemMeta badOmenPotionName = badOmenPotion.getItemMeta();
        badOmenPotionName.setDisplayName("§5§lBad Omen III §d§lPotion");
        badOmenPotion.setItemMeta(badOmenPotionName);
        PotionMeta badOmenPotionMeta = (PotionMeta) badOmenPotion.getItemMeta();
        badOmenPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BAD_OMEN, Integer.MAX_VALUE, 2), true);
        badOmenPotionMeta.setColor(Color.fromRGB(95,53,116));
        badOmenPotion.setItemMeta(badOmenPotionMeta);

        ShapelessRecipe badOmenRecipe = new ShapelessRecipe(badOmenPotion);
        badOmenRecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);
        badOmenRecipe.addIngredient(Material.ROTTEN_FLESH);
        badOmenRecipe.addIngredient(Material.BONE);
        badOmenRecipe.addIngredient(Material.GUNPOWDER);
        badOmenRecipe.addIngredient(Material.ENDER_EYE);
        badOmenRecipe.addIngredient(Material.MAGMA_CREAM);
        badOmenRecipe.addIngredient(Material.NETHERITE_INGOT);
        badOmenRecipe.addIngredient(Material.GHAST_TEAR);
        badOmenRecipe.addIngredient(Material.GLASS_BOTTLE);

        getServer().addRecipe(badOmenRecipe);

    }

    private void loadConfigManager() {
        cfmg = new ConfigManager();
        cfmg.setup();
    }

    public void onDisable() {
        System.out.println("§4[§6Hardcore§4] Plus désactivé");
    }
}
