package davide.customitems.gui;

import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ItemsGUI implements CommandExecutor {
    public static Inventory itemInv;
    public static Inventory materialInv;

    public ItemsGUI() {
        itemInv = Bukkit.createInventory(null, 54, "Items");
        materialInv = Bukkit.createInventory(null, 54, "Materials");
        setInv();
    }

    private void setInv() {
        List<Item> items = ItemList.items.get(0);
        List<Item> mats = ItemList.items.get(1);

        for (int i = 0; i < 9; i++) {
            itemInv.setItem(i, ItemList.fillerGlass.getItemStack());
            materialInv.setItem(i, ItemList.fillerGlass.getItemStack());
        }
        for (int i = 45; i < 54; i++) {
            itemInv.setItem(i, ItemList.fillerGlass.getItemStack());
            materialInv.setItem(i, ItemList.fillerGlass.getItemStack());
        }

        for (int i = 9; i < 45; i++)
            if (i - 9 < items.size())
                itemInv.setItem(i, items.get(i - 9).getItemStack());

        for (int i = 9; i < 45; i++)
            if (i - 9 < mats.size())
                materialInv.setItem(i, mats.get(i - 9).getItemStack());

        itemInv.setItem(53, ItemList.matsArrow.getItemStack());
        materialInv.setItem(45, ItemList.itemArrow.getItemStack());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("customitems"))
            player.openInventory(itemInv);

        return false;
    }
}
