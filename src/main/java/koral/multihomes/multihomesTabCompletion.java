package koral.multihomes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class multihomesTabCompletion implements TabCompleter {
    Multihomes plugin;

    public multihomesTabCompletion(final Multihomes plugin) {
        this.plugin = plugin;
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String  label, String[] args)
    {
        Player player = (Player) sender;
          if(args.length <= 1){

              final String id = player.getUniqueId().toString();
              ConfigurationSection cfgList = this.plugin.homes.getConfigurationSection("Homes." + id);
              if (cfgList == null || cfgList.getKeys(false).size() == 0) {
                  return null;
              }
              List<String> lista = new ArrayList<>(); // LISTA
              for (String home : cfgList.getKeys(false))                        //PRZELATYWANIE PRZEZ CALY PLIK
              {
                  lista.add(home);
                  // DODAWANIE DO LISTY KAZDEGO KLUCZA PRZEZ FORA
              }
              return lista;

          }
          return null;
    }


}
