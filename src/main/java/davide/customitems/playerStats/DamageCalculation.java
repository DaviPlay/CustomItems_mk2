package davide.customitems.playerStats;

import davide.customitems.api.Utils;
import davide.customitems.api.VanillaItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DamageCalculation implements Listener {

    public static int getTotalDamage(ItemStack is, Player player) {
        ItemMeta meta = is.getItemMeta();

        //Damage Calculation
        Reforge weaponReforge = Reforge.getReforge(is);
        Reforge helmReforge = null;
        Reforge chestReforge = null;
        Reforge pantsReforge = null;
        Reforge bootsReforge = null;
        int weaponDamage = Item.getDamage(is);

        ItemStack[] armor = player.getInventory().getArmorContents();

        int helmDamage = 0, chestDamage = 0, pantsDamage = 0, bootsDamage = 0;
        if (armor[0] != null) {
            helmDamage = Item.getDamage(armor[0]);
            helmReforge = Reforge.getReforge(armor[0]);
        }
        if (armor[1] != null) {
            chestDamage = Item.getDamage(armor[1]);
            chestReforge = Reforge.getReforge(armor[1]);
        }
        if (armor[2] != null) {
            pantsDamage = Item.getDamage(armor[2]);
            pantsReforge = Reforge.getReforge(armor[2]);
        }
        if (armor[3] != null) {
            bootsDamage = Item.getDamage(armor[3]);
            bootsReforge = Reforge.getReforge(armor[3]);
        }
        int armorDamage = helmDamage + chestDamage + pantsDamage + bootsDamage;

        int weaponReforgeDamage = weaponReforge != null && weaponReforge.getDamageModifier() != 0 ? weaponReforge.getDamageModifier() : 0;

        int helmReforgeDamage = helmReforge != null && helmReforge.getDamageModifier() != 0 ? helmReforge.getDamageModifier() : 0;
        int chestReforgeDamage = chestReforge != null && chestReforge.getDamageModifier() != 0 ? chestReforge.getDamageModifier() : 0;
        int pantsReforgeDamage = pantsReforge != null && pantsReforge.getDamageModifier() != 0 ? pantsReforge.getDamageModifier() : 0;
        int bootsReforgeDamage = bootsReforge != null && bootsReforge.getDamageModifier() != 0 ? bootsReforge.getDamageModifier() : 0;

        int armorReforgeDamage = helmReforgeDamage + chestReforgeDamage + pantsReforgeDamage + bootsReforgeDamage;

        int totalDamage = Math.max((weaponDamage + armorDamage + weaponReforgeDamage + armorReforgeDamage), 0);

        //Adding Enchantment Damage
        int percentage = 25;
        if (meta != null && meta.hasEnchants()) {
            if (meta.getEnchants().containsKey(Enchantment.DAMAGE_ALL))
                totalDamage += 1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1);
            else if (meta.getEnchants().containsKey(Enchantment.ARROW_DAMAGE)) {
                percentage *= (meta.getEnchants().get(Enchantment.ARROW_DAMAGE) + 1);
                totalDamage += totalDamage * (double) percentage / 100;
            }
        }

        //Critical Chance Calculation
        int weaponCrit = Item.getCritChance(is);

        int helmCrit = 0, chestCrit = 0, pantsCrit = 0, bootsCrit = 0;
        if (armor[0] != null) helmCrit = Item.getCritChance(armor[0]);
        if (armor[1] != null) chestCrit = Item.getCritChance(armor[1]);
        if (armor[2] != null) pantsCrit = Item.getCritChance(armor[2]);
        if (armor[3] != null) bootsCrit = Item.getCritChance(armor[3]);
        int armorCrit = helmCrit + chestCrit + pantsCrit + bootsCrit;

        int reforgeCrit = weaponReforge != null && weaponReforge.getCritChanceModifier() != 0 ? weaponReforge.getCritChanceModifier() : 0;

        int totalCrit = Math.max((weaponCrit + armorCrit + reforgeCrit), 0);
        if (totalCrit > 100) totalCrit = 100;

        if (new Random().nextInt(100) <= totalCrit) {
            totalDamage *= Item.getCritDamage(is);
            player.sendMessage("§cCrit!");
        }

        return Math.max(totalDamage, 0);
    }

    @EventHandler
    private void onMeleeHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;
        if (item.getType() == Type.RANGED) return;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        float sharpDamage = 0;
        if (meta.hasEnchants() && meta.getEnchants().containsKey(Enchantment.DAMAGE_ALL))
            sharpDamage = 1 + 0.5f * (meta.getEnchants().get(Enchantment.DAMAGE_ALL) - 1);

        for (VanillaItems d : VanillaItems.values())
            if (is.getType() == d.getType())
                if (e.getDamage() - sharpDamage != d.getExpectedDamage()) {
                    if (e.getDamage() - sharpDamage == d.getExpectedDamage() + (d.getExpectedDamage() / 2)) {
                        break;
                    }
                    return;
                }

        e.setDamage(getTotalDamage(is, player));
    }

    @EventHandler
    private void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!Item.isCustomItem(is)) return;

        arrow.setCritical(false);
        arrow.setDamage(getTotalDamage(is, player));
    }
}
