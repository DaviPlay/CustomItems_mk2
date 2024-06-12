package davide.customitems.events;

import davide.customitems.CustomItems;
import davide.customitems.api.Utils;
import davide.customitems.gui.itemCreationGUIs.Events;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class GeneralEvents implements Listener {
    private static CustomItems plugin;

    public GeneralEvents(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        GeneralEvents.plugin = plugin;
    }

    @EventHandler
    private void interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        assert item != null;
        if (Utils.validateItem(is, player, e))
            return;

        for (Ability a : item.getAbilities()) {
            Events event = a.event();
            if (event == null) return;

            switch (event) {
                case THROW -> EventList.throwItem(player, is, (int) Math.floor(event.getParam1()));
                case TELEPORT -> EventList.teleportPlayer(player, (int) Math.floor(event.getParam1()));
            }
        }
    }

    @EventHandler
    private void sneak(PlayerToggleSneakEvent e) {

    }

    @EventHandler
    private void hit(EntityDamageByEntityEvent e) {

    }
}
