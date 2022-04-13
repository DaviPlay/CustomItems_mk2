package davide.customitems.ItemCreation;

import davide.customitems.API.CraftingType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Crafting {
    private final NamespacedKey key;
    private final ItemStack item;
    private final float exp;
    private final int cookingTime;
    private final List<ItemStack> choice;

    private final List<RecipeChoice> recipeChoices = new ArrayList<>();

    public Crafting(NamespacedKey key, ItemStack item, CraftingType.Crafting crafting, float exp, int cookingTime, List<ItemStack> choice) {
        this.key = key;
        this.item = item;
        this.exp = exp;
        this.cookingTime = cookingTime;
        this.choice = choice;

        switch (crafting) {
            case SHAPED:
                createShaped();
                break;
            case SHAPELESS:
                createShapeless();
                break;
            case FURNACE:
                createFurnace();
                break;
        }
    }

    private void createShaped() {
        ShapedRecipe sr = new ShapedRecipe(key, item);

        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        StringBuilder s3 = new StringBuilder();
        char first1, buffer1 = 0;
        String buffer = null;

        for (int i = 0; i < choice.size(); i++) {
            if (i < 3) {
                if (choice.get(i) == null)
                    s1.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer)) {
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    }
                    buffer1 = first1;
                    buffer = choice.get(i).getType().name();
                    s1.append(first1);
                }
            } else if (i >= 3 && i < 6) {
                if (choice.get(i) == null)
                    s2.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer)) {
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    }
                    buffer = choice.get(i).getType().name();
                    buffer1 = first1;
                    s2.append(first1);
                }
            } else if (i >= 6 && i < 9) {
                if (choice.get(i) == null)
                    s3.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer)) {
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    }
                    buffer = choice.get(i).getType().name();
                    buffer1 = first1;
                    s3.append(first1);
                }
            }
        }

        System.out.println(String.valueOf(s1) + " " + String.valueOf(s2) + " " + String.valueOf(s3));
        sr.shape(String.valueOf(s1), String.valueOf(s2), String.valueOf(s3));

        for (ItemStack itemStack : choice)
            if (itemStack != null)
                recipeChoices.add(new RecipeChoice.ExactChoice(itemStack));

        char first2, buffer2 = 0, buffer3 = 0;
        String buffer4 = null;

        for (RecipeChoice rc : recipeChoices) {
            first2 = rc.getItemStack().getType().name().charAt(0);
            if (first2 == buffer3 && !rc.getItemStack().getType().name().equals(buffer4)) {
                first2 = String.valueOf(first2).toLowerCase(Locale.ROOT).charAt(0);
            }
            buffer4 = rc.getItemStack().getType().name();
            buffer3 = first2;

            if (first2 != buffer2) {
                sr.setIngredient(first2, rc);
                buffer2 = first2;
            }
        }

        Bukkit.addRecipe(sr);
    }

    private void createShapeless() {
        ShapelessRecipe sr = new ShapelessRecipe(key, item);
        int i = 0;

        for (ItemStack itemStack : choice)
            if (itemStack != null)
                recipeChoices.add(new RecipeChoice.ExactChoice(itemStack));

        for (RecipeChoice rc : recipeChoices) {
            if (i < 9)
                sr.addIngredient(rc);

            i++;
        }

        Bukkit.addRecipe(sr);
    }

    private void createFurnace() {
        FurnaceRecipe fr = null;

        for (ItemStack itemStack : choice)
            if (itemStack != null)
                recipeChoices.add(new RecipeChoice.ExactChoice(itemStack));

        for (RecipeChoice rc : recipeChoices)
            fr = new FurnaceRecipe(key, item, rc, exp, cookingTime);

        if (fr != null)
            Bukkit.addRecipe(fr);
    }
}
