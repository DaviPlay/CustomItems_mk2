package davide.customitems.gui;

import davide.customitems.api.Utils;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemsGUI implements IGUI, CommandExecutor {
    public static List<Inventory> itemInv = new ArrayList<>();

    public ItemsGUI() {
        itemInv.add(Bukkit.createInventory(this, 54, "Items"));
        setInv();
    }

    private void setInv() {
        List<Item> items = ItemList.items.get(0);

        int j = 0, k = 0;
        for (int i = 9; i < 45; i++) {
            if (items.get(k).isShowInGui())
                itemInv.get(j).setItem(i, items.get(k).getItemStack());
            else {
                if (i > 9)
                    i--;
            }

            k++;

            if (k > items.size() - 1)
                break;

            if (i == 44) {
                itemInv.add(Bukkit.createInventory(this, 54, "Items"));
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

            inv.setItem(49, ItemList.closeBarrier.getItemStack());

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

    int currentInv = 0;
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem == null) return;
        ItemMeta meta = clickedItem.getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();
        Item item = Item.toItem(clickedItem);

        if (item != null && !ItemList.utilsItems.contains(item)) {
            if (container != null)
                if (container.getKeys().contains(item.getKey()))
                    if (whoClicked.getGameMode() == GameMode.CREATIVE) {
                        if (clickType.isLeftClick()) {
                            if (item.hasRandomUUID())
                                Item.setRandomUUID(clickedItem);

                            Utils.addToInventory(whoClicked, clickedItem);
                        } else if (clickType.isRightClick()) {
                            if (CraftingInventories.getInv(item.getKey()) != null)
                                whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                        }
                    } else {
                        if (CraftingInventories.getInv(item.getKey()) != null)
                            whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                    }
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(itemInv.get(currentInv));
                }
            }
            case 49 -> whoClicked.closeInventory();
            case 53 -> {
                if (currentInv < itemInv.size() - 1) {
                    currentInv++;
                    whoClicked.openInventory(itemInv.get(currentInv));
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
