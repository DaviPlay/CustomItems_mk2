package davide.customitems.playerStats;

import davide.customitems.itemCreation.Item;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class DefenceCalculation implements Listener {

    public static int getTotalDefence(ItemStack is, Player player) {
        //Damage Calculation
        Reforge weaponReforge = Reforge.getReforge(is);
        Reforge helmReforge = null;
        Reforge chestReforge = null;
        Reforge pantsReforge = null;
        Reforge bootsReforge = null;
        int weaponDefence = Item.getDefence(is);

        ItemStack[] armor = player.getInventory().getArmorContents();

        int helmDefence = 0, chestDefence = 0, pantsDefence = 0, bootsDefence = 0;
        if (armor[0] != null) {
            helmDefence = Item.getDefence(armor[0]);
            helmReforge = Reforge.getReforge(armor[0]);
        }
        if (armor[1] != null) {
            chestDefence = Item.getDefence(armor[1]);
            chestReforge = Reforge.getReforge(armor[1]);
        }
        if (armor[2] != null) {
            pantsDefence = Item.getDefence(armor[2]);
            pantsReforge = Reforge.getReforge(armor[2]);
        }
        if (armor[3] != null) {
            bootsDefence = Item.getDefence(armor[3]);
            bootsReforge = Reforge.getReforge(armor[3]);
        }
        int armorDefence = helmDefence + chestDefence + pantsDefence + bootsDefence;

        int weaponReforgeDefence = weaponReforge != null && weaponReforge.getDefenceModifier() != 0 ? weaponReforge.getDefenceModifier() : 0;

        int helmReforgeDefence = helmReforge != null && helmReforge.getDefenceModifier() != 0 ? helmReforge.getDefenceModifier() : 0;
        int chestReforgeDefence = chestReforge != null && chestReforge.getDefenceModifier() != 0 ? chestReforge.getDefenceModifier() : 0;
        int pantsReforgeDefence = pantsReforge != null && pantsReforge.getDefenceModifier() != 0 ? pantsReforge.getDefenceModifier() : 0;
        int bootsReforgeDefence = bootsReforge != null && bootsReforge.getDefenceModifier() != 0 ? bootsReforge.getDefenceModifier() : 0;

        int armorReforgeDefence = helmReforgeDefence + chestReforgeDefence + pantsReforgeDefence + bootsReforgeDefence;

        return Math.max((weaponDefence + armorDefence + weaponReforgeDefence + armorReforgeDefence), 0);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();

        e.setDamage(e.getDamage() - getTotalDefence(is, player));
    }
}
