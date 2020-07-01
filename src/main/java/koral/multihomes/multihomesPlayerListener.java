package koral.multihomes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class multihomesPlayerListener implements Listener {

    Multihomes plugin;

    public multihomesPlayerListener(final Multihomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        String id = player.getUniqueId().toString();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                if (item != null && item.getType() == Material.valueOf(this.plugin.getConfig().getString("item"))) {
                    player.sendMessage(ChatColor.RED + this.plugin.getConfig().getString("itemconsumed"));
                    if(item.getAmount()>=plugin.getConfig().getInt("itemamount")) {
                        item.setAmount(item.getAmount() - plugin.getConfig().getInt("itemamount"));
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1, 1);
                        int additionalValue;
                        additionalValue = this.plugin.homedata.getInt("Homes." + id + ".PlayerMaxHomes");
                        additionalValue++;
                        this.plugin.homedata.set("Homes." + id + "." + ".PlayerAdditionalHomes", (Object) additionalValue);
                        this.plugin.saveHomeDataFile();
                    }

                }



            }
        }

    }
