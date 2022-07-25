package davide.customitems.itemCreation.builders;

import davide.customitems.itemCreation.Item;
import org.bukkit.inventory.ItemStack;

public class UtilsBuilder extends ItemBuilder {

    public UtilsBuilder(ItemStack itemStack, String name) {
        super(itemStack, name);
    }

    @Override
    public ItemBuilder lore(String... lore) {
        return super.lore(lore);
    }

    @Override
    public Item build() {
        Item item = new Item(this);
        validateItem(item);
        return item;
    }
}
