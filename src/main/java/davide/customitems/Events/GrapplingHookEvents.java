package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.ItemCreation.Item;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GrapplingHookEvents implements Listener {

    @EventHandler
    private void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.grapplingHook.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.grapplingHook.getKey())) {
            player.sendMessage("§cWoah, slow down there");
            return;
        }

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.3).setY(1));

        Cooldowns.setCooldown(player.getUniqueId(), Item.grapplingHook.getKey(), Item.grapplingHook.getDelay());
    }
}
