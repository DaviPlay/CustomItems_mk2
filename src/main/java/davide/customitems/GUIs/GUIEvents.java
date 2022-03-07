package davide.customitems.GUIs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIEvents implements Listener {

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clickedInv = e.getClickedInventory();
        if (clickedInv == null) return;
        if (!clickedInv.equals(GUI.inv)) return;
        if (e.getCurrentItem() == null) return;

        if (e.getClick().isLeftClick() && clickedInv.equals(GUI.inv))
            if (!e.getCurrentItem().equals(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)))
                player.getInventory().addItem(e.getCurrentItem());

        e.setCancelled(true);
    }
}
