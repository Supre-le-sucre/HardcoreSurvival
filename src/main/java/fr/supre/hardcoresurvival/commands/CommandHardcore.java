package fr.supre.hardcoresurvival.commands;

import fr.supre.hardcoresurvival.core.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandHardcore implements CommandExecutor {

    private Main main;

    public CommandHardcore(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            //---------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("revive")) {
                if (!p.hasPermission("hardcore.revive")) {
                    p.sendMessage(main.getConfig().getString("Messages.no-permission").replace("%perm%", "hardcore.survival"));
                } else {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (Bukkit.getServer().getOnlinePlayers().contains(target)) {
                            if (main.cfmg.getDatas().getStringList("Datas.dead").contains(String.valueOf(target.getUniqueId()))) {
                                int x = main.cfmg.getDatas().getInt("Datas." + target.getUniqueId() + ".X");
                                int y = main.cfmg.getDatas().getInt("Datas." + target.getUniqueId() + ".Y");
                                int z = main.cfmg.getDatas().getInt("Datas." + target.getUniqueId() + ".Z");
                                Location deathLoc = new Location(p.getWorld(), x + 1, y + 1, z);
                                target.teleport(deathLoc);
                                target.setGameMode(GameMode.SURVIVAL);
                                Bukkit.getServer().broadcastMessage(main.getConfig().getString("Messages.notify-revive").replace("%reviver%", p.getName()).replace("%revived%", target.getName()));
                                p.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 150);
                                p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation(), 50);
                                p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation(), 75);
                                p.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 1.5F);
                                List<String> deadList = main.cfmg.getDatas().getStringList("Datas.dead");
                                deadList.remove(String.valueOf(target.getUniqueId()));
                                main.cfmg.getDatas().set("Datas.dead", deadList);
                                main.cfmg.getDatas().set("Datas." + target.getUniqueId(), null);
                                main.cfmg.saveDatas();
                            } else p.sendMessage(main.getConfig().getString("Messages.alive"));
                        } else p.sendMessage(main.getConfig().getString("Messages.offline"));
                    } else p.sendMessage(main.getConfig().getString("Messages.revive-syntax"));
                }
            }
            //---------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("sacrifice")) {
                if (p.getGameMode().equals(GameMode.SPECTATOR))
                    p.sendMessage(main.getConfig().getString("Messages.sacrifice-dead"));
                else {
                    p.getInventory().clear();
                    p.setHealth(0);
                    Bukkit.getServer().broadcastMessage((main.getConfig().getString("Messages.notify-sacrifice").replace("%player%", p.getName())));
                    p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.TOTEM_OF_UNDYING));
                }
            }
            //----------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("tpto")) {
                if (!p.getGameMode().equals(GameMode.SPECTATOR))
                    p.sendMessage(main.getConfig().getString("Messages.tpto-alive"));
                else {
                    if (args.length != 0) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (Bukkit.getServer().getOnlinePlayers().contains(target)) {
                            p.teleport(target.getLocation());
                        } else p.sendMessage(main.getConfig().getString("Messages.offline"));
                    } else p.sendMessage(main.getConfig().getString("Messages.tpto-syntax"));
                }
            }
            //----------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("start")) {
                if (!p.hasPermission("hardcore.start"))
                    p.sendMessage(main.getConfig().getString("Messages.no-permission").replace("%perm%", "hardcore.start"));
                else {
                    int definition1 = 0;
                    int definition2 = 1;
                    int definition3 = 2;
                    int definition4 = 3;
                    ArrayList<Player> players = new ArrayList<Player>();
                    if (args.length == 0) {
                        Collection<? extends Player> playerCollection = Bukkit.getServer().getOnlinePlayers();
                        ;
                        players = new ArrayList(playerCollection);
                    } else {
                        for (int i = 0; i < args.length; i++) {
                            Player target = Bukkit.getPlayer(args[i]);
                            if (Bukkit.getServer().getOnlinePlayers().contains(target)) {
                                players.add(target);
                                p.sendMessage(main.getConfig().getString("Messages.notify-start").replace("%player%", target.getName()));
                                target.sendMessage(main.getConfig().getString("Messages.start-message"));
                            } else
                                p.sendMessage(main.getConfig().getString("Messages.offline"));
                        }

                    }
                    for (int i = 0; i < players.size(); i++) {
                        int x = 0;
                        int z = 0;
                        if (i == definition1) {
                            x = main.getConfig().getInt("Gameplay.Start.spawn-in")*-1;
                            z = main.getConfig().getInt("Gameplay.Start.spawn-in")*-1;
                            definition1 = definition1 + 4;
                        } else if (i == definition2) {
                            x = main.getConfig().getInt("Gameplay.Start.spawn-in");
                            z = main.getConfig().getInt("Gameplay.Start.spawn-in")*-1;
                            definition2 = definition2 + 4;
                        } else if (i == definition3) {
                            x = main.getConfig().getInt("Gameplay.Start.spawn-in");
                            z = main.getConfig().getInt("Gameplay.Start.spawn-in");
                            definition3 = definition3 + 4;
                        } else if (i == definition4) {
                            x = main.getConfig().getInt("Gameplay.Start.spawn-in")*-1;
                            z = main.getConfig().getInt("Gameplay.Start.spawn-in");
                            definition4 = definition4 + 4;
                        }
                        Location startLoc = new Location(p.getWorld(), x, 150, z);
                        players.get(i).getInventory().clear();
                        players.get(i).setHealth(20);
                        players.get(i).setFoodLevel(30);
                        players.get(i).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 150));
                        players.get(i).teleport(startLoc);
                    }
                    if (args.length == 0) Bukkit.getServer().broadcastMessage(main.getConfig().getString("Messages.start-message"));

                }
            }
            if(cmd.getName().equalsIgnoreCase("hardcorereload")) {
                if (!p.hasPermission("hardcore.reload"))
                    p.sendMessage(main.getConfig().getString("Messages.no-permission").replace("%perm%", "hardcore.reload"));
                else {
                    main.reloadConfig();
                    Bukkit.getServer().removeRecipe(main.keyBadOmenRecipe);
                    if (main.getConfig().getBoolean("Gameplay.Recipe.craft-bad-omen")) {
                        ItemStack badOmenPotion = new ItemStack(Material.POTION);
                        ItemMeta badOmenPotionName = badOmenPotion.getItemMeta();
                        badOmenPotionName.setDisplayName(main.getConfig().getString("Gameplay.Recipe.bad-omen-name"));
                        badOmenPotion.setItemMeta(badOmenPotionName);
                        PotionMeta badOmenPotionMeta = (PotionMeta) badOmenPotion.getItemMeta();
                        badOmenPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BAD_OMEN, Integer.MAX_VALUE, main.getConfig().getInt("Gameplay.Recipe.bad-omen-level") - 1), true);
                        badOmenPotionMeta.setColor(Color.fromRGB(main.getConfig().getInt("Gameplay.Recipe.bad-omen-color.R"), main.getConfig().getInt("Gameplay.Recipe.bad-omen-color.G"), main.getConfig().getInt("Gameplay.Recipe.bad-omen-color.B")));
                        badOmenPotion.setItemMeta(badOmenPotionMeta);

                        ShapelessRecipe badOmenRecipe = new ShapelessRecipe(main.keyBadOmenRecipe, badOmenPotion);
                        badOmenRecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);
                        badOmenRecipe.addIngredient(Material.ROTTEN_FLESH);
                        badOmenRecipe.addIngredient(Material.BONE);
                        badOmenRecipe.addIngredient(Material.GUNPOWDER);
                        badOmenRecipe.addIngredient(Material.ENDER_EYE);
                        badOmenRecipe.addIngredient(Material.MAGMA_CREAM);
                        badOmenRecipe.addIngredient(Material.NETHERITE_INGOT);
                        badOmenRecipe.addIngredient(Material.GHAST_TEAR);
                        badOmenRecipe.addIngredient(Material.GLASS_BOTTLE);

                        Bukkit.getServer().addRecipe(badOmenRecipe);
                    }
                    p.sendMessage(main.getConfig().getString("Messages.reload"));

                }
            }
            return true;
        } else
        return false;
    }
}
