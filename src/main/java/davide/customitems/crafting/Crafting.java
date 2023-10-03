package davide.customitems.crafting;

import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Crafting {
    private final NamespacedKey key;
    private final ItemStack item;
    private final float exp;
    private final int cookingTime;
    private final List<ItemStack> choice;

    public Crafting(NamespacedKey key, ItemStack item, CraftingType crafting, float exp, int cookingTime, List<ItemStack> choice) {
        this.key = key;
        this.item = item;
        this.exp = exp;
        this.cookingTime = cookingTime;
        this.choice = choice;

        switch (crafting) {
            case SHAPED -> createShaped();
            case SHAPELESS -> createShapeless();
            case FURNACE -> createFurnace();
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
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer))
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    else if (choice.get(i).getType().name().equals(buffer))
                        first1 = buffer1;

                    buffer1 = first1;
                    buffer = choice.get(i).getType().name();

                    s1.append(first1);
                }
            } else if (i < 6) {
                if (choice.get(i) == null)
                    s2.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer))
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    else if (choice.get(i).getType().name().equals(buffer))
                        first1 = buffer1;

                    buffer = choice.get(i).getType().name();
                    buffer1 = first1;

                    s2.append(first1);
                }
            } else if (i < 9) {
                if (choice.get(i) == null)
                    s3.append(" ");
                else {
                    first1 = choice.get(i).getType().name().charAt(0);
                    if (first1 == buffer1 && !choice.get(i).getType().name().equals(buffer))
                        first1 = String.valueOf(first1).toLowerCase(Locale.ROOT).charAt(0);
                    else if (choice.get(i).getType().name().equals(buffer))
                        first1 = buffer1;

                    buffer = choice.get(i).getType().name();
                    buffer1 = first1;

                    s3.append(first1);
                }
            }
        }
        sr.shape(String.valueOf(s1), String.valueOf(s2), String.valueOf(s3));

        final List<RecipeChoice> recipeChoices = new ArrayList<>();
        for (ItemStack itemStack : choice)
            if (itemStack != null)
                recipeChoices.add(Item.isCustomItem(itemStack) ? new RecipeChoice.MaterialChoice(itemStack.getType()) : new RecipeChoice.ExactChoice(itemStack));

        char first2, buffer2 = 0, buffer3 = 0;
        String buffer4 = null;

        for (RecipeChoice rc : recipeChoices) {
            first2 = rc.getItemStack().getType().name().charAt(0);
            if (first2 == buffer3 && !rc.getItemStack().getType().name().equals(buffer4))
                first2 = String.valueOf(first2).toLowerCase(Locale.ROOT).charAt(0);
            else if (rc.getItemStack().getType().name().equals(buffer4))
                first2 = buffer3;

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
        List<RecipeChoice.ExactChoice> rcs = new ArrayList<>();

        for (ItemStack itemStack : choice)
            rcs.add(new RecipeChoice.ExactChoice(itemStack));

        try {
            Field f = sr.getClass().getDeclaredField("ingredients");
            f.setAccessible(true);
            f.set(sr, rcs);
            Bukkit.addRecipe(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFurnace() {
        FurnaceRecipe fr;
        RecipeChoice rc = null;

        for (ItemStack itemStack : choice)
            if (itemStack != null)
                rc = new RecipeChoice.ExactChoice(itemStack);

        assert rc != null;
        fr = new FurnaceRecipe(key, item, rc, exp, cookingTime);

        Bukkit.addRecipe(fr);
    }
}
