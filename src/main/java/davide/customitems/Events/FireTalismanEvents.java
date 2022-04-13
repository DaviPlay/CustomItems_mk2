package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireTalismanEvents implements Listener {

    @EventHandler
    private void onFireDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;

        Player player = (Player) e.getEntity();
        ItemMeta mainMeta = player.getInventory().getItemInMainHand().getItemMeta();
        if (mainMeta == null) return;
        PersistentDataContainer mainContainer = mainMeta.getPersistentDataContainer();
        if (!mainContainer.has(Item.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.fireTalisman.getKey())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        Cooldowns.setCooldown(player.getUniqueId(), Item.fireTalisman.getKey(), Item.fireTalisman.getDelay());
    }

    @EventHandler
    private void onEat(PlayerItemConsumeEvent e) {
        ItemMeta mainMeta = e.getItem().getItemMeta();
        if (mainMeta == null) return;
        PersistentDataContainer mainContainer = mainMeta.getPersistentDataContainer();
        if (!mainContainer.has(Item.fireTalisman.getKey(), PersistentDataType.INTEGER)) return;

        e.setCancelled(true);
    }
}
