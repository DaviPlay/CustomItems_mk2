package davide.customitems.GUIs;

import davide.customitems.API.ItemList;
import davide.customitems.ItemCreation.Item;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

        for (Item[] items : ItemList.items)
            for (Item item : items)
                if (topInv.equals(GUI.itemInv) || topInv.equals(GUI.materialInv) || topInv.equals(CraftingInventories.getInv(item.getKey())))
                    bools.add(topInv.equals(GUI.itemInv) || topInv.equals(GUI.materialInv) || topInv.equals(CraftingInventories.getInv(item.getKey())));

        if (!bools.contains(true)) return;

        if (clickedInv == null) return;
        if (e.getCurrentItem() == null) return;

        ItemMeta meta = e.getCurrentItem().getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();

        for (Item[] items : ItemList.items)
            for (Item item : items)
                if (container != null)
                    if (container.getKeys().contains(item.getKey()))
                        if (player.getGameMode() == GameMode.CREATIVE) {
                            if (e.getClick().isLeftClick()) {
                                player.getInventory().addItem(e.getCurrentItem());
                                break;
                            } else if (e.getClick().isRightClick()) {
                                player.openInventory(CraftingInventories.getInv(item.getKey()));
                                break;
                            }
                        } else {
                            player.openInventory(CraftingInventories.getInv(item.getKey()));
                            break;
                        }

        e.setCancelled(true);
    }

    @EventHandler
    private void menuArrow(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        ItemStack itemArrow = ItemList.itemArrow.getItemStack();
        ItemStack matsArrow = ItemList.matsArrow.getItemStack();
        Player player = (Player) e.getWhoClicked();

        if (item.equals(itemArrow))
            player.openInventory(GUI.itemInv);
        else if (item.equals(matsArrow))
            player.openInventory(GUI.materialInv);
    }
}
