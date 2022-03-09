package davide.customitems.GUIs;

import davide.customitems.ItemCreation.Item;
import org.bukkit.Bukkit;
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

        for (Item item : Item.items)
            if (item.getCrafting() != null)
                createInventories(item);
    }

    public static void createInventories(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, itemResult.getName());
        int i = 0;
        int j = 0;

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe sr = (ShapedRecipe) recipe;
                String[] shape = sr.getShape();
                Map<Character, RecipeChoice> map = sr.getChoiceMap();

                for (String sItem : shape) {
                    char[] chars = sItem.toCharArray();
                    i++;

                    for (char ingredient : chars) {
                        RecipeChoice choice = map.get(ingredient);
                        ItemStack item = null;

                        if (choice != null) {
                            if (choice instanceof RecipeChoice.ExactChoice)
                                item = ((RecipeChoice.ExactChoice) choice).getItemStack();
                            else
                                item = ((RecipeChoice.MaterialChoice) choice).getItemStack();
                        }

                        if ((i - 1) >= 3) break;
                        inv.setItem(((i - 1) * 3) + (j + 1), item);

                        j++;
                        if (j == 3) j = 0;
                    }
                    if ((i - 1) >= 3) break;
                }
            }
        inv.setItem(0, result);
        invs.put(itemResult.key, inv);
    }

    public static Inventory getInv(NamespacedKey key) {
        return invs.get(key);
    }
}
