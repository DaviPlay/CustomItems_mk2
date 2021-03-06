package davide.customitems.reforgeCreation;

import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ReforgeList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ReforgeAssigning implements Listener, CommandExecutor {

    @EventHandler
    private void assignReforgeOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        while (true) {
            Reforge reforge = Reforge.randomReforge();

            if (reforge.getType() != null && item.getSubType() != null) {
                if (reforge.getType() == item.getType()|| reforge.getType() == item.getSubType().getType()) {
                    Item.setReforge(reforge, is);
                    break;
                }
            }
            else if (reforge.getSubType() != null) {
                if (reforge.getSubType() == item.getSubType()) {
                    Item.setReforge(reforge, is);
                    break;
                }
            }
            else break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setReforge")) {
            if (getReforge(args[0]) == null) {
                player.sendMessage("§cThat reforge doesn't exist!");
                return true;
            }

            Item.setReforge(getReforge(args[0]), player.getInventory().getItemInMainHand());
        }

        return false;
    }

    private Reforge getReforge(String name) {
        Reforge reforge = null;

        for (Reforge r : ReforgeList.reforges)
            if (r.getName().equalsIgnoreCase(name))
                reforge = r;

        return reforge;
    }
}
