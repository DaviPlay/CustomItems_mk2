package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.ItemAbilities;
import davide.customitems.ItemCreation.Item;
import davide.customitems.Lists.ItemList;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ProtectorArmorEvents implements Listener {
    private final Item[] armor = { ItemList.protectorBoots, ItemList.protectorLeggings, ItemList.protectorChestplate, ItemList.protectorHelmet };

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        if (ItemAbilities.checkFullSet(armorContents, armor))
            return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
            return;

        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (e.getDamage() > 0.25 * maxHealth) return;

        e.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.sendMessage("§cYou have been protected!");

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getDelay());
    }
}
