package davide.customitems.Events;

import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.ItemList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.ultimateBread.getKey(), PersistentDataType.INTEGER)) return;

        final int duration = 300;
        int newDuration = player.hasPotionEffect(PotionEffectType.SATURATION) ? player.getPotionEffect(PotionEffectType.SATURATION).getDuration() + duration : duration;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration * 20, 0));
        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }
}
