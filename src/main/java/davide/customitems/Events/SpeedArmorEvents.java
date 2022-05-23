package davide.customitems.Events;

import davide.customitems.API.ArmorEquipEvent;
import davide.customitems.ItemCreation.Item;
import davide.customitems.Lists.ItemList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpeedArmorEvents implements Listener {
    private final Item[] armor = {ItemList.speedHelmet, ItemList.speedChestplate, ItemList.speedLeggings, ItemList.speedBoots};

    @EventHandler
    private void onEquipArmor(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack is = e.getNewArmorPiece();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item item : armor)
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {
                float speed = player.getWalkSpeed();

                player.setWalkSpeed(speed + 0.1f);
            }
    }

    @EventHandler
    private void onUnequipArmor(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null || e.getOldArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack is = e.getOldArmorPiece();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item item : armor)
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {
                float speed = player.getWalkSpeed();

                player.setWalkSpeed(speed - 0.1f);
            }
    }
}
