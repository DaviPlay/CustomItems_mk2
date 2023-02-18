package davide.customitems.gui.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.builders.MaterialBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FurnaceRecipeGUI implements IGUI {
    public static Inventory inv;

    public FurnaceRecipeGUI(List<ItemStack> recipe) {
        inv = Bukkit.createInventory(this, 54, "Furnace Recipe");
        setInv(recipe);
    }

    private void setInv(List<ItemStack> recipe) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        if (recipe.isEmpty())
            inv.setItem(11, ItemList.craftingGlass.getItemStack());
        else
            inv.setItem(11, recipe.get(0));

        inv.setItem(29, new ItemStack(Material.COAL));
        inv.setItem(22,ItemList.furnaceCrafting.getItemStack());

        inv.setItem(24, new MaterialBuilder(MaterialCreationGUI.itemStack, MaterialCreationGUI.name, false)
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
            case 11 -> {
                if (clickType.isLeftClick())
                    GUIEvents.signReadCraftingMat(whoClicked, slot, true, inv);
                else if (clickType.isRightClick())
                    GUIEvents.signReadAmount(whoClicked, clickedItem, slot, inv);
            }
            case 24, 45 -> {
                MaterialCreationGUI.furnaceRecipe = Collections.singletonList(inv.getItem(11));
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
