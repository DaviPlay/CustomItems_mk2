package davide.customitems.API;

import davide.customitems.ItemCreation.Item;
import davide.customitems.ReforgeCreation.Reforge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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

        e.setDamage(Item.getDamage(is));

        /*Damage Stats Debug

        Reforge reforge = Reforge.getReforge(is);
        if (reforge != null) {
            player.sendMessage("Total damage dealt: " + Item.getDamage(is));
            player.sendMessage("Base damage dealt: " + item.getBaseDamage());
            player.sendMessage("Reforge damage dealt: " + reforge.getDamageModifier());
        }
        else {
            player.sendMessage("Total damage dealt: " + Item.getDamage(is));
            player.sendMessage("Base damage dealt: " + item.getBaseDamage());
            player.sendMessage("Reforge damage dealt: " + 0);
        }*/
    }
}
