package davide.customitems.reforgeCreation;

import davide.customitems.CustomItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.SubType;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ReforgeList;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class Reforge {
    private final String name;
    private Type type;
    private SubType subType;
    private int weight;
    private final int damageModifier;
    private final int healthModifier;
    private final int critChanceModifier;
    private final int defenceModifier;

    private final static CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public Reforge(String name, Type type, int weight, int damageModifier, int critChanceModifier, int healthModifier, int defenceModifier) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    public Reforge(String name, SubType subType, int weight, int damageModifier, int critChanceModifier, int healthModifier , int defenceModifier) {
        this.name = name;
        this.subType = subType;
        this.weight = weight;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    public Reforge(String name, Type type, int damageModifier, int critChanceModifier, int healthModifier , int defenceModifier) {
        this.name = name;
        this.type = type;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    public Reforge(String name, SubType subType, int damageModifier, int critChanceModifier, int healthModifier, int defenceModifier) {
        this.name = name;
        this.subType = subType;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.critChanceModifier = critChanceModifier;
        this.defenceModifier = defenceModifier;

        if (getReforge(name) == null)
            ReforgeList.reforges.add(this);
    }

    public static Reforge randomReforge() {
        //Compute the total weight of all reforges together
        double totalWeight = 0.0;
        for (Reforge reforge : ReforgeList.reforges)
            totalWeight += reforge.getWeight();

        //Now choose a random reforge.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < ReforgeList.reforges.size() - 1; ++idx) {
            r -= ReforgeList.reforges.get(idx).getWeight();
            if (r <= 0.0) break;
        }

        return ReforgeList.reforges.get(idx);
    }

    public static boolean isReforged(ItemStack is) {
        return getReforge(is) != null;
    }

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

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "reforge"), PersistentDataType.STRING, reforge.name);
        is.setItemMeta(meta);

        String name = item.getRarity().getColor() + reforge.getName() +  " " + item.getName();
        item.setName(name, is);

        Item.removeStatsFromLore(is);

        //Damage
        if (reforge.getDamageModifier() != 0) {
            if (r != null)
                Item.setDamage(Item.getBaseDamage(is, r), is, reforge);
            else
                Item.setDamage(Item.getDamage(is), is, reforge);
        } else if (Item.getDamage(is) != 0) {
            if (r != null)
                Item.setDamage(Item.getBaseDamage(is, r), is);
            else
                Item.setDamage(Item.getDamage(is), is);
        }

        //Crit Chance
        if (reforge.getCritChanceModifier() != 0) {
            if (r != null)
                Item.setCritChance(Item.getBaseCritChance(is, r), is, reforge);
            else
                Item.setCritChance(Item.getCritChance(is), is, reforge);
        } else if (Item.getCritChance(is) != 0) {
            if (r != null)
                Item.setCritChance(Item.getBaseCritChance(is, r), is);
            else
                Item.setCritChance(Item.getCritChance(is), is);
        }

        //Health
        if (reforge.getHealthModifier() != 0) {
            if (r != null)
                Item.setHealth(Item.getBaseHealth(is, r), is, reforge);
            else
                Item.setHealth(Item.getHealth(is), is, reforge);
        } else if (Item.getHealth(is) != 0) {
            if (r != null)
                Item.setHealth(Item.getBaseHealth(is, r), is);
            else
                Item.setHealth(Item.getHealth(is), is);
        }

        //Defence
        if (reforge.getDefenceModifier() != 0) {
            if (r != null)
                Item.setDefence(Item.getBaseDefence(is, r), is, reforge);
            else
                Item.setDefence(Item.getDefence(is), is, reforge);
        } else if (Item.getDefence(is) != 0) {
            if (r != null)
                Item.setDefence(Item.getBaseDefence(is, r), is);
            else
                Item.setDefence(Item.getDefence(is), is);
        }
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

    public int getHealthModifier() {
        return healthModifier;
    }

    public int getCritChanceModifier() {
        return critChanceModifier;
    }

    public int getDefenceModifier() {
        return defenceModifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reforge reforge)) return false;
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
                ", healthModifier=" + healthModifier +
                ", critChanceModifier=" + critChanceModifier +
                '}';
    }
}
