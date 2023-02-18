package davide.customitems.gui;

import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ViewMatRecipe implements IGUI {
    private static final List<Inventory> craftingInvs = new ArrayList<>();
    private static final List<Inventory> newInvs = new ArrayList<>();
    int n = 0;

    public ViewMatRecipe(Item item) {
        craftingInvs.clear();
        newInvs.clear();
        setInvs(item);
        n = 0;
    }

    private void setInvs(Item item) {
        for (Map.Entry<NamespacedKey, Inventory> entry : CraftingInventories.getInvs().entrySet()) {
            Inventory inv = entry.getValue();

            for (int i = 10; i <= 30; i++) {
                if (i != 25) {
                    ItemStack is = inv.getItem(i);

                    if (is != null) {
                        Item mat = Item.toItem(is);

                        if (mat != null && mat.equals(item)) {
                            craftingInvs.add(inv);
                            break;
                        }
                    }
                }
            }
        }

        for (Inventory i : craftingInvs) {
            Inventory inv = Bukkit.createInventory(this, 54, item.getName());
            inv.setContents(i.getContents());

            newInvs.add(inv);
        }

        n = 0;

        for (Inventory inv : newInvs) {
            inv.setItem(45, ItemList.fillerGlass.getItemStack());

            if (n > 0)
                inv.setItem(48, ItemList.backArrow.getItemStack());

            if (n < newInvs.size() - 1)
                inv.setItem(50, ItemList.nextArrow.getItemStack());

            n++;
        }
    }

    public static List<Inventory> getInvs() {
        return newInvs;
    }

    int currentInv = 0;
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 48 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(newInvs.get(currentInv));
                }
            }
            case 49 -> whoClicked.closeInventory();
            case 50 -> {
                if (currentInv < newInvs.size() - 1) {
                    currentInv++;
                    whoClicked.openInventory(newInvs.get(currentInv));
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return ItemsGUI.itemInv.get(0);
    }
}
