package davide.customitems.gui.itemCreation;

import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.builders.MaterialBuilder;
import davide.customitems.itemCreation.builders.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CraftingRecipeGUI implements IGUI {
    public static Inventory inv;
    private static final Item craftingGlass = new UtilsBuilder(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), "§aSelect Material", false).build();

    public CraftingRecipeGUI(List<ItemStack> recipe) {
        inv = Bukkit.createInventory(this, 54, "Crafting Recipe");
        setInv(recipe);
    }

    private void setInv(List<ItemStack> recipe) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        if (!recipe.isEmpty()) {
            int j = 0;
            for (int i = 10; i < 31; i++) {
                if (recipe.get(j) == null)
                    inv.setItem(i, craftingGlass.getItemStack());
                else
                    inv.setItem(i, recipe.get(j));

                j++;
                if (i == 12 || i == 21)
                    i += 6;
            }
        } else {
            for (int i = 10; i < 31; i++) {
                inv.setItem(i, craftingGlass.getItemStack());

                if (i == 12 || i == 21)
                    i += 6;
            }
        }

        switch (MaterialCreationGUI.craftingType) {
            case SHAPELESS -> inv.setItem(23, ItemList.shapelessCrafting.getItemStack());
            case SHAPED -> inv.setItem(23, ItemList.shapedCrafting.getItemStack());
            case FURNACE -> inv.setItem(23, ItemList.furnaceCrafting.getItemStack());
        }

        inv.setItem(25, new UtilsBuilder(MaterialCreationGUI.itemStack, MaterialCreationGUI.name, false)
                .rarity(MaterialCreationGUI.rarity)
                .isGlint(true)
                .build()
                .getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10, 11, 12, 19, 20, 21, 28, 29, 30 -> {
                if (!Item.isCustomItem(clickedItem))
                    MaterialCreationGUI.signReadCraftingMat(whoClicked, slot, true);
            }
            case 25 -> {
                MaterialCreationGUI.crafting = getRecipe(inventory);
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
        }
    }

    private List<ItemStack> getRecipe(Inventory inv) {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 10; i < 31; i++) {
            ItemStack item = inv.getItem(i);

            if (!Item.isCustomItem(item))
                items.add(item);

            if (i == 12 || i == 21)
                i += 6;
        }

        return items;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
