package davide.customitems.ReforgeCreation;

import davide.customitems.ItemCreation.SubType;
import davide.customitems.ItemCreation.Type;
import davide.customitems.ItemCreation.Item;
import davide.customitems.Lists.ReforgeList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Reforge {
    private final String name;
    private Type type;
    private SubType subType;
    private final int weight;
    private final int damageModifier;

    public Reforge(String name, Type type, int weight, int damageModifier) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.damageModifier = damageModifier;
    }

    public Reforge(String name, SubType subType, int weight, int damageModifier) {
        this.name = name;
        this.subType = subType;
        this.weight = weight;
        this.damageModifier = damageModifier;
    }

    public static Reforge randomReforge() {
        //Compute the total weight of all reforges together
        double totalWeight = 0.0;
        for (Reforge reforge : ReforgeList.reforges)
            totalWeight += reforge.getWeight();

        //Now choose a random reforge.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < ReforgeList.reforges.length - 1; ++idx) {
            r -= ReforgeList.reforges[idx].getWeight();
            if (r <= 0.0) break;
        }

        return ReforgeList.reforges[idx];
    }

    public static Reforge getReforge(ItemStack is) {
        Item item = Item.toItem(is);
        if (item == null) return null;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        String name = meta.getDisplayName();
        Reforge r = null;

        for (Reforge reforge : ReforgeList.reforges)
            if (name.contains(reforge.getName())) {
                name = name.replace(item.getName(), "").replace(" ", "").replace(name.charAt(1), '§').replace("§", "");
                break;
            }

        for (Reforge reforge : ReforgeList.reforges)
            if (reforge.getName().equalsIgnoreCase(name)) {
                r = reforge;
                break;
            }

        return r;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public SubType getSubType() {
        return subType;
    }

    public int getWeight() {
        return weight;
    }

    public int getDamageModifier() {
        return damageModifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reforge)) return false;
        Reforge reforge = (Reforge) o;
        return weight == reforge.weight && damageModifier == reforge.damageModifier && Objects.equals(name, reforge.name) && type == reforge.type && subType == reforge.subType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, subType, weight, damageModifier);
    }

    @Override
    public String toString() {
        return "Reforge{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", subType=" + subType +
                ", weight=" + weight +
                ", damageModifier=" + damageModifier +
                '}';
    }
}
