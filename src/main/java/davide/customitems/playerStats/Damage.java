package davide.customitems.playerStats;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import se.eris.notnull.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Damage {
    private String key;
    private float damage;
    private int critChance;
    private boolean isCrit;
    private float critDamage;
    private Map<Enchantment, Integer> enchants;
    private Entity damaged;
    private Entity damager;

    private static long ID = -1;

    private static final List<Damage> damageList = new ArrayList<>();

    public Damage(String key, float damage, int critChance, boolean isCrit, float critDamage, Map<Enchantment, Integer> enchants) {
        this.key = key;
        this.damage = damage;
        this.critChance = critChance;
        this.isCrit = isCrit;
        this.critDamage = critDamage;
        this.enchants = enchants;

        ID++;
        damageList.add(this);
    }

    public Damage(float damage, int critChance, boolean isCrit, float critDamage, Map<Enchantment, Integer> enchants, Entity damaged, Entity damager) {
        this.damage = damage;
        this.critChance = critChance;
        this.isCrit = isCrit;
        this.critDamage = critDamage;
        this.enchants = enchants;
        this.damaged = damaged;
        this.damager = damager;

        ID++;
        damageList.add(this);
    }

    @Nullable
    public static Damage get(long id) {
        for (Damage d : damageList)
            if (d.getId() == id)
                return d;

        return null;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public boolean isCrit() {
        return isCrit;
    }

    public void setCrit(boolean isCrit) {
        this.isCrit = isCrit;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public float getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(float critDamage) {
        this.critDamage = critDamage;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return enchants;
    }

    public void setEnchants(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
    }

    public Entity getDamaged() {
        return damaged;
    }

    public void setDamaged(Entity damaged) {
        this.damaged = damaged;
    }

    public Entity getDamager() {
        return damager;
    }

    public void setDamager(Entity damager) {
        this.damager = damager;
    }

    public long getId() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Damage damage1 = (Damage) o;
        return damage == damage1.damage && isCrit == damage1.isCrit && critChance == damage1.critChance && Float.compare(critDamage, damage1.critDamage) == 0 && Objects.equals(enchants, damage1.enchants) && Objects.equals(damaged, damage1.damaged) && Objects.equals(damager, damage1.damager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, isCrit, critChance, critDamage, enchants, damaged, damager);
    }

    @Override
    public String toString() {
        return "Damage (" + ID + "){" +
                "key=" + key +
                ", damage=" + damage +
                ", isCrit=" + isCrit +
                ", critChance=" + critChance +
                ", critMulti=" + critDamage +
                ", enchants=" + enchants +
                ", damaged=" + damaged +
                ", damager=" + damager +
                '}';
    }
}
