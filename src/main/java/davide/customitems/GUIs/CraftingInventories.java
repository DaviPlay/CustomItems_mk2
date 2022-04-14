package davide.customitems.GUIs;

import davide.customitems.ItemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingInventories {
    private static HashMap<NamespacedKey, Inventory> invs;

    public static void setInvs() {
        invs = new HashMap<>();

        for (Item[] items : Item.items)
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
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());
        int i = 0;
        int j;

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, Item.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe sr = (ShapedRecipe) recipe;
                String[] shape = sr.getShape();
                Map<Character, RecipeChoice> map = sr.getChoiceMap();

                for (String sItem : shape) {
                    char[] chars = sItem.toCharArray();
                    i++;
                    j = 0;

                    for (char ingredient : chars) {
                        j++;
                        RecipeChoice choice = map.get(ingredient);
                        ItemStack item = null;

                        if (choice != null)
                            if (choice instanceof RecipeChoice.ExactChoice)
                                item = ((RecipeChoice.ExactChoice) choice).getItemStack();
                            else
                                item = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                        if (i > 3) break;

                        inv.setItem((i * 9) + j, item);
                    }
                }
            }

        inv.setItem(23, new Item(new ItemStack(Material.CRAFTING_TABLE), null, null, null, false, true, 0, true, null, null,
                "§aShaped Recipe", "§fThis recipe needs to be replicated", "§fin this exact order").getItemStack());
        inv.setItem(25, result);
        inv.setItem(49, Item.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private static void createShapelessInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item it = Item.toItem(result);
        List<ItemStack> crafting = it.getCrafting();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, Item.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe sr = (ShapelessRecipe) recipe;
                List<RecipeChoice> choices = sr.getChoiceList();
                ItemStack item = null;
                int i = -1;

                for (RecipeChoice choice : choices) {
                    i++;
                    if (choice != null)
                        if (choice instanceof RecipeChoice.ExactChoice)
                            item = ((RecipeChoice.ExactChoice) choice).getItemStack();
                        else
                            item = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                    if (i > crafting.size()) break;

                    if (i < 3)
                        inv.setItem(10 + i, item);
                    else if (i >= 3 && i < 6)
                        inv.setItem(19 + (i - 3), item);
                    else if (i >= 6 && i < 9)
                        inv.setItem(28 + (i - 6), item);
                }
                break;
            }

        inv.setItem(23, new Item(new ItemStack(Material.CRAFTING_TABLE), null, null, null, false, true, 0, true, null, null,
                "§aShapeless Recipe", "§fThis recipe can be done in any order").getItemStack());
        inv.setItem(25, result);
        inv.setItem(49, Item.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private static void createFurnaceInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            inv.setItem(x, Item.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof FurnaceRecipe) {
                FurnaceRecipe fr = (FurnaceRecipe) recipe;
                RecipeChoice choice = fr.getInputChoice();
                ItemStack item;

                if (choice instanceof RecipeChoice.ExactChoice)
                    item = ((RecipeChoice.ExactChoice) choice).getItemStack();
                else
                    item = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                inv.setItem(11, item);
            }

        inv.setItem(29, new ItemStack(Material.COAL));
        inv.setItem(22, new Item(new ItemStack(Material.FURNACE), null, null, null, false, true, 0, true, null, null,
                "§aFurnace Recipe").getItemStack());
        inv.setItem(24, result);
        inv.setItem(49, Item.itemArrow.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    public static Inventory getInv(NamespacedKey key) {
        return invs.get(key);
    }
}
