package davide.customitems.Events;

import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.Cooldowns;
import davide.customitems.API.ItemList;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AspectOfTheEndEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.aspectOfTheEnd.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.aspectOfTheEnd.getKey())) {
            player.sendMessage(Cooldowns.timeLeft(player.getUniqueId(), ItemList.aspectOfTheEnd.getKey()));
            return;
        }

        Block b = player.getTargetBlock(null, 8);
        Location loc = new Location(b.getWorld(), b.getX(), b.getY() + 0.5f, b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.aspectOfTheEnd.getKey(), ItemList.aspectOfTheEnd.getDelay());
    }
}
