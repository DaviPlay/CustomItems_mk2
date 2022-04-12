package davide.customitems.Events;

import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UltimateBreadEvents implements Listener {

    @EventHandler
    private void onConsume(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(Item.ultimateBread.getKey(), PersistentDataType.INTEGER)) return;
        final int duration = 300;
        int newDuration;

        if (!player.hasPotionEffect(PotionEffectType.SATURATION))
            newDuration = duration;
        else
            newDuration = player.getPotionEffect(PotionEffectType.SATURATION).getDuration() + duration;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration * 20, 0));
        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }
}
