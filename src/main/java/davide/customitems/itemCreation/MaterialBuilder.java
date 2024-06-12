package davide.customitems.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.lists.ItemList;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MaterialBuilder extends ItemBuilder {
    ItemStack compact;

    protected boolean addToList = true;

    public MaterialBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);
        CraftingType type = CraftingType.SHAPELESS;
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

    public MaterialBuilder(ItemStack itemStack, String name, boolean addToList) {
        super(itemStack, name);
        super.type(Type.MATERIAL);
        super.isGlint(true);
        CraftingType type = CraftingType.SHAPELESS;
        super.craftingType(type);
        this.addToList = addToList;

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

    public ItemBuilder compact(ItemStack compact) {
        this.compact = compact;
        return this;
    }

    @Override
    public Item build() {
        if (compact != null) {
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

        super.addToList = addToList;
        Item item = new Item(this);
        super.validateItem(item);

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
}
