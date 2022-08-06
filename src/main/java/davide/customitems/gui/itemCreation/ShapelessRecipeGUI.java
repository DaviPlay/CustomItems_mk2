package davide.customitems.gui.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.builders.MaterialBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipeGUI implements IGUI {
    public static Inventory inv;

    public ShapelessRecipeGUI(List<ItemStack> recipe) {
        inv = Bukkit.createInventory(this, 54, "Crafting Recipe");
        setInv(recipe);
    }

    private void setInv(List<ItemStack> recipe) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        if (recipe.isEmpty()) {
            for (int i = 10; i < 21; i++) {
                inv.setItem(i, ItemList.craftingGlass.getItemStack());

                if (i == 16)
                    i += 2;
            }
        }
        else {
            int j = 0;
            for (int i = 10; i < 21; i++) {
                if (recipe.get(j) != null)
                    inv.setItem(i, recipe.get(j));
                else
                    inv.setItem(i, ItemList.craftingGlass.getItemStack());

                j++;

                if (i == 16)
                    i += 2;
            }
        }

        inv.setItem(31, new MaterialBuilder(MaterialCreationGUI.itemStack, MaterialCreationGUI.name, false)
                .rarity(MaterialCreationGUI.rarity)
                .craftingType(CraftingType.NONE)
                .build()
                .getItemStack());

        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10, 11, 12, 13, 14, 15, 16, 19, 20 -> {
                if (clickType.isLeftClick())
                    GUIEvents.signReadCraftingMat(whoClicked, slot, true, inv);
                else if (clickType.isRightClick())
                    GUIEvents.signReadAmount(whoClicked, clickedItem, slot, inv);
            }
            case 31, 45 -> {
                MaterialCreationGUI.shapelessRecipe = getRecipe(inventory);
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    private List<ItemStack> getRecipe(Inventory inv) {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 10; i < 21; i++) {
            ItemStack item = inv.getItem(i);

            if (!Item.isCustomItem(item))
                items.add(item);
            else
                items.add(null);

            if (i == 16)
                i += 2;
        }

        return items;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
