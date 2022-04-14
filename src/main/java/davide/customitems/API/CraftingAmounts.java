package davide.customitems.API;

import davide.customitems.GUIs.GUI;
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
        if (Item.toItem(result) == null) return;

        switch (Item.toItem(result).getCraftingType()) {
            case SHAPED:
                if (onPrepareShaped(result, e.getInventory()))
                    e.getInventory().setResult(null);
                break;
            case SHAPELESS:
                if (onPrepareShapeless(result, e.getInventory()))
                    e.getInventory().setResult(null);
                break;
        }
    }

    @EventHandler
    private void onCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;
        if (e.getSlot() > 9) return;
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        if (Item.toItem(item) == null) return;
        if (e.getClick().isShiftClick()) return;

        switch (Item.toItem(item).getCraftingType()) {
            case SHAPED:
                onCraftShaped(item, e.getInventory());
                break;
            case SHAPELESS:
                onCraftShapeless(item, e.getInventory());
                break;
        }
    }

    private boolean onPrepareShaped(ItemStack itemStack, Inventory inv) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();

        for (int i = 0; i < 8; i++) {
            ItemStack cItem = crafting.get(i);

            if (cItem != null) {
                ItemStack is = inv.getItem(i + 1);

                assert is != null;
                if (is.getAmount() < cItem.getAmount())
                    return true;
            }
        }

        return false;
    }

    private boolean onPrepareShapeless(ItemStack itemStack, Inventory inv) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();
        int count = 0, j = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack is = inv.getItem(i + 1);

            if (is != null) {
                j++;
                if (is.getAmount() >= crafting.get(j).getAmount())
                    count++;
            }
        }

        return count - 1 != j;
    }

    private void onCraftShaped(ItemStack itemStack, Inventory inv) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();

        for (int i = 0; i < crafting.size(); i++) {
            ItemStack cItem = crafting.get(i);

            if (cItem != null) {
                ItemStack is = inv.getItem(i + 1);

                assert is != null;
                is.setAmount(is.getAmount() - (cItem.getAmount() - 1));
            }
        }
    }

    private void onCraftShapeless(ItemStack itemStack, Inventory inv) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();
        int j = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack is = inv.getItem(i + 1);

            if (is != null) {
                j++;
                is.setAmount(is.getAmount() - (crafting.get(j).getAmount() - 1));
            }
        }
    }
}
