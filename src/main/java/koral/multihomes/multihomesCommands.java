package koral.multihomes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class multihomesCommands implements CommandExecutor {

    Multihomes plugin;
    public multihomesCommands(final Multihomes plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (sender instanceof Player) {

            if (label.equalsIgnoreCase("sethome") && args.length == 0) {
                player.sendMessage(ChatColor.RED + this.plugin.getConfig().getString("sethomenoargs"));
            }


            if (label.equalsIgnoreCase("sethome") && args.length > 0) {
                final String id = player.getUniqueId().toString();
                if (this.plugin.homedata.getString("Homes." + id + "." + ".Totalhomenumber") != null) {
                    ConfigurationSection cfgList = this.plugin.homes.getConfigurationSection("Homes." + id);
                    int counter = 0;

                    for (String home : cfgList.getKeys(false))                        //PRZELATYWANIE PRZEZ CALY PLIK
                    {
                        counter++;
                    }
                    int value = counter;
                    this.plugin.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                } else {
                    int value = 0;
                    int additionalValue = 0;
                    this.plugin.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                    this.plugin.homedata.set("Homes." + id + "." +  ".PlayerAdditionalHomes", (Object) additionalValue);
                    this.plugin.saveHomeDataFile();
                }
                int value;
                value = this.plugin.homedata.getInt("Homes." + id + "." + ".Totalhomenumber");
                if (this.plugin.homedata.getInt("Homes." + id + "." + ".Totalhomenumber")  - this.plugin.homedata.getInt("Homes." + id + "." + ".PlayerAdditionalHomes") < plugin.getConfig().getInt("maxhomes")) {
                    this.plugin.homename = args[0].toLowerCase();
                    final String name = player.getName();
                    final double x = player.getLocation().getX();
                    final double y = player.getLocation().getY();
                    final double z = player.getLocation().getZ();
                    final float yaw = player.getLocation().getYaw();
                    final float pitch = player.getLocation().getPitch();
                    final String worldName = player.getWorld().getName();
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".X", (Object) x);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Y", (Object) y);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Z", (Object) z);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Yaw", (Object) yaw);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Pitch", (Object) pitch);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".World", (Object) worldName);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Nickname", (Object) name);
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename + ".Homename", (Object) plugin.homename);
                    this.plugin.saveHomesFile();
                    value++;
                    this.plugin.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                    this.plugin.saveHomeDataFile();
                    player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messagesucesssethome"));
                    //  int value = map.get(player.getName());
                    //    map.replace(player.getName(), value + 1);
                    this.plugin.saveHomesFile();
                }
                else
                    player.sendMessage(ChatColor.RED + this.plugin.getConfig().getString("youneeditem") + this.plugin.getConfig().getInt("itemamount") + " " + ChatColor.DARK_RED+ this.plugin.getConfig().getString("item"));
            }


            if (label.equalsIgnoreCase("home") && args.length > 0) {
                plugin.homename = args[0].toLowerCase();

                final String id = player.getUniqueId().toString();
                if (plugin.homename.equals(this.plugin.homes.getString("Homes." + id + "." + plugin.homename + ".Homename"))) {
                    final double x = this.plugin.homes.getDouble("Homes." + id + "." + plugin.homename + ".X");
                    final double y = this.plugin.homes.getDouble("Homes." + id + "." + plugin.homename + ".Y");
                    final double z = this.plugin.homes.getDouble("Homes." + id + "." + plugin.homename + ".Z");
                    final float yaw = (float) this.plugin.homes.getLong("Homes." + id + "." + plugin.homename + ".Yaw");
                    final float pitch = (float) this.plugin.homes.getLong("Homes." + id + "." + plugin.homename + ".Pitch");
                    final World world = Bukkit.getWorld(this.plugin.homes.getString("Homes." + id + "." + plugin.homename + ".World"));
                    final Location home = new Location(world, x, y, z, yaw, pitch);
                    Location a = player.getLocation();
                    if(!player.hasPermission("multihomes.bypass.delay")) {
                        player.sendMessage(ChatColor.RED + this.plugin.getConfig().getString("youwillbetp") +ChatColor.DARK_RED + this.plugin.getConfig().getInt("delaytime") + "s");
                        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                            public void run() {
                                Location b = player.getLocation();
                                if(a.equals(b)) {
                                    player.teleport(home);
                                    player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messagesucesshome") + args[0]);
                                }
                                else {
                                    player.sendMessage(ChatColor.RED + plugin.getConfig().getString("tpcanceled"));
                                    Bukkit.getScheduler().cancelTasks(plugin);
                                }
                            }
                        }, 20L * plugin.getConfig().getInt("delaytime"));// 60 L == 3 sec, 20 ticks == 1 sec
                    }
                    else
                    {
                        player.teleport(home);
                        player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messagesucesshome") + ChatColor.DARK_RED +args[0]);
                    }

                }
                else player.sendMessage(ChatColor.RED + plugin.getConfig().getString("sethomenull"));
// this.homes total -  additionalhomes >= config limiter;

            }


            if (label.equalsIgnoreCase("home") && args.length == 0) {
                final String id = player.getUniqueId().toString();
                ConfigurationSection cfgList = this.plugin.homes.getConfigurationSection("Homes." + id);
                List<String> list = new ArrayList<>(); // LISTA

                if (cfgList == null || cfgList.getKeys(false).size() == 0) {
                    player.sendMessage(ChatColor.RED + plugin.getConfig().getString("homelistnull"));
                    return true;
                }
                for (String home : cfgList.getKeys(false))                        //PRZELATYWANIE PRZEZ CALY PLIK
                {
                    // player.sendMessage("Home: " + cfgList.getString(home + ".Homename"));      //Wyswietlanie kazdego po kolei
                    list.add(home);
                    // DODAWANIE DO LISTY KAZDEGO KLUCZA PRZEZ FORA
                }
                player.sendMessage(ChatColor.DARK_RED + plugin.getConfig().getString("homelist") + ChatColor.RED + list.toString());

            }

            if (label.equalsIgnoreCase("homehelp")) {
                player.sendMessage(ChatColor.RED + "/home" + ChatColor.YELLOW + plugin.getConfig().getString("home"));
                player.sendMessage(ChatColor.RED + "/sethome <arg>" + ChatColor.YELLOW + plugin.getConfig().getString("sethome"));
                player.sendMessage(ChatColor.RED + "/delhome <arg>" + ChatColor.YELLOW + plugin.getConfig().getString("delhome"));
            }


            if (label.equalsIgnoreCase("delhome") && args.length > 0) {
                final String id = player.getUniqueId().toString();
                plugin.homename = args[0].toLowerCase();
                if (plugin.homename.equals(this.plugin.homes.getString("Homes." + id + "." + plugin.homename + ".Homename"))) {
                    this.plugin.homes.set("Homes." + id + "." + plugin.homename, (Object) null);
                    this.plugin.saveHomesFile();
                    player.sendMessage(ChatColor.RED + plugin.getConfig().getString("youdeletedhome") + args[0]);
                    int value = this.plugin.homedata.getInt("Homes." + id + "." + ".Totalhomenumber");
                    value--;
                    this.plugin.homedata.set("Homes." + id + "." + ".Totalhomenumber", (Object) value);
                    this.plugin.saveHomeDataFile();
                    //   int value = map.get(player.getName());
                    //  map.replace(player.getName(), value - 1);
                } else
                    player.sendMessage( ChatColor.RED + plugin.getConfig().getString("unknownhome")  + ChatColor.DARK_RED + args[0]);
            }


            if (label.equalsIgnoreCase("delhome") && args.length == 0) {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("delhomenoargs"));
            }


        }
        return true;
    }





}