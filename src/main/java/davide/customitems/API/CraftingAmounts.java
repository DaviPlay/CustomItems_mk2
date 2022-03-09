package davide.customitems.API;

import davide.customitems.ItemCreation.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftingAmounts implements Listener {

    @EventHandler
    private void onPrepareCraft(PrepareItemCraftEvent e) {
        ItemStack result = e.getInventory().getResult();
        if (result == null) return;

        for (Item item : Item.items)
            if (result.equals(item.getItemStack())) {
                List<ItemStack> crafting = Item.toItem(result).getCrafting();

                for (int i = 0; i < crafting.size(); i++) {
                    if (crafting.get(i) != null) {
                        Inventory inv = e.getInventory();
                        ItemStack invItem = inv.getItem(i + 1);
                        int amount = crafting.get(i).getAmount();

                        assert invItem != null;
                        if (!(invItem.getAmount() >= amount))
                            e.getInventory().setResult(null);
                    }
                }
            }
    }

    @EventHandler
    private void onCraft(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().getType().equals(InventoryType.WORKBENCH)) return;
        if (!e.getSlotType().equals(InventoryType.SlotType.RESULT)) return;

        ItemStack current = e.getCurrentItem();
        if (current == null) return;

        for (Item item : Item.items)
            if (current.equals(item.getItemStack())) {
                Inventory inv = e.getInventory();
                List<ItemStack> crafting = Item.toItem(current).getCrafting();

                for (int i = 0; i <= crafting.size() - 1; i++) {
                    if (crafting.get(i) != null) {
                        ItemStack invItem = inv.getItem(i + 1);
                        int amount = crafting.get(i).getAmount();

                        assert invItem != null;
                        if (e.isShiftClick())
                            e.setCancelled(true);
                        else
                            invItem.setAmount((invItem.getAmount() - amount) + 1);
                    }
                }
            }
    }
}
