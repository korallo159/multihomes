package koral.multihomes;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

import static org.bukkit.Material.*;

public final class Multihomes extends JavaPlugin implements Listener, CommandExecutor {
    private multihomesCommands commandExecutor;
    private multihomesPlayerListener playerListener;


    File homesFile;
    File homedataFile;
    YamlConfiguration homedata;
    YamlConfiguration homes;
    String homename;






    public Multihomes() {
        this.homesFile = new File(this.getDataFolder(), "Homes.yml");
        this.homes = YamlConfiguration.loadConfiguration(this.homesFile);
        this.homedataFile = new File(this.getDataFolder(), "homedata.yml");
        this.homedata = YamlConfiguration.loadConfiguration(this.homedataFile);

    }





    @Override
    public void onEnable() {
        File file = new File(getDataFolder() + File.separator + "config.yml"); //This will get the config file
        if (!file.exists()) { //This will check if the file exist
            //Situation A, File doesn't exist




            //Save the default settings
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            //situation B, Config does exist
            saveConfig(); //saves the config
            reloadConfig();    //reloads the config
            // Plugin startup logic
        }
        this.playerListener = new multihomesPlayerListener(this);
        getServer().getPluginManager().registerEvents(this.playerListener, this);
        this.commandExecutor = new multihomesCommands(this);
        this.getCommand("sethome").setExecutor(this.commandExecutor);
        this.getCommand("home").setExecutor(this.commandExecutor);
        this.getCommand("home").setTabCompleter(new multihomesTabCompletion(this));
        this.getCommand("delhome").setExecutor(this.commandExecutor);
        this.getCommand("delhome").setTabCompleter(new multihomesTabCompletion(this));
        this.getCommand("homehelp").setExecutor(this.commandExecutor);
        this.getCommand("homereload").setExecutor(this.commandExecutor);
        // Plugin startup logic

    }



    @Override
    public void onDisable() {
        this.saveHomesFile();
        this.saveHomeDataFile();
        // Plugin shutdown logic
    }




    public void saveHomesFile() {
        try {
            this.homes.save(this.homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveHomeDataFile() {
        try {
            this.homedata.save(this.homedataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}