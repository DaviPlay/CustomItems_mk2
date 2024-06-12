package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Events {
    TELEPORT(new UtilsBuilder(new ItemStack(Material.ENDER_EYE), "TELEPORT", false).build(), 8),
    THROW(new UtilsBuilder(new ItemStack(Material.ARMOR_STAND), "THROW", false).build(), 50);

    private final Item item;
    private float param1;
    Events(Item item, float param1) {
        this.item = item;
        this.param1 = param1;
    }

    public float getParam1() {
        return param1;
    }
    public void setParam1(float float1) {
        this.param1 = float1;
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return item.getItemStack(1);
    }
}
