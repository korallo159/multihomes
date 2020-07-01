package koral.multihomes;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
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

    public void CheckConfig() {
        if (getConfig().get("Name") == null) { //if the setting has been deleted it will be null
            getConfig().set("Name", "Value"); //reset the setting
            saveConfig();
            reloadConfig();
        }



    }




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
            getConfig().addDefault("messagesucesshome", "Zostałeś przeteleportowany do "); //adding default settings
            getConfig().addDefault("messagesucesssethome", "Dom ustawiony"); //adding default settings
            getConfig().addDefault("sethomenoargs", "Poprawne użycie /sethome <nazwa domu>"); //adding default settings
            getConfig().addDefault("sethomenull", "Taki dom nie istnieje"); //adding default settings
            getConfig().addDefault("homelist", "Lista domów :"); //adding default settings
            getConfig().addDefault("homelistnull", "Nie masz żadnych domów"); //adding default settings
            getConfig().addDefault("delhomenoargs", "Poprawne użycie /delhome <nazwa domu>"); //adding default settings
            getConfig().addDefault("youdeletedhome", "Usunąleś dom: "); //adding default settings
            getConfig().addDefault("unknownhome", "Nie masz zapisanego domu : "); //adding default settings
            getConfig().addDefault("maxhomenumber", "Nie masz zapisanego domu : ");
            getConfig().addDefault("material", DIAMOND); //adding default settings


            //Save the default settings
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            //situation B, Config does exist
            CheckConfig(); //function to check the important settings
            saveConfig(); //saves the config
            reloadConfig();    //reloads the config
            // Plugin startup logic
        }
        this.playerListener = new multihomesPlayerListener(this);
        getServer().getPluginManager().registerEvents(this.playerListener, this);
        this.commandExecutor = new multihomesCommands(this);
        this.getCommand("sethome").setExecutor(this.commandExecutor);
        this.getCommand("home").setExecutor(this.commandExecutor);
        this.getCommand("delhome").setExecutor(this.commandExecutor);
        this.getCommand("homehelp").setExecutor(this.commandExecutor);
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