package davide.customitems.Events;

import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SoulBowEvents implements Listener {
    String uuid;

    @EventHandler
    private void onShoot(EntityDamageByEntityEvent e) {
        LivingEntity shot = (LivingEntity) e.getEntity();
        if (!(e.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getDamager();
        if (!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(Item.soulBow.getKey(), PersistentDataType.INTEGER)) return;

        if (player.getHealth() > 3)
            player.setHealth(player.getHealth() - 3);
        else
            player.sendMessage("§cYou don't have enough health to do that!");

        Wolf wolf = (Wolf) shot.getWorld().spawnEntity(shot.getLocation(), EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(player);
        wolf.setAdult();

        uuid = player.getUniqueId().toString();
        wolf.addScoreboardTag("wolf");
        player.addScoreboardTag(uuid);
    }

    @EventHandler
    private void onDie(EntityDamageByEntityEvent e) {
        LivingEntity shot = (LivingEntity) e.getEntity();
        if (!(e.getDamager() instanceof Wolf)) return;
        Wolf wolf = (Wolf) e.getDamager();
        Player player = (Player) wolf.getOwner();
        if (player == null) return;
        List<Wolf> wolfs = new ArrayList<>();

        if (e.getDamage() > shot.getHealth()) {
            for (Entity w : player.getWorld().getEntities()) {
                if (w instanceof Wolf) {
                    wolfs.add((Wolf) w);
                }
            }

            for (Wolf w : wolfs)
                if (w.getScoreboardTags().contains("wolf")) {
                    Player owner = (Player) w.getOwner();
                    assert owner != null;
                    if (owner.getScoreboardTags().contains(uuid))
                        w.remove();
                }
        }
    }
}
