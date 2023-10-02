package davide.customitems.gui.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.builders.ItemBuilder;
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

public class ShapedRecipeGUI implements IGUI {
    public static Inventory inv;

    public ShapedRecipeGUI(List<ItemStack> recipe, IGUI type) {
        inv = Bukkit.createInventory(this, 54, "Crafting Recipe");
        setInv(recipe, type);
    }

    private void setInv(List<ItemStack> recipe, IGUI type) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        if (!recipe.isEmpty()) {
            int j = 0;
            for (int i = 10; i < 31; i++) {
                if (recipe.get(j) == null)
                    inv.setItem(i, ItemList.craftingGlass.getItemStack());
                else
                    inv.setItem(i, recipe.get(j));

                j++;
                if (i == 12 || i == 21)
                    i += 6;
            }
        } else {
            for (int i = 10; i < 31; i++) {
                inv.setItem(i, ItemList.craftingGlass.getItemStack());

                if (i == 12 || i == 21)
                    i += 6;
            }
        }

        inv.setItem(23, ItemList.shapedCrafting.getItemStack());
        if (type instanceof MaterialCreationGUI)
            inv.setItem(24, new MaterialBuilder(MaterialCreationGUI.itemStack, MaterialCreationGUI.name, false)
                    .rarity(MaterialCreationGUI.rarity)
                    .build()
                    .getItemStack());
        else
            inv.setItem(24, new ItemBuilder(ItemCreationGUI.itemStack, ItemCreationGUI.name, false)
                    .subType(ItemCreationGUI.subType)
                    .rarity(ItemCreationGUI.rarity)
                    .damage(ItemCreationGUI.damage)
                    .critChance(ItemCreationGUI.critChance)
                    .critDamage(ItemCreationGUI.critDamage)
                    .health(ItemCreationGUI.health)
                    .defence(ItemCreationGUI.defence)
                    .abilities(ItemCreationGUI.abilities.toArray(new Ability[]{}))
                    .enchantments(ItemCreationGUI.enchantments)
                    .lore(ItemCreationGUI.lore.toArray(new String[]{}))
                    .craftingType(CraftingType.NONE)
                    .build()
                    .getItemStack());

        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10, 11, 12, 19, 20, 21, 28, 29, 30 -> {
                if (clickType.isLeftClick())
                    GUIUtils.signReadCraftingMat(whoClicked, slot, true, inv);
                else if (clickType.isRightClick())
                    GUIUtils.signReadAmount(whoClicked, clickedItem, slot, inv);
            }
            case 25, 45 -> {
                MaterialCreationGUI.recipe = getRecipe(inventory);
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    private List<ItemStack> getRecipe(Inventory inv) {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 10; i < 31; i++) {
            ItemStack item = inv.getItem(i);

            assert item != null;
            if (!Item.toItem(item).equals(ItemList.craftingGlass))
                items.add(item);
            else
                items.add(null);

            if (i == 12 || i == 21)
                i += 6;
        }

        return items;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
