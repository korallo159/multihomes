package koral.multihomes;

import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.*;

import static org.bukkit.Material.*;

public final class Multihomes extends JavaPlugin implements Listener, CommandExecutor {
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
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("sethome");
        this.getCommand("home");
        this.getCommand("delhome");
        // Plugin startup logic



    }



    @Override
    public void onDisable() {
        this.saveHomesFile();
        this.saveHomeDataFile();
        // Plugin shutdown logic
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {

            if (label.equalsIgnoreCase("sethome") && args.length == 0)
            {
                player.sendMessage(ChatColor.RED + getConfig().getString("sethomenoargs"));
            }
            if (label.equalsIgnoreCase("sethome") && args.length > 0) {
                final String id = player.getUniqueId().toString();
          if (this.homedata.getString("Homes." + id + "." + ".Totalhomenumber") != null) {
              ConfigurationSection cfgList = this.homes.getConfigurationSection("Homes." + id);
              int counter = 0;

              for (String home : cfgList.getKeys(false))                        //PRZELATYWANIE PRZEZ CALY PLIK
              {
                  counter++;
              }
              int value = counter;
              this.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
          }
          else
              {
                  int value = 0;
                  this.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                  this.saveHomeDataFile();
              }
         int value;
          value = this.homedata.getInt("Homes." + id + "." + ".Totalhomenumber");
 if(this.homedata.getInt("Homes." + id + "." + ".Totalhomenumber") >= getConfig().getInt("maxhomes"))
                this.homename = args[0].toLowerCase();
                final String name = player.getName();
                final double x = player.getLocation().getX();
                final double y = player.getLocation().getY();
                final double z = player.getLocation().getZ();
                final float yaw = player.getLocation().getYaw();
                final float pitch = player.getLocation().getPitch();
                final String worldName = player.getWorld().getName();
                this.homes.set("Homes." + id + "." + homename + ".X", (Object) x);
                this.homes.set("Homes." + id + "." + homename + ".Y", (Object) y);
                this.homes.set("Homes." + id + "." + homename + ".Z", (Object) z);
                this.homes.set("Homes." + id + "." + homename + ".Yaw", (Object) yaw);
                this.homes.set("Homes." + id + "." + homename + ".Pitch", (Object) pitch);
                this.homes.set("Homes." + id + "." + homename + ".World", (Object) worldName);
                this.homes.set("Homes." + id + "." + homename + ".Nickname", (Object) name);
                this.homes.set("Homes." + id + "." + homename + ".Homename", (Object) homename);
                this.saveHomesFile();
                value++;
                this.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                this.saveHomeDataFile();
                player.sendMessage(ChatColor.GREEN + getConfig().getString("messagesucesssethome"));
              //  int value = map.get(player.getName());
            //    map.replace(player.getName(), value + 1);
                this.saveHomesFile();
                player.sendMessage("Aktualna liczba home: " + value);
            }



            if (label.equalsIgnoreCase("home") && args.length > 0) {
                homename = args[0].toLowerCase();

                final String id = player.getUniqueId().toString();
                if(homename.equals(this.homes.getString("Homes." + id + "." + homename + ".Homename"))) {
                    final double x = this.homes.getDouble("Homes." + id + "." + homename + ".X");
                    final double y = this.homes.getDouble("Homes." + id + "." + homename + ".Y");
                    final double z = this.homes.getDouble("Homes." + id + "." + homename + ".Z");
                    final float yaw = (float) this.homes.getLong("Homes." + id + "." + homename + ".Yaw");
                    final float pitch = (float) this.homes.getLong("Homes." + id + "." + homename + ".Pitch");
                    final World world = Bukkit.getWorld(this.homes.getString("Homes." + id + "." + homename + ".World"));
                    final Location home = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(home);
                    player.sendMessage(ChatColor.GREEN + getConfig().getString("messagesucesshome") + ChatColor.RED + args[0]);

                }
                else player.sendMessage(ChatColor.RED + getConfig().getString("sethomenull"));
// this.homes total -  additionalhomes >= config limiter;

            }

            if (label.equalsIgnoreCase("home") && args.length == 0)
            {
                final String id = player.getUniqueId().toString();
                ConfigurationSection cfgList = this.homes.getConfigurationSection("Homes." + id);
                List<String> list = new ArrayList<>(); // LISTA

                if (cfgList == null || cfgList.getKeys(false).size() == 0) {
                    player.sendMessage( ChatColor.RED + getConfig().getString("homelistnull"));
                    return true;
                }
                for (String home : cfgList.getKeys(false))                        //PRZELATYWANIE PRZEZ CALY PLIK
                {
                   // player.sendMessage("Home: " + cfgList.getString(home + ".Homename"));      //Wyswietlanie kazdego po kolei
                    list.add(home);
                                                                                                 // DODAWANIE DO LISTY KAZDEGO KLUCZA PRZEZ FORA
                }
                player.sendMessage(ChatColor.DARK_RED + getConfig().getString("homelist") + ChatColor.RED+ list.toString());

            }
            if(label.equalsIgnoreCase("homehelp"))
            {
                  player.sendMessage(ChatColor.RED + "/home" + ChatColor.YELLOW +" - Wyswietlanie listy domow");
                player.sendMessage(ChatColor.RED + "/sethome nazwa" + ChatColor.YELLOW +" - Ustawianie domu");
                player.sendMessage(ChatColor.RED + "/delhome nazwa" + ChatColor.YELLOW +" - Usuwanie domu");
            }

            if (label.equalsIgnoreCase("delhome") && args.length > 0)
            {
                final String id = player.getUniqueId().toString();
                homename = args[0].toLowerCase();
                if(homename.equals(this.homes.getString("Homes." + id + "." + homename + ".Homename")))
                {
                    this.homes.set("Homes." + id + "." + homename, (Object) null);
                    this.saveHomesFile();
                    player.sendMessage(ChatColor.RED + getConfig().getString("youdeletedhome") + args[0]);
                    int value = this.homedata.getInt("Homes." + id + "." + ".Totalhomenumber");
                    value--;
                    this.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                    this.saveHomeDataFile();
                 //   int value = map.get(player.getName());
                  //  map.replace(player.getName(), value - 1);
                }
                else
                    player.sendMessage(args[0] + getConfig().getString("unknownhome"));
            }

            if (label.equalsIgnoreCase("delhome") && args.length == 0)
            {
                player.sendMessage(ChatColor.RED + getConfig().getString("delhomenoargs"));
            }




        }
        return true;
    }


 /*   @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        final String id = p.getUniqueId().toString();
        int value = 0;
        this.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
        this.saveHomeDataFile();
    }


  */


/*@EventHandler
         public void onPlayerJoin(PlayerJoinEvent e)
{

    Player p = e.getPlayer();
    if(!p.hasPlayedBefore())
    {
        int value = 0;
        final String id = p.getUniqueId().toString();
        this.homes.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
     saveHomesFile();
    }
    int value = 0;
    final String id = p.getUniqueId().toString();
    this.homes.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
    this.saveHomesFile();
}

 */

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        String mats;

        if ( action.equals( Action.RIGHT_CLICK_AIR ) || action.equals( Action.RIGHT_CLICK_BLOCK ) ) {
            if ( item != null && item.getType() == Material.valueOf("material")) {
                player.sendMessage( "You have right click a diamond!" );
                item.setAmount(item.getAmount() -1);

            }
        }

    }





        private void saveHomesFile () {
            try {
                this.homes.save(this.homesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void saveHomeDataFile () {
        try {
            this.homedata.save(this.homedataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    }
