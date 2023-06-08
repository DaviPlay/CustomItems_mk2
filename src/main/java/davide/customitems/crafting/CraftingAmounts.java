package davide.customitems.crafting;

import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.List;

public class CraftingAmounts implements Listener {

    @EventHandler
    private void onFurnaceStartSmelt(FurnaceStartSmeltEvent e) {
        ItemStack result = e.getRecipe().getResult();
        if (!Item.isCustomItem(result)) return;

        if (e.getSource().getAmount() < e.getRecipe().getInput().getAmount())
            e.setTotalCookTime(0);
    }

    @EventHandler
    private void onFurnaceSmelt(FurnaceSmeltEvent e) {
        ItemStack result = e.getResult();
        if (!Item.isCustomItem(result)) return;
        FurnaceRecipe recipe = (FurnaceRecipe) Bukkit.getRecipe(Item.toItem(result).getKey());
        if (recipe == null) return;

        e.getSource().setAmount(e.getSource().getAmount() - (recipe.getInput().getAmount() - 1));
    }

    @EventHandler
    private void onPrepareCraft(PrepareItemCraftEvent e) {
        ItemStack result = e.getInventory().getResult();
        Recipe recipe = e.getRecipe();
        if (result == null) return;
        if (Item.toItem(result) == null) return;

        switch (Item.toItem(result).getCraftingType()) {
            case SHAPED -> {
                ShapedRecipe sr = (ShapedRecipe) recipe;
                if (onPrepareShaped(result, e.getInventory(), sr.getKey()))
                    e.getInventory().setResult(null);
            }
            case SHAPELESS -> {
                ShapelessRecipe sr1 = (ShapelessRecipe) recipe;
                if (onPrepareShapeless(result, e.getInventory(), sr1.getKey()))
                    e.getInventory().setResult(null);
            }
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
            case SHAPED -> onCraftShaped(item, e.getInventory());
            case SHAPELESS -> onCraftShapeless(item, e.getInventory());
        }
    }

    private boolean onPrepareShaped(ItemStack itemStack, Inventory inv, NamespacedKey srKey) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();

        if (srKey.equals(item.getKey()))
            for (int i = 0; i < 9; i++) {
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

    private boolean onPrepareShapeless(ItemStack itemStack, Inventory inv, NamespacedKey srKey) {
        Item item = Item.toItem(itemStack);
        List<ItemStack> crafting = item.getCrafting();
        int count = 0, j = -1;

        if (srKey.equals(item.getKey()))
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
