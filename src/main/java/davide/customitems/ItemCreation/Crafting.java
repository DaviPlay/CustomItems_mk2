package davide.customitems.ItemCreation;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class Crafting {
    private final NamespacedKey key;
    private final ItemStack item;
    private final List<ItemStack> choice;

    private final List<RecipeChoice> recipeChoices = new ArrayList<>();

    public Crafting(NamespacedKey key, ItemStack item, List<ItemStack> choice) {
        this.key = key;
        this.item = item;
        this.choice = choice;

        create();
    }

    private void create() {
        ShapedRecipe sr = new ShapedRecipe(key, item);

        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        StringBuilder s3 = new StringBuilder();
        char first1;

        for (int i = 0; i < choice.size(); i++) {
            if (i < 3) {
                if (choice.get(i) == null)
                    s1.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    s1.append(first1);
                }
            } else if (i >= 3 && i < 6) {
                if (choice.get(i) == null)
                    s2.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    s2.append(first1);
                }
            } else if (i >= 6 && i < 9) {
                if (choice.get(i) == null)
                    s3.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    s3.append(first1);
                }
            }
        }

        sr.shape(String.valueOf(s1), String.valueOf(s2), String.valueOf(s3));

        for (ItemStack itemStack : choice)
            if (itemStack != null)
                recipeChoices.add(new RecipeChoice.ExactChoice(itemStack));

        char first2, buffer = 0;
        for (RecipeChoice rc : recipeChoices) {
            first2 = rc.getItemStack().getType().name().charAt(0);

            if (first2 != buffer) {
                sr.setIngredient(first2, rc);
                buffer = first2;
            }
        }

        Bukkit.addRecipe(sr);
    }
}
