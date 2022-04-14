package davide.customitems.Events;

import davide.customitems.API.Radius;
import davide.customitems.API.Vector;
import davide.customitems.CustomItems;
import davide.customitems.ItemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class SlimeBootsEvents implements Listener {

    @EventHandler
    private void onFall(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null) return;
        ItemMeta meta = player.getInventory().getBoots().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(Item.slimeBoots.getKey(), PersistentDataType.INTEGER)) return;

        if (player.getVelocity().getY() > -0.515) return;

        for (int i = 0; i < 5; i++) {
            Block block = player.getLocation().subtract(0, i, 0).getBlock();

            if (block.getType() == Material.SLIME_BLOCK) return;
            if (block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR && !block.isPassable()) {
                ArrayList<Block> blocks = Radius.getBlocksInRadius(block, new Vector(1, 0, 1));
                ArrayList<Material> states = new ArrayList<>();

                for (Block b : blocks)
                    states.add(b.getState().getType());

                for (int j = 0; j < blocks.size(); j++) {
                    if (blocks.get(j).getType() != Material.SLIME_BLOCK) {
                        blocks.get(j).setType(Material.SLIME_BLOCK);

                        int finalJ = j;
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> blocks.get(finalJ).setType(states.get(finalJ)), 2 * 20);
                    }
                }

                break;
            }
        }
    }
}
