package davide.customitems.reforgeCreation;

import davide.customitems.CustomItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.SubType;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ReforgeList;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reforge {
    private final String name;
    private Type type;
    private SubType subType;
    private int weight = 0;
    private final int damageModifier;
    private final int healthModifier;
    private final int critChanceModifier;
    private final float critDamageModifier;
    private final int defenceModifier;

    private final static CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public Reforge(String name, Type type, int weight, int damageModifier, int critChanceModifier, float critDamageModifier, int healthModifier, int defenceModifier) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.critDamageModifier = critDamageModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    public Reforge(String name, SubType subType, int weight, int damageModifier, int critChanceModifier, float critDamageModifier, int healthModifier , int defenceModifier) {
        this.name = name;
        this.subType = subType;
        this.weight = weight;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.critDamageModifier = critDamageModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    @NotNull
    public static Reforge randomReforge() {
        //Compute the total weight of all reforges together
        double totalWeight = 0;
        for (Reforge reforge : ReforgeList.reforges)
            totalWeight += reforge.getWeight();

        //Now choose a random reforge.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < ReforgeList.reforges.size() - 1; ++idx) {
            r -= ReforgeList.reforges.get(idx).getWeight();
            if (r <= 0) break;
        }

        return ReforgeList.reforges.get(idx);
    }

    @Nullable
    public static Reforge randomReforge(Type type) {
        List<Reforge> targetedReforges = new ArrayList<>();
        for (Reforge r : ReforgeList.reforges)
            if (r.getType() == type) targetedReforges.add(r);

        if (targetedReforges.isEmpty()) return null;

        //Compute the total weight of all reforges together
        double totalWeight = 0.0;
        for (Reforge reforge : targetedReforges)
            totalWeight += reforge.getWeight();

        //Now choose a random reforge.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < targetedReforges.size() - 1; ++idx) {
            r -= targetedReforges.get(idx).getWeight();
            if (r <= 0.0) break;
        }

        return targetedReforges.get(idx);
    }

    public static boolean isReforged(ItemStack is) {
        return getReforge(is) != null;
    }

    @Nullable
    public static Reforge getReforge(ItemStack is) {
        if (!Item.isCustomItem(is)) return null;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        Reforge r = null;
        String rName = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "reforge"), PersistentDataType.STRING);

        for (Reforge reforge : ReforgeList.reforges)
            if (reforge.name.equalsIgnoreCase(rName)) {
                r = reforge;
                break;
            }

        return r;
    }

    public static Reforge getReforge(String name) {
        Reforge r = null;

        for (Reforge reforge : ReforgeList.reforges)
            if (reforge.name.equalsIgnoreCase(name)) {
                r = reforge;
                break;
            }

        return r;
    }

    public static void setReforge(Reforge reforge, ItemStack is) {
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Reforge r = getReforge(is);

        String name = Item.getRarity(is).getColor() + reforge.getName() +  " " + Item.getName(is);
        Item.setName(name, is);

        meta = is.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "reforge"), PersistentDataType.STRING, reforge.name);
        is.setItemMeta(meta);

        if (r != null)
            Item.setStats(Item.getBaseDamage(is, r), Item.getBaseCritChance(is, r), Item.getBaseCritDamage(is, r), Item.getBaseHealth(is, r), Item.getBaseDefence(is, r), is, true);
        else
            Item.setStats(Item.getDamage(is), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, true);
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

    public int getBaseDamageModifier() {
        return damageModifier;
    }

    public static int getDamageModifier(ItemStack is, Reforge r) {
        if (is == null) return 0;

        return (int) (r.getBaseDamageModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2));
    }

    public int getBaseHealthModifier() {
        return healthModifier;
    }

    public static int getHealthModifier(ItemStack is, Reforge r) {
        if (is == null) return 0;

        return (int) (r.getBaseHealthModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2));
    }

    public int getBaseCritChanceModifier() {
        return critChanceModifier;
    }

    public static int getCritChanceModifier(ItemStack is, Reforge r) {
        if (is == null) return 0;

        return (int) (r.getBaseCritChanceModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2));
    }

    public float getBaseCritDamageModifier() {
        return critDamageModifier;
    }

    public static float getCritDamageModifier(ItemStack is, Reforge r) {
        if (is == null) return 0;

        return r.getBaseCritDamageModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2);
    }

    public int getBaseDefenceModifier() {
        return defenceModifier;
    }

    public static int getDefenceModifier(ItemStack is, Reforge r) {
        if (is == null) return 0;

        return (int) (r.getBaseDefenceModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reforge reforge = (Reforge) o;
        return weight == reforge.weight && damageModifier == reforge.damageModifier && healthModifier == reforge.healthModifier && critChanceModifier == reforge.critChanceModifier && Float.compare(critDamageModifier, reforge.critDamageModifier) == 0 && defenceModifier == reforge.defenceModifier && Objects.equals(name, reforge.name) && type == reforge.type && subType == reforge.subType;
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
                ", healthModifier=" + healthModifier +
                ", critChanceModifier=" + critChanceModifier +
                '}';
    }
}
