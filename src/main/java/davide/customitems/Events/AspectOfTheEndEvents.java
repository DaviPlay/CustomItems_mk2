package davide.customitems.Events;

import davide.customitems.API.ClickableBlocks;
import davide.customitems.API.Cooldowns;
import davide.customitems.ItemCreation.Item;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class AspectOfTheEndEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (ClickableBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.aspectOfTheEnd.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.aspectOfTheEnd.getKey())) {
            player.sendMessage(Cooldowns.timeLeft(player.getUniqueId(), Item.aspectOfTheEnd.getKey()));
            return;
        }

        Block b = player.getTargetBlock(null, 8);
        Location loc = new Location(b.getWorld(), b.getX(), b.getY() + 0.5f, b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        Cooldowns.setCooldown(player.getUniqueId(), Item.aspectOfTheEnd.getKey(), Item.aspectOfTheEnd.getDelay());
    }
}
