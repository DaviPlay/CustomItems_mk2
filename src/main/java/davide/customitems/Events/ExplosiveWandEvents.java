package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.ClickableBlocks;
import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ExplosiveWandEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (ClickableBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (!(e.getHand() == EquipmentSlot.HAND)) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.explosiveWand.key, PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.explosiveWand.key)) {
            player.sendMessage("§cThe ability is on cooldown for " + Cooldowns.timeLeft(player.getUniqueId(), Item.explosiveWand.key) + " seconds!");
            return;
        }

        player.getWorld().createExplosion(player.getLocation(), 3);
        Cooldowns.setCooldown(player.getUniqueId(), Item.explosiveWand.key, Item.explosiveWand.getDelay());
    }
}
