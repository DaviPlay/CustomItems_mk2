package davide.customitems.API;

import davide.customitems.ItemCreation.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Cauldron;
import org.bukkit.material.Colorable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ListenersPrevents implements Listener {

    @EventHandler
    private void disableShiftOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item[] items : ItemList.items)
            for (Item item : items)
                if (container.has(item.getKey(), PersistentDataType.INTEGER))
                    if (e.getClick().isShiftClick())
                        e.setCancelled(true);
    }

    @EventHandler
    private void onDyeReset(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack is = e.getItem();
        if (is == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        if (!(meta instanceof LeatherArmorMeta)) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item item : ItemList.items[0])
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {
                Block b = e.getClickedBlock();
                if (b == null) return;

                if (b.getType() == Material.WATER_CAULDRON)
                    e.setCancelled(true);
            }
    }

    @EventHandler
    private void onDyeCraft(PrepareItemCraftEvent e) {
        ItemStack is = e.getInventory().getResult();
        if (is == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        if (!(meta instanceof LeatherArmorMeta)) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item item : ItemList.items[0])
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {
                Inventory inv = e.getInventory();

                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = inv.getItem(i + 1);

                    if (itemStack != null) {
                        if (SpecialBlocks.isDye(itemStack.getType()))
                            e.getInventory().setResult(null);
                    }
                }
            }
    }
}
