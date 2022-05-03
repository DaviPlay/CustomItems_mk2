package davide.customitems.Events;

import davide.customitems.Lists.ItemList;
import davide.customitems.API.SpecialBlocks;
import davide.customitems.CustomItems;
import davide.customitems.ItemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MidasStaffEvents implements Listener {

    @EventHandler
    private void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        LivingEntity hit = (LivingEntity) e.getEntity();
        Player player = (Player) e.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.midasStaff.getKey(), PersistentDataType.INTEGER)) return;

        hit.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, hit.getLocation(), 20, 0, 0, 0, 0.25);
        Block b = hit.getLocation().getBlock();
        b.setType(Material.GOLD_BLOCK);
        hit.remove();
    }

    @EventHandler
    private void enchant(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType()))
                return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.midasStaff.getKey(), PersistentDataType.INTEGER)) return;

        if (!player.isSneaking()) return;
        Item.toItem(item).setGlint(!meta.hasEnchants(), item);
    }

    @EventHandler
    private void onWalk(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        int first = player.getInventory().first(ItemList.midasStaff.getItemStack().getType());
        ItemStack item = first == -1 ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(first);

        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.midasStaff.getKey(), PersistentDataType.INTEGER)) return;

        if (!Item.toItem(item).isGlint()) return;
        Block b = player.getLocation().subtract(0, 1, 0).getBlock();
        Material type = b.getState().getType();

        if (!b.isPassable())
            if (b.getType() != Material.GOLD_BLOCK) {
                b.setType(Material.GOLD_BLOCK);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> b.setType(type), 2 * 20);
            }
    }
}
