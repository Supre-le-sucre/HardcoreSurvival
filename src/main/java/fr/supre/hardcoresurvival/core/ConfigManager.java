package fr.supre.hardcoresurvival.core;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

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
            Bukkit.getLogger().log(Level.INFO,"[Hardcore] data.yml does not exist ! Creating...");
            try {
                dataFile.createNewFile();
                Bukkit.getLogger().log(Level.INFO,"[Hardcore] data.yml was created successfully");
            }catch(IOException e){
                Bukkit.getLogger().log(Level.SEVERE,"[Hardcore] data.yml could not be created... Is all writing permissions were set correctly ? Check the exception Stack Trace for more info.");
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
            Bukkit.getLogger().log(Level.SEVERE, "[Hardcore] data.yml could not be saved... Is all writing permissions were set correctly ? Check the exception Stack Trace for more info.");
            e.printStackTrace();
        }
    }

    public void reloadDatas() {
        datas = YamlConfiguration.loadConfiguration(dataFile);
    }
}

