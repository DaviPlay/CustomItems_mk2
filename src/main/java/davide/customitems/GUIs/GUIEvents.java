package davide.customitems.GUIs;

import davide.customitems.ItemCreation.Item;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class GUIEvents implements Listener {

    @EventHandler
    private void openInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory topInv = player.getOpenInventory().getTopInventory();
        Inventory clickedInv = e.getClickedInventory();
        List<Boolean> bools = new ArrayList<>();

        if (clickedInv == null) return;
        if (e.getCurrentItem() == null) return;

        for (Item item : Item.items)
            if (topInv.equals(GUI.inv) || topInv.equals(CraftingInventories.getInv(item.key)))
                bools.add(topInv.equals(GUI.inv) || topInv.equals(CraftingInventories.getInv(item.key)));

        if (!bools.contains(true))
            return;

        ItemMeta meta = e.getCurrentItem().getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();

        for (Item item : Item.items) {
            if (topInv.equals(GUI.inv) || topInv.equals(CraftingInventories.getInv(item.key)))
                if (clickedInv.equals(GUI.inv) || clickedInv.equals(CraftingInventories.getInv(item.key)))
                    if (container != null)
                        if (container.getKeys().contains(item.key))
                            if (player.getGameMode() == GameMode.CREATIVE) {
                                if (e.getClick().isLeftClick()) {
                                    player.getInventory().addItem(e.getCurrentItem());
                                    break;
                                } else if (e.getClick().isRightClick()) {
                                    player.openInventory(CraftingInventories.getInv(item.key));
                                    break;
                                }
                            } else {
                                player.openInventory(CraftingInventories.getInv(item.key));
                                break;
                            }
        }
        e.setCancelled(true);
    }
}
