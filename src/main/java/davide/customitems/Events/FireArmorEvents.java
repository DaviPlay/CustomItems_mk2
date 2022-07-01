package davide.customitems.Events;

import davide.customitems.API.ArmorEquipEvent;
import davide.customitems.API.ItemAbilities;
import davide.customitems.CustomItems;
import davide.customitems.ItemCreation.Item;
import davide.customitems.Lists.ItemList;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FireArmorEvents implements Listener {

    private final Item[] targetArmor = { ItemList.fireBoots, ItemList.fireLeggings, ItemList.fireChestplate, ItemList.fireHelmet };

    @EventHandler
    private void onEquip(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        ItemStack is = e.getNewArmorPiece();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item item : targetArmor)
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {

                if (!ItemAbilities.hasFullSet(armorContents, targetArmor, e.getType(), e.getNewArmorPiece()))
                    return;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!ItemAbilities.hasFullSet(player.getInventory().getArmorContents(), targetArmor))
                            this.cancel();

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        if (!entities.isEmpty())
                            for (Entity entity : entities)
                                if (entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;

                                    if (livingEntity.getFireTicks() <= 1)
                                        livingEntity.setFireTicks(20);
                                }
                    }
                }.runTaskTimer(CustomItems.getPlugin(CustomItems.class), 0, 1);
            }
    }
}
