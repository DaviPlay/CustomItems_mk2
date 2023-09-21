package davide.customitems.itemCreation.builders;

import davide.customitems.crafting.CraftingType;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MaterialBuilder extends ItemBuilder {
    private final CraftingType type = CraftingType.SHAPELESS;

    private boolean addToList = true;

    public MaterialBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);
        super.craftingType(type);

        if (craftingType == CraftingType.FURNACE) {
            super.crafting(Collections.singletonList(
                    new ItemStack(itemStack.getType())
            ));
        } else {
            super.crafting(Arrays.asList(
                    new ItemStack(itemStack.getType(), Math.min(32, itemStack.getMaxStackSize())),
                    new ItemStack(itemStack.getType(), Math.min(32, itemStack.getMaxStackSize())),
                    new ItemStack(itemStack.getType(), Math.min(32, itemStack.getMaxStackSize())),
                    new ItemStack(itemStack.getType(), Math.min(32, itemStack.getMaxStackSize())),
                    new ItemStack(itemStack.getType(), Math.min(32, itemStack.getMaxStackSize()))
            ));
        }
    }

    public MaterialBuilder(ItemStack itemStack, ItemStack compact, String name) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);
        super.craftingType(type);

        compact.setAmount(16);
        if (craftingType == CraftingType.FURNACE) {
            super.crafting(Collections.singletonList(
                    compact
            ));
        } else {
            super.crafting(Arrays.asList(
                    compact,
                    compact,
                    compact,
                    compact,
                    compact,
                    compact,
                    compact,
                    compact
            ));
        }
    }

    public MaterialBuilder(ItemStack itemStack, String name, boolean addToList) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        this.addToList = addToList;
    }

    @Override
    public ItemBuilder rarity(Rarity rarity) {
        return super.rarity(rarity);
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
        Item item = new Item(this);
        validateItem(item);

        if (addToList) {
            if (ItemList.items.isEmpty())
                for (int i = 0; i < 2; i++)
                    ItemList.items.add(new ArrayList<>());
            else if (ItemList.items.size() == 1)
                ItemList.items.add(new ArrayList<>());

            ItemList.items.get(1).add(item);
        }
        return item;
    }

    @Override
    public void validateItem(Item item) {
        super.validateItem(item);
    }
}
