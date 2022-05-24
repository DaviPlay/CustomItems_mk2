package davide.customitems.PlayerStats;

import davide.customitems.ItemCreation.Item;
import davide.customitems.ReforgeCreation.Reforge;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class DamageCalculation implements Listener {

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        //Damage Calculation
        int weaponDamage = Item.getTemporaryDamage(is);

        ItemStack[] armor = player.getInventory().getArmorContents();

        int helmDamage = 0, chestDamage = 0, pantsDamage = 0, bootsDamage = 0;
        if (armor[0] != null) helmDamage = Item.getTemporaryDamage(armor[0]);
        if (armor[1] != null) chestDamage = Item.getTemporaryDamage(armor[1]);
        if (armor[2] != null) pantsDamage = Item.getTemporaryDamage(armor[2]);
        if (armor[3] != null) bootsDamage = Item.getTemporaryDamage(armor[3]);
        int armorDamage = helmDamage + chestDamage + pantsDamage + bootsDamage;

        float totalDamage = Math.max((weaponDamage + armorDamage), 0);

        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet())
            if (entry.getKey().equals(Enchantment.DAMAGE_ALL))
                switch (entry.getValue()) {
                    case 1:
                        totalDamage += 2;
                        break;

                    case 2:
                        totalDamage += 2.5;
                        break;

                    case 3:
                        totalDamage += 3;
                        break;

                    case 4:
                        totalDamage += 3.5;
                        break;

                    case 5:
                        totalDamage += 5;
                        break;
                }

        //Critical Chance Calculation
        int weaponCrit = Item.getTemporaryCritChance(is);

        int helmCrit = 0, chestCrit = 0, pantsCrit = 0, bootsCrit = 0;
        if (armor[0] != null) helmCrit = Item.getTemporaryCritChance(armor[0]);
        if (armor[1] != null) chestCrit = Item.getTemporaryCritChance(armor[1]);
        if (armor[2] != null) pantsCrit = Item.getTemporaryCritChance(armor[2]);
        if (armor[3] != null) bootsCrit = Item.getTemporaryCritChance(armor[3]);
        int armorCrit = helmCrit + chestCrit + pantsCrit + bootsCrit;

        int totalCrit = Math.max((weaponCrit + armorCrit), 0);
        if (totalCrit > 100) totalCrit = 100;

        if (new Random().nextInt(100) <= totalCrit) {
            totalDamage *= 2;
            player.sendMessage("§cCrit!");
        }

        if (totalDamage < 0) totalDamage = 0;

        e.setDamage(totalDamage);

        //Damage Stats Debug
        /*
        Reforge reforge = Reforge.getReforge(is);
        player.sendMessage("Total damage dealt: " + e.getDamage());
        player.sendMessage("Weapon damage dealt: " + item.getBaseDamage());
        if (reforge != null)
            player.sendMessage("Reforge damage dealt: " + reforge.getDamageModifier());
        player.sendMessage("Armor damage dealt: " + armorDamage);

        LivingEntity entity = (LivingEntity) e.getEntity();
        player.sendMessage(entity.getHealth() - e.getDamage() + " / " + entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + " HP");
        player.sendMessage("");
        */
    }
}
