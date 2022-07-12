package davide.customitems.gui;

import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.*;

public class CraftingInventories {
    private static HashMap<NamespacedKey, Inventory> invs;

    public static void setInvs() {
        invs = new HashMap<>();

        for (Item[] items : ItemList.items)
            for (Item item : items)
                if (item.getCrafting() != null)
                    switch (item.getCraftingType()) {
                        case SHAPED:
                            createShapedInvs(item);
                            break;
                        case SHAPELESS:
                            createShapelessInvs(item);
                            break;
                        case FURNACE:
                            createFurnaceInvs(item);
                            break;
                    }
        }

    private static void createShapedInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());
        int i = 0;
        int j;

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe sr = (ShapedRecipe) recipe;
                String[] shape = sr.getShape();
                Map<Character, RecipeChoice> map = sr.getChoiceMap();

                if (item != null && sr.getKey().equals(item.getKey()))
                    for (String sItem : shape) {
                        char[] chars = sItem.toCharArray();
                        i++;
                        j = 0;

                        for (char ingredient : chars) {
                            j++;
                            RecipeChoice choice = map.get(ingredient);
                            ItemStack is = null;

                            if (choice != null)
                                if (choice instanceof RecipeChoice.ExactChoice)
                                    is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                                else
                                    is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                            if (i > 3) break;

                            inv.setItem((i * 9) + j, is);
                        }
                    }
            }

        inv.setItem(23, ItemList.shapedCrafting.getItemStack());
        inv.setItem(25, result);
        inv.setItem(49, ItemList.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private static void createShapelessInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        if (item == null) return;
        List<ItemStack> crafting = item.getCrafting();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe sr = (ShapelessRecipe) recipe;
                List<RecipeChoice> choices = sr.getChoiceList();
                ItemStack is = null;
                int i = -1;

                if (sr.getKey().equals(item.getKey()))
                    for (RecipeChoice choice : choices) {
                        i++;
                        if (choice != null)
                            if (choice instanceof RecipeChoice.ExactChoice)
                                is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                            else
                                is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                        if (i > crafting.size()) break;

                        if (i < 3)
                            inv.setItem(10 + i, is);
                        else if (i >= 3 && i < 6)
                            inv.setItem(19 + (i - 3), is);
                        else if (i >= 6 && i < 9)
                            inv.setItem(28 + (i - 6), is);
                    }
                break;
            }

        inv.setItem(23, ItemList.shapelessCrafting.getItemStack());
        inv.setItem(25, result);
        inv.setItem(49, ItemList.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private static void createFurnaceInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof FurnaceRecipe) {
                FurnaceRecipe fr = (FurnaceRecipe) recipe;
                RecipeChoice choice = fr.getInputChoice();
                ItemStack is = null;

                if (item != null && fr.getKey().equals(item.getKey()))
                    if (choice instanceof RecipeChoice.ExactChoice)
                        is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                    else
                        is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                inv.setItem(11, is);
            }

        inv.setItem(29, new ItemStack(Material.COAL));
        inv.setItem(22,ItemList.furnaceCrafting.getItemStack());
        inv.setItem(24, result);
        inv.setItem(49, ItemList.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    public static Inventory getInv(NamespacedKey key) {
        return invs.get(key);
    }
}
