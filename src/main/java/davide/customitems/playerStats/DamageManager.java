package davide.customitems.playerStats;

import davide.customitems.api.Instruction;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DamageManager implements Listener {

    public static Damage damageCalculation(ItemStack is, Player player, LivingEntity entity) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        AtomicBoolean isCrit = new AtomicBoolean(false);

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

        AtomicReference<Float> totalDamage = new AtomicReference<>((float) Math.max((Item.getDamage(is) + armorDamage), 0));

        //Adding Enchantment Damage
        int powerPercentage = 25;

        for (Enchantment e : meta.getEnchants().keySet()) {
            int finalPowerPercentage = powerPercentage;
            switch (e.getKey().getKey()) {
                case "sharpness" -> totalDamage.updateAndGet(v -> v + (int) (1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1)));
                case "smite" -> {
                    if (entity.getCategory() == EntityCategory.UNDEAD)
                        totalDamage.updateAndGet(v -> v + 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_UNDEAD));
                }
                case "bane_of_arthropods" -> {
                    if (entity.getCategory() == EntityCategory.ARTHROPOD)
                        totalDamage.updateAndGet(v -> v + 2.5f * meta.getEnchants().get(Enchantment.DAMAGE_ARTHROPODS));
                }
                case "power" -> {
                    powerPercentage *= (meta.getEnchants().get(Enchantment.ARROW_DAMAGE) + 1);
                    totalDamage.updateAndGet(v -> v + totalDamage.get() * finalPowerPercentage / 100);
                }
            }
        }

        //Critical Chance Calculation
        int helmCritChance = 0, chestCritChance = 0, pantsCritChance = 0, bootsCritChance = 0;
        if (armor[0] != null) helmCritChance = Item.getCritChance(armor[0]);
        if (armor[1] != null) chestCritChance = Item.getCritChance(armor[1]);
        if (armor[2] != null) pantsCritChance = Item.getCritChance(armor[2]);
        if (armor[3] != null) bootsCritChance = Item.getCritChance(armor[3]);
        int armorCritChance = helmCritChance + chestCritChance + pantsCritChance + bootsCritChance;

        int totalCrit = Math.max((Item.getCritChance(is) + armorCritChance), 0);
        totalCrit = Math.min(totalCrit, 100);

        //Critical Damage Calculation
        float helmCritDamage = 0, chestCritDamage = 0, pantsCritDamage = 0, bootsCritDamage = 0;
        if (armor[0] != null) helmCritDamage = Item.getCritDamage(armor[0]);
        if (armor[1] != null) chestCritDamage = Item.getCritDamage(armor[1]);
        if (armor[2] != null) pantsCritDamage = Item.getCritDamage(armor[2]);
        if (armor[3] != null) bootsCritDamage = Item.getCritDamage(armor[3]);
        float armorCritDamage = helmCritDamage + chestCritDamage + pantsCritDamage + bootsCritDamage;

        float totalCritDamage = Math.max(Item.getCritDamage(is) + armorCritDamage, 1);

        ChanceManager.chanceCalculation(totalCrit, new Instruction() {
            @Override
            public void run() {
                isCrit.set(true);
                totalDamage.updateAndGet(v -> v * totalCritDamage);
            }
        }, player);

        totalDamage.set(Math.max(totalDamage.get(), 0));
        return new Damage(Item.toItem(is).getKey().getKey(), totalDamage.get(), totalCrit, isCrit.get(), totalCritDamage, meta.getEnchants());
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
        if (player.getAttackCooldown() != 1) return;

        Damage damage = damageCalculation(is, player, (LivingEntity) e.getEntity());
        if (damage == null) return;
        damage.setDamaged(e.getEntity());
        damage.setDamager(player);

        System.out.println(damage);
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
