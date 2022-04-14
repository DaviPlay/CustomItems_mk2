package davide.customitems.Events;

import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.Cooldowns;
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

public class CocaineEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (!(e.getHand() == EquipmentSlot.HAND)) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.cocaine.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.cocaine.getKey())) {
            player.sendMessage(Cooldowns.timeLeft(player.getUniqueId(), Item.cocaine.getKey()));
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10 * 20, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8 * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 8 * 20, 1));

        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        Cooldowns.setCooldown(player.getUniqueId(), Item.cocaine.getKey(), Item.cocaine.getDelay());
    }
}
