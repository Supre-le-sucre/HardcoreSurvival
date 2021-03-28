package fr.supre.hardcoresurvival.core;
import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

    private Main main = Main.getPlugin(Main.class);
    public FileConfiguration datas;
    public File dataFile;


    public void setup(){
        if(!main.getDataFolder().exists()) {
            main.getDataFolder().mkdir();
        }
        dataFile = new File(main.getDataFolder(),"data.yml");
        if(!dataFile.exists()) {
            System.out.println("data.yml n'existe pas, création...");
            try {
                dataFile.createNewFile();
                System.out.println("data.yml créé");
            }catch(IOException e){
                Bukkit.getServer().getConsoleSender().sendMessage("Impossible de créer le fichier ");
                e.printStackTrace();
            }
        }
        datas = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getDatas() {
        return datas;
    }


    public void saveDatas() {
        try {
            datas.save(dataFile);
        }catch(IOException e) {
            System.out.println("datas.yml n'est pas sauvergardé");
        }
    }

    public void reloadDatas() {
        datas = YamlConfiguration.loadConfiguration(dataFile);
    }
}

