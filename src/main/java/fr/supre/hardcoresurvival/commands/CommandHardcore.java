package fr.supre.hardcoresurvival.commands;

import fr.supre.hardcoresurvival.core.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            if(cmd.getName().equalsIgnoreCase("revive") && p.hasPermission("hardcore.revive")) {
                if(args.length != 0) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if(Bukkit.getServer().getOnlinePlayers().contains(target)) {
                        if(main.cfmg.getDatas().getStringList("Datas.dead").contains(String.valueOf(target.getUniqueId()))) {
                            int x = main.cfmg.getDatas().getInt("Datas."+target.getUniqueId()+".X");
                            int y = main.cfmg.getDatas().getInt("Datas."+target.getUniqueId()+".Y");
                            int z = main.cfmg.getDatas().getInt("Datas."+target.getUniqueId()+".Z");
                            Location deathLoc = new Location(p.getWorld(), x+1,y+1,z);
                            target.teleport(deathLoc);
                            target.setGameMode(GameMode.SURVIVAL);
                            Bukkit.getServer().broadcastMessage("§2"+target.getName()+" §eest revenu d'entre les morts grâce à §d" + p.getName());
                            p.getWorld().spawnParticle(Particle.SPELL_WITCH, target.getLocation(), 150);
                            p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation(), 50);
                            p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation(), 75);
                            p.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 1.5F);
                            List<String> deadList = main.cfmg.getDatas().getStringList("Datas.dead");
                            deadList.remove(String.valueOf(target.getUniqueId()));
                            main.cfmg.getDatas().set("Datas.dead", deadList);
                            main.cfmg.getDatas().set("Datas."+target.getUniqueId(), null);
                            main.cfmg.saveDatas();
                        } else p.sendMessage("§c§lLe joueur n'est pas mort !");
                    } else p.sendMessage("§c§lVous ne pouvez pas revive un joueur hors-ligne");
                } else p.sendMessage("§c§lVous devez sépcifier un nom de joueur: §6/revive <nom>");
            }
            //---------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("sacrifice") && !p.getGameMode().equals(GameMode.SPECTATOR)) {
                p.getInventory().clear();
                p.setHealth(0);
                Bukkit.getServer().broadcastMessage("§6"+p.getName()+" §cs'est sacrifié pour un totem d'immortalité");
                p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(Material.TOTEM_OF_UNDYING));
            }
            //----------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("tpto") && p.getGameMode().equals(GameMode.SPECTATOR)) {
                if(args.length != 0) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if(Bukkit.getServer().getOnlinePlayers().contains(target)) {
                        p.teleport(target.getLocation());
                    } else p.sendMessage("§c§lVous ne pouvez pas revive un joueur hors-ligne");
                } else p.sendMessage("§c§lTu dois spécifier le nom d'un joueur §6/tpto <nom du joueur>");
            }
            //----------------------------------------------------------------------------------------------
            if(cmd.getName().equalsIgnoreCase("start") && p.hasPermission("hardcore.start")) {
                int definition1 = 0;
                int definition2 = 1;
                int definition3 = 2;
                int definition4 = 3;
                ArrayList<Player> players = new ArrayList<Player>();
                if(args.length == 0) {
                    Collection<? extends Player> playerCollection = Bukkit.getServer().getOnlinePlayers();;
                    players = new ArrayList(playerCollection);
                }
                else {
                    for(int i=0; i<args.length ; i++) {
                        Player target = Bukkit.getPlayer(args[i]);
                        if(Bukkit.getServer().getOnlinePlayers().contains(target)) {
                            players.add(target);
                            p.sendMessage("§6[§4Hardore§6] §cLe jeu a commencé pour §e"+ target.getName());
                            target.sendMessage("§6[§4Hardcore§6] §cBonne chance ! >:3");
                        }
                        else p.sendMessage("§c§lVous ne pouvez pas faire commencer le jeu à un joueur hors-ligne");
                    }

                }
                for(int i = 0; i < players.size() ; i++) {
                    int x = 0;
                    int z = 0;
                    if(i == definition1) {
                        x = -2500;
                        z = -2500;
                        definition1 = definition1 + 4;
                    }
                    else if(i == definition2) {
                        x = 2500;
                        z = -2500;
                        definition2 = definition2 + 4;
                    }
                    else if(i == definition3) {
                        x = 2500;
                        z = 2500;
                        definition3 = definition3 + 4;
                    }
                    else if(i == definition4) {
                        x = -2500;
                        z = 2500;
                        definition4 = definition4 + 4;
                    }
                    Location startLoc = new Location(p.getWorld(), x, 150, z);
                    players.get(i).getInventory().clear();
                    players.get(i).setHealth(20);
                    players.get(i).setFoodLevel(30);
                    players.get(i).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, Integer.MAX_VALUE));
                    players.get(i).teleport(startLoc);
                }
                if(args.length == 0) Bukkit.getServer().broadcastMessage("§6[§4Hardcore§6] §cBonne chance ! >:3");

            }
            return true;
        } else
        return false;
    }
}
