package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
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
        ItemStack item;

        int first = player.getInventory().first(Item.fireTalisman.getItemStack().getType());
        if (first == -1)
            item = player.getInventory().getItemInOffHand();
        else
            item = player.getInventory().getItem(first);

        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.fireTalisman.getKey())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

        item.setAmount(item.getAmount() - 1);
        Cooldowns.setCooldown(player.getUniqueId(), Item.fireTalisman.getKey(), Item.fireTalisman.getDelay());
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent e) {
        ItemMeta meta = e.getItem().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        e.setCancelled(true);
    }
}