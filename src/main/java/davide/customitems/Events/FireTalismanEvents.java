package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.ItemList;
import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireTalismanEvents implements Listener {

    @EventHandler
    private void onFireDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.LAVA) return;

        Player player = (Player) e.getEntity();

        int first = player.getInventory().first(ItemList.fireTalisman.getItemStack().getType());
        ItemStack item = first == -1 ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(first);

        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.fireTalisman.getKey())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

        item.setAmount(item.getAmount() - 1);
        Cooldowns.setCooldown(player.getUniqueId(), ItemList.fireTalisman.getKey(), ItemList.fireTalisman.getDelay());
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent e) {
        ItemMeta meta = e.getItem().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        e.setCancelled(true);
    }
}
