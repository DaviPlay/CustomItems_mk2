package davide.customitems.lists;

import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public enum EventList {
    // <int> teleport distance
    TELEPORT(new UtilsBuilder(new ItemStack(Material.ENDER_EYE), "TELEPORT", false).build(), 8),
    // <int> throw distance limit, [<Instruction> code to run on hit]
    THROW(new UtilsBuilder(new ItemStack(Material.ARMOR_STAND), "THROW", false).build(), 50),
    // <PotionEffectType> potion effect to apply, <int> duration (in ticks), [<int> amplifier]
    GIVE_POTION_EFFECT(new UtilsBuilder(new ItemStack(Material.POTION), "GIVE_POTION_EFFECT", false).build(), PotionEffectType.POISON, 10 * 20, 0),
    NONE(null);

    private final Item item;
    private Object[] param;

    @SafeVarargs
    <T> EventList(Item item, T... param) {
        this.item = item;
        this.param = param;
    }

    public Object[] getParam() {
        return param;
    }
    @SafeVarargs
    public final <T> void setParam(T... param) {
        this.param = param;
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return item.getItemStack(1);
    }
}
