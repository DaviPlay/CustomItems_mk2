package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.UUIDDataType;
import davide.customitems.CustomItems;
import davide.customitems.ItemCreation.Item;
import davide.customitems.Lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

        final Vector dir = player.getEyeLocation().getDirection();

        ArmorStand as = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0).add(dir), ArmorStand.class);
        as.setHelmet(ItemList.throwingAxe.getItemStack());
        as.setHeadPose(new EulerAngle(190, as.getHeadPose().getY(), as.getHeadPose().getZ()));
        as.setInvisible(true);
        as.setSmall(true);
        as.setMarker(true);
        as.setCanPickupItems(false);
        as.setGravity(false);

        Cooldowns.setCooldown(uuid, ItemList.throwingAxe.getKey(), ItemList.throwingAxe.getDelay());

        final int DISTANCE_MAX = 20;
        AtomicInteger i = new AtomicInteger();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CustomItems.getPlugin(CustomItems.class), () -> {
            if (as.isDead()) return;

            if (i.get() > DISTANCE_MAX)
                as.remove();

            as.teleport(as.getLocation().add(dir));
            as.setHeadPose(new EulerAngle(as.getHeadPose().getX(), as.getHeadPose().getY() + 2.5, as.getHeadPose().getZ()));

            if (!as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.AIR)
                for (Entity entity : as.getWorld().getNearbyEntities(as.getLocation(), 2, 2, 2))
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;

                        if (!(livingEntity instanceof Player) && !(livingEntity instanceof ArmorStand))
                            if (as.getLocation().distanceSquared(livingEntity.getLocation()) <= 1) {
                                livingEntity.damage(Item.getTemporaryDamage(is), player);
                                as.remove();
                                break;
                            }
                    }

            i.getAndIncrement();
        }, 0, 1);
    }
}
