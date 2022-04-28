package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.ItemList;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HookShotEvents implements Listener {

    @EventHandler
    private void onFish(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.hookShot.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.hookShot.getKey())) {
            player.sendMessage("§cWoah, slow down there");
            return;
        }

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.75).setY(1.25));

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.hookShot.getKey(), ItemList.hookShot.getDelay());
    }
}
