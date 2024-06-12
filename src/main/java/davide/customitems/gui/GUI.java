package davide.customitems.gui;

import davide.customitems.events.GUIEvents;
import davide.customitems.lists.ItemList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUI implements IGUI {
    public void setInv(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        inv.setItem(inv.getSize() - 9, ItemList.backArrow.getItemStack());
        inv.setItem(inv.getSize() - 5, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (slot == inventory.getSize() - 5) {
            whoClicked.closeInventory();
        } else if (slot == inventory.getSize() - 9) {
            whoClicked.openInventory(GUIEvents.getLastInv());
        }
    }

    @Override
    public IGUI getGUIType() {
        return this;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
