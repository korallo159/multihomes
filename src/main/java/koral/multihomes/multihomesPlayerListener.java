package koral.multihomes;

import org.bukkit.Material;
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
        String mats;

        if ( action.equals( Action.RIGHT_CLICK_AIR ) || action.equals( Action.RIGHT_CLICK_BLOCK ) ) {
            if ( item != null && item.getType() == Material.DIAMOND) {
                player.sendMessage( "You have right click a diamond!" );
                item.setAmount(item.getAmount() -1);

            }
        }

    }

}
