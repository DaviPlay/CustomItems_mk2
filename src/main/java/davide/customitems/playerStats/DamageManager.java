package davide.customitems.playerStats;

import davide.customitems.api.VanillaItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DamageManager implements Listener {

    public static Damage damageCalculation(ItemStack is, Player player, LivingEntity entity) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        boolean isCrit = false;

        //Damage Calculation
        ItemStack[] armor = player.getInventory().getArmorContents();

        int helmDamage = 0, chestDamage = 0, pantsDamage = 0, bootsDamage = 0;
        if (armor[0] != null) {
            helmDamage = Item.getDamage(armor[0]);
        }
        if (armor[1] != null) {
            chestDamage = Item.getDamage(armor[1]);
        }
        if (armor[2] != null) {
            pantsDamage = Item.getDamage(armor[2]);
        }
        if (armor[3] != null) {
            bootsDamage = Item.getDamage(armor[3]);
        }
        int armorDamage = helmDamage + chestDamage + pantsDamage + bootsDamage;

        float totalDamage = Math.max((Item.getDamage(is) + armorDamage), 0);

        //Adding Enchantment Damage
        int powerPercentage = 25;

        for (Enchantment e : meta.getEnchants().keySet()) {
            switch (e.getKey().getKey()) {
                case "sharpness" -> totalDamage += (int) (1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1));
                case "smite" -> {
                    if (entity.getCategory() == EntityCategory.UNDEAD)
                        totalDamage += 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_UNDEAD);
                }
                case "bane_of_arthropods" -> {
                    if (entity.getCategory() == EntityCategory.ARTHROPOD)
                        totalDamage += 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_ARTHROPODS);
                }
                case "power" -> {
                    powerPercentage *= (meta.getEnchants().get(Enchantment.ARROW_DAMAGE) + 1);
                    totalDamage += totalDamage * powerPercentage / 100;
                }
            }
        }

        /*
        if (meta.hasEnchants()) {
            if (meta.getEnchants().containsKey(Enchantment.DAMAGE_ALL))
                totalDamage += (int) (1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1));
            else if (entity != null && meta.getEnchants().containsKey(Enchantment.DAMAGE_UNDEAD)) {
                if (entity.getCategory() == EntityCategory.UNDEAD)
                    totalDamage += 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_UNDEAD);
            } else if (entity != null && meta.getEnchants().containsKey(Enchantment.DAMAGE_ARTHROPODS)) {
                if (entity.getCategory() == EntityCategory.ARTHROPOD)
                    totalDamage += 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_ARTHROPODS);
            } else if (meta.getEnchants().containsKey(Enchantment.ARROW_DAMAGE)) {
                powerPercentage *= (meta.getEnchants().get(Enchantment.ARROW_DAMAGE) + 1);
                totalDamage += totalDamage * powerPercentage / 100;
            }
        }

         */

        //Critical Chance Calculation
        int helmCritChance = 0, chestCritChance = 0, pantsCritChance = 0, bootsCritChance = 0;
        if (armor[0] != null) helmCritChance = Item.getCritChance(armor[0]);
        if (armor[1] != null) chestCritChance = Item.getCritChance(armor[1]);
        if (armor[2] != null) pantsCritChance = Item.getCritChance(armor[2]);
        if (armor[3] != null) bootsCritChance = Item.getCritChance(armor[3]);
        int armorCritChance = helmCritChance + chestCritChance + pantsCritChance + bootsCritChance;

        int totalCrit = Math.max((Item.getCritChance(is) + armorCritChance), 0);
        if (totalCrit > 100) totalCrit = 100;

        //Critical Damage Calculation
        float helmCritDamage = 0, chestCritDamage = 0, pantsCritDamage = 0, bootsCritDamage = 0;
        if (armor[0] != null) helmCritDamage = Item.getCritDamage(armor[0]);
        if (armor[1] != null) chestCritDamage = Item.getCritDamage(armor[1]);
        if (armor[2] != null) pantsCritDamage = Item.getCritDamage(armor[2]);
        if (armor[3] != null) bootsCritDamage = Item.getCritDamage(armor[3]);
        float armorCritDamage = helmCritDamage + chestCritDamage + pantsCritDamage + bootsCritDamage;

        float totalCritDamage = Item.getCritDamage(is) + armorCritDamage;

        if (new Random().nextInt(100) <= totalCrit) {
            isCrit = true;
            totalDamage *= (int) (totalCritDamage);
        }
        totalDamage = Math.max(totalDamage, 0);

        return new Damage(totalDamage, totalCrit, isCrit, totalCritDamage, meta.getEnchants());
    }

    @EventHandler
    private void onMeleeHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        Item item = Item.toItem(is);
        if (item == null) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && item.getType() == Type.RANGED) return;
        if (meta == null) return;

        Damage damage = damageCalculation(is, player, (LivingEntity) e.getEntity());
        if (damage == null) return;
        damage.setDamaged(e.getEntity());
        damage.setDamager(player);

        //Checking if the weapon is enchanted for damage comparison with vanilla counterpart
        float sharpDamage = 0;
        if (meta.hasEnchants() && meta.getEnchants().containsKey(Enchantment.DAMAGE_ALL))
            sharpDamage = 1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1);

        //Checking if the attack is charged
        for (VanillaItems d : VanillaItems.values())
            if (is.getType() == d.getType())
                if (e.getDamage() - sharpDamage != d.getExpectedDamage()) {
                    if (e.getDamage() - sharpDamage == d.getExpectedDamage() + (d.getExpectedDamage() / 2)) {
                        break;
                    }
                    return;
                }

        if (damage.isCrit())
            player.sendMessage("§cCritical Hit!");

        e.setDamage(damage.getDamage());
    }

    @EventHandler
    private void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!Item.isCustomItem(is)) return;

        Damage damage = damageCalculation(is, player, null);
        if (damage == null) return;

        arrow.setCritical(false);
        arrow.setDamage(damage.getDamage());
    }
}
