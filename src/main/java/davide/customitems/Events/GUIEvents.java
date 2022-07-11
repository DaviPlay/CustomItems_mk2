package davide.customitems.Events;

import davide.customitems.GUIs.CraftingInventories;
import davide.customitems.GUIs.GUI;
import davide.customitems.Lists.ItemList;
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

        ItemStack currentItem = e.getCurrentItem();
        ItemMeta meta = currentItem.getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();
        Item item = Item.toItem(currentItem);

        if (item != null) {
            if (container != null)
                if (container.getKeys().contains(item.getKey()))
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        if (e.getClick().isLeftClick()) {
                            if (item.hasRandomUUID())
                                Item.setRandomUUID(currentItem);

                            player.getInventory().addItem(currentItem);
                        } else if (e.getClick().isRightClick())
                            player.openInventory(CraftingInventories.getInv(item.getKey()));
                    } else
                        player.openInventory(CraftingInventories.getInv(item.getKey()));
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
