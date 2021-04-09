package fr.supre.hardcoresurvival.listeners;

import com.mojang.authlib.GameProfile;
import fr.supre.hardcoresurvival.core.Main;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
//Créé quasi entièrement par Bistouri
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Listeners implements Listener {
    static int playerSleeping;
    private final Main main;

    public Listeners(Main main) {
            this.main = main;
        }
    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {

        Player p = event.getEntity();
        Location ploc = p.getLocation();
        p.getWorld().playSound(ploc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 2F);
        List<String> dataList = main.cfmg.getDatas().getStringList("Datas.dead");
        dataList.add(String.valueOf(p.getUniqueId()));
        main.cfmg.getDatas().set("Datas.dead", dataList);
        main.cfmg.getDatas().getStringList("Datas").add(String.valueOf(p.getUniqueId()));
        main.cfmg.getDatas().getStringList("Datas."+p.getUniqueId()).add("X");
        main.cfmg.getDatas().set("Datas."+p.getUniqueId()+".X", ploc.getBlockX()+1);
        main.cfmg.getDatas().getStringList("Datas."+p.getUniqueId()).add("Y");
        main.cfmg.getDatas().set("Datas."+p.getUniqueId()+".Y", ploc.getBlockY());
        main.cfmg.getDatas().getStringList("Datas."+p.getUniqueId()).add("Z");
        main.cfmg.getDatas().set("Datas."+p.getUniqueId()+".Z", ploc.getBlockZ());
        main.cfmg.saveDatas();
        new Location(ploc.getWorld(), ploc.getX()+1, ploc.getY()-1,ploc.getZ()).getBlock().setType(Material.SOUL_SAND);
        new Location(ploc.getWorld(), ploc.getX()+2, ploc.getY()-1,ploc.getZ()).getBlock().setType(Material.SOUL_SAND);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY(),ploc.getZ()).getBlock().setType(Material.MOSSY_STONE_BRICKS);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY()+1,ploc.getZ()).getBlock().setType(Material.CRACKED_STONE_BRICKS);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY()+2,ploc.getZ()).getBlock().setType(Material.CRACKED_STONE_BRICKS);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY()+3,ploc.getZ()).getBlock().setType(Material.MOSSY_STONE_BRICKS);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY()+2,ploc.getZ()+1).getBlock().setType(Material.MOSSY_STONE_BRICKS);
        new Location(ploc.getWorld(), ploc.getX(), ploc.getY()+2,ploc.getZ()-1).getBlock().setType(Material.CRACKED_STONE_BRICKS);
        String d  = event.getDeathMessage();
        event.setDeathMessage("§c"+d+"§e en §6X:" + p.getLocation().getBlockX()+ " §6Y:" + p.getLocation().getBlockY() +" §6Z:"+ p.getLocation().getBlockZ() + " §epaix a son âme...");
        p.setGameMode(GameMode.SPECTATOR);
        Stream<Player> all = getConnectedPlayers().stream();
        all.forEach((pl) -> pl.playSound(pl.getLocation(), Sound.MUSIC_DISC_WARD, 100, 1F));
        BukkitTask music = new BukkitRunnable()
        {
            int s = 0;
            @Override
            public void run() {
                s++;
                if(s == 8) {
                    all.forEach((pl -> pl.stopSound(Sound.MUSIC_DISC_WARD)));
                    cancel();
                }
            }
        }.runTaskTimer(this.main, 0, 20);
    }
    @EventHandler
    public void onSleeping(PlayerBedEnterEvent event) {
        if (!event.isCancelled()) {
            playerSleeping++;
            Bukkit.broadcastMessage("§a" + event.getPlayer().getName() + " §eest en train de dormir ! §c§lAU DODO ! \n \n§eIl y a §a" + playerSleeping + " §6/ §c" + getNumberOfRequiredSleep() + "§e qui font dodo...");
        }
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!event.getPlayer().hasPermission("hardcore.chat")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c§lLa communication sur le tchat est désactivée !");
        }
    }
    @EventHandler
    public void onNotSleeping(PlayerBedLeaveEvent event) {
        playerSleeping--;
        Bukkit.broadcastMessage("§c"+ event.getPlayer().getName() + " §ene dors plus...\n \n§eIl y a §a" + playerSleeping + " §6/ §c" + getNumberOfRequiredSleep() + "§e qui font dodo...");
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
       if(event.getItemDrop().getItemStack().equals(new ItemStack(Material.TOTEM_OF_UNDYING))) {
           List<String> uuidList = main.cfmg.getDatas().getStringList("Datas.dead");
           for(String s: uuidList){
               int x = main.cfmg.getDatas().getInt("Datas."+s+".X");
               int y = main.cfmg.getDatas().getInt("Datas."+s+".Y");
               int z = main.cfmg.getDatas().getInt("Datas."+s+".Z");
               Location deathLoc = new Location(event.getPlayer().getWorld(),x+1,y+1,z);
               if(event.getItemDrop().getLocation().getBlockX() == x && event.getItemDrop().getLocation().getBlockY() == y+1 && event.getItemDrop().getLocation().getBlockZ() == z){
                   Player p = null;
                    for(Player pl: main.getServer().getOnlinePlayers()){
                        if(pl.getUniqueId().toString().equals(s)){
                            p = pl;
                            break;
                        }
                    }
                    if(p == null){
                        event.getPlayer().sendMessage("§c§lTu ne peux pas revive quelqu'un d'offline");
                        return;
                    }else{
                        p.teleport(deathLoc);
                        p.setGameMode(GameMode.SURVIVAL);
                        Bukkit.getServer().broadcastMessage("§2"+p.getName()+" §eest revenu d'entre les morts grâce à §d" + event.getPlayer().getName());
                        p.getWorld().spawnParticle(Particle.SPELL_WITCH, event.getItemDrop().getLocation(), 150);
                        p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, event.getItemDrop().getLocation(), 50);
                        p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, event.getItemDrop().getLocation(), 75);
                        p.getWorld().playSound(event.getItemDrop().getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 1.5F);
                        List<String> deadList = main.cfmg.getDatas().getStringList("Datas.dead");
                        deadList.remove(String.valueOf(p.getUniqueId()));
                        main.cfmg.getDatas().set("Datas.dead", deadList);
                        main.cfmg.getDatas().set("Datas."+p.getUniqueId(), null);
                        main.cfmg.saveDatas();
                        event.getItemDrop().remove();
                    }
                   break;
               }
           }
        }
    }
    int getNumberOfRequiredSleep() {
        int n = 0;
        ArrayList<Player> Online =  new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
        int total = Online.size();
        for(int i=0; i<Online.size(); i++) {
            if(Online.get(i).getGameMode().equals(GameMode.SPECTATOR)) n++;
            else if(!Online.get(i).getWorld().getEnvironment().equals(World.Environment.NORMAL)) n++;

        }
        return total-n;
    }

    ArrayList<Player> getConnectedPlayers() {
        ArrayList<Player> connected = new ArrayList<Player>(Bukkit.getServer().getOnlinePlayers());
        return connected;
    }


}
