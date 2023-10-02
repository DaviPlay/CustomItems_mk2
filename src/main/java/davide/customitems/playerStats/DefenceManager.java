package davide.customitems.playerStats;

import davide.customitems.itemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DefenceManager implements Listener {

    public static int getTotalDefence(ItemStack is, Player player) {
        //Damage Calculation
        ItemStack[] armor = player.getInventory().getArmorContents();

        int helmDefence = 0, chestDefence = 0, pantsDefence = 0, bootsDefence = 0;
        if (armor[0] != null) helmDefence = Item.getDefence(armor[0]);
        if (armor[1] != null) chestDefence = Item.getDefence(armor[1]);
        if (armor[2] != null) pantsDefence = Item.getDefence(armor[2]);
        if (armor[3] != null) bootsDefence = Item.getDefence(armor[3]);
        int armorDefence = helmDefence + chestDefence + pantsDefence + bootsDefence;

        return Math.max((Item.getDefence(is) + armorDefence), 0);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();

        e.setDamage(e.getDamage() - getTotalDefence(is, player));
    }
}
