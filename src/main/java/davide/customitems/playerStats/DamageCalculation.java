package davide.customitems.playerStats;

import davide.customitems.api.VanillaItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

        if (meta != null && !meta.getEnchants().isEmpty())
            switch (meta.getEnchants().get(Enchantment.DAMAGE_ALL)) {
                case 1 -> totalDamage += 1;
                case 2 -> totalDamage += 1.5;
                case 3 -> totalDamage += 2.5;
                case 4 -> totalDamage += 4;
                case 5 -> totalDamage += 5;
                case 6 -> totalDamage += 6;
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
            totalDamage *= 2;
            player.sendMessage("§cCrit!");
        }

        return Math.max(totalDamage, 0);
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();

        for (VanillaItems d : VanillaItems.values())
            if (is.getType() == d.getType())
                if (e.getDamage() != d.getExpectedDamage()) {
                    if (e.getDamage() == d.getExpectedDamage() + (d.getExpectedDamage() / 2)) {
                        break;
                    }

                    return;
                }

        e.setDamage(getTotalDamage(is, player));

        //Damage Stats Debug
        /*
        player.sendMessage("Total damage dealt: " + e.getDamage());
        player.sendMessage("Weapon damage dealt: " + Item.getDamage(is));
        if (weaponReforge != null)
            player.sendMessage("Weapon Reforge damage dealt: " + weaponReforge.getDamageModifier());
        player.sendMessage("Armor damage dealt: " + armorDamage);
        if (armorReforgeDamage != 0)
            player.sendMessage("Armor Reforge damage dealt: " + armorReforgeDamage);

        LivingEntity entity = (LivingEntity) e.getEntity();
        player.sendMessage(entity.getHealth() - e.getDamage() + " / " + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + " HP");
        player.sendMessage("");
        */
    }
}
