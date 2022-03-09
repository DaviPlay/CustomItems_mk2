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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StonkEvents implements Listener {

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
        if (!container.has(Item.stonk.key, PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.stonk.key)) {
            player.sendMessage("§cThe ability is on cooldown for " + Cooldowns.timeLeft(player.getUniqueId(), Item.stonk.key) + " seconds!");
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 4));
        Cooldowns.setCooldown(player.getUniqueId(), Item.stonk.key, Item.stonk.getDelay());
    }
}
