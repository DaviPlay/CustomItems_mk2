package davide.customitems.itemCreation;

import davide.customitems.lists.ItemList;
import org.bukkit.inventory.ItemStack;

public class UtilsBuilder extends ItemBuilder {
    private boolean addToList = true;

    public UtilsBuilder(ItemStack itemStack, String name, boolean addToList) {
        super(itemStack, name);
        this.addToList = addToList;
    }

    public UtilsBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
    }

    @Override
    public ItemBuilder lore(String... lore) {
        return super.lore(lore);
    }

    @Override
    public Item build() {
        super.addToList = addToList;
        Item item = new Item(this);
        if (addToList)
            ItemList.utilsItems.add(item);
        return item;
    }
}
