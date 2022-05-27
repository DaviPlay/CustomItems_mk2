package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.ItemAbilities;
import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.UUIDDataType;
import davide.customitems.Lists.ItemList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class ThrowingAxeEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.throwingAxe.getKey(), new UUIDDataType())) return;

        UUID uuid = container.get(ItemList.throwingAxe.getKey(), new UUIDDataType());

        if (Cooldowns.checkCooldown(uuid, ItemList.throwingAxe.getKey())) {
            player.sendMessage(Cooldowns.inCooldownMessage(uuid, ItemList.throwingAxe.getKey()));
            return;
        }

        ItemAbilities.throwItem(player, is, 10);

        Cooldowns.setCooldown(uuid, ItemList.throwingAxe.getKey(), ItemList.throwingAxe.getDelay());
    }
}
