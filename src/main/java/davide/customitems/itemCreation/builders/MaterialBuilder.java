package davide.customitems.itemCreation.builders;

import davide.customitems.crafting.CraftingType;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.itemCreation.Type;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MaterialBuilder extends ItemBuilder {

    public MaterialBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);

        if (craftingType == CraftingType.FURNACE) {
            super.crafting(Collections.singletonList(
                    new ItemStack(itemStack.getType())
            ));
        } else {
            super.crafting(Arrays.asList(
                    new ItemStack(itemStack.getType(), 32),
                    new ItemStack(itemStack.getType(), 32),
                    new ItemStack(itemStack.getType(), 32),
                    new ItemStack(itemStack.getType(), 32),
                    new ItemStack(itemStack.getType(), 32),
                    null,
                    null,
                    null,
                    null
            ));
        }
    }

    @Override
    public ItemBuilder rarity(Rarity rarity) {
        return super.rarity(rarity);
    }

    @Override
    public ItemBuilder hasRandomUUID(boolean hasRandomUUID) {
        return super.hasRandomUUID(hasRandomUUID);
    }

    @Override
    public ItemBuilder craftingType(CraftingType craftingType) {
        return super.craftingType(craftingType);
    }

    @Override
    public ItemBuilder crafting(List<ItemStack> crafting) {
        return super.crafting(crafting);
    }

    @Override
    public ItemBuilder cookingTime(int cookingTime) {
        return super.cookingTime(cookingTime);
    }

    @Override
    public ItemBuilder exp(float exp) {
        return super.exp(exp);
    }

    @Override
    public Item build() {
        return super.build();
    }

    @Override
    public void validateItem(Item item) {
        super.validateItem(item);
    }
}