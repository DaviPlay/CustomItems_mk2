package davide.customitems.GUIs;

import davide.customitems.ItemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUI implements CommandExecutor {
    public static Inventory inv;

    public GUI() {
        inv = Bukkit.createInventory(null, 54, "CustomItems");
        setInv();
    }

    private void setInv() {
        for (int i = 0; i < 9; i++)
            inv.setItem(i, Item.fillerGlass.getItemStack());
        for (int i = 45; i < 54; i++)
            inv.setItem(i, Item.fillerGlass.getItemStack());

        for (int i = 9; i < 45; i++) {
            if (i - 9 == Item.items.length) return;

            inv.setItem(i, Item.items[i - 9].getItemStack());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("customitems"))
            player.openInventory(inv);

        return false;
    }
}
