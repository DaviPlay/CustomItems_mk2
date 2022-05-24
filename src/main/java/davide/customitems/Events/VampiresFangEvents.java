package davide.customitems.Events;

import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class VampiresFangEvents implements Listener {

    @EventHandler
    private void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getDamager();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(item.getKey(), PersistentDataType.INTEGER)) return;

        double amountToHeal = (e.getDamage() * 25) / 100;

        player.setHealth(player.getHealth() + amountToHeal);
    }
}
