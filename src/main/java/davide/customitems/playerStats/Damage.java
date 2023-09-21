package davide.customitems.playerStats;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Damage {
    private int damage;
    private int critChance;
    private boolean isCrit;
    private float critDamage;
    private Map<Enchantment, Integer> enchants;
    private Entity damaged;
    private Entity damager;

    private final UUID ID;

    public Damage(int damage, int critChance, boolean isCrit, float critDamage, Map<Enchantment, Integer> enchants) {
        this.damage = damage;
        this.critChance = critChance;
        this.isCrit = isCrit;
        this.critDamage = critDamage;
        this.enchants = enchants;

        ID = UUID.randomUUID();
    }

    public Damage(int damage, int critChance, boolean isCrit, float critDamage, Map<Enchantment, Integer> enchants, Entity damaged, Entity damager) {
        this.damage = damage;
        this.critChance = critChance;
        this.isCrit = isCrit;
        this.critDamage = critDamage;
        this.enchants = enchants;
        this.damaged = damaged;
        this.damager = damager;

        ID = UUID.randomUUID();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
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

    public UUID getID() {
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
        return "Damage{" +
                "damage=" + damage +
                ", isCrit=" + isCrit +
                ", critChance=" + critChance +
                ", critMulti=" + critDamage +
                ", enchants=" + enchants +
                ", damaged=" + damaged +
                ", damager=" + damager +
                '}';
    }
}
