package davide.customitems.ReforgeCreation;

import davide.customitems.ItemCreation.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ReforgeAssigning implements Listener {

    @EventHandler
    private void assignReforgeOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        while (true) {
            Reforge reforge = Reforge.randomReforge();

            if (reforge.getType() == item.getType() || reforge.getSubType() == item.getSubType() || reforge.getType() == item.getSubType().getType()) {
                Item.setReforge(reforge, is);
                break;
            }
        }
    }
}
