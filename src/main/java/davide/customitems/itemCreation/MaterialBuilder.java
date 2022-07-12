package davide.customitems.itemCreation;

import davide.customitems.crafting.CraftingType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MaterialBuilder extends ItemBuilder {

    public MaterialBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);
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

    public MaterialBuilder rarity(Rarity rarity) {
        super.rarity(rarity);
        return this;
    }

    public MaterialBuilder hasRandomUUID(boolean hasRandomUUID) {
        super.hasRandomUUID(hasRandomUUID);
        return this;
    }

    /**
     * <b>DO NOT USE THE SHAPELESS CRAFTING TYPE WITH CUSTOM ITEMS</b>
     */
    public MaterialBuilder craftingType(CraftingType craftingType) {
        super.craftingType(craftingType);
        return this;
    }

    public MaterialBuilder exp(float exp) {
        super.exp(exp);
        return this;
    }

    public MaterialBuilder cookingTime(int cookingTime) {
        super.cookingTime(cookingTime);
        return this;
    }

    public Item build() {
        Item item = new Item(this);
        validateItem(item);
        return item;
    }

    private void validateItem(Item item) {
        if (item.getCrafting() != null && item.getCraftingType() == null)
            throw new IllegalArgumentException("The crafting recipe must have a type");

        if (item.getCrafting() == null && item.getCraftingType() != null)
            throw new IllegalArgumentException("The item must have a crafting recipe for it to have a specified type");

        if (item.getCraftingType() == CraftingType.FURNACE)
            if (item.getExp() <= 0 || item.getCookingTime() <= 0)
                throw new IllegalArgumentException("The furnace crafting type must have a specified exp gain and cooking time");

        if (item.isGlint() && item.getEnchantments() != null)
            throw new IllegalArgumentException("The item cannot have enchantments if it's been tagged as 'isGlint'");

        if (item.getAbilities() == null && item.getDelay() > 0)
            throw new IllegalArgumentException("The item must have at least 1 ability for it to have a delay");
    }
}
