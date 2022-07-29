package davide.customitems.gui;

import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ItemsGUI implements CommandExecutor {
    public static List<Inventory> itemInv = new ArrayList<>();

    public ItemsGUI() {
        itemInv.add(Bukkit.createInventory(null, 54, "Items"));
        setInv();
    }

    private void setInv() {
        List<Item> items = ItemList.items.get(0);

        int j = 0, k = 0;
        for (int i = 9; i < 45; i++) {
            itemInv.get(j).setItem(i, items.get(k).getItemStack());
            k++;

            if (k > items.size() - 1)
                break;

            if (i == 44) {
                itemInv.add(Bukkit.createInventory(null, 54, "Items"));
                i = 8;
                j++;
            }
        }

        int n = 0;
        for (Inventory inv : itemInv) {
            for (int i = 0; i < 9; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());
            for (int i = 45; i < 54; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());

            if (n != itemInv.size() - 1)
                inv.setItem(53, ItemList.nextArrow.getItemStack());
            if (n != 0)
                inv.setItem(45,ItemList.backArrow.getItemStack());
            n++;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("customitems"))
            player.openInventory(itemInv.get(0));

        return false;
    }
}
