package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.EventList;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsGUI extends GUI {
    public static List<Inventory> invs = new ArrayList<>();
    private static int currentInv = 0;

    private final IGUI type;

    public EventsGUI(String searchPrompt, IGUI type) {
        this.type = type;
        invs.clear();
        invs.add(Bukkit.createInventory(this, 54, "Choose an Event"));
        setInv(searchPrompt);
    }

    private void setInv(String searchPrompt) {
        int j = 0, k = 0;
        for (int i = 9; i < 45; i++) {
            if (EventList.values()[k].name().toLowerCase(Locale.ROOT).contains(searchPrompt.toLowerCase(Locale.ROOT))) {
                invs.get(j).setItem(i, EventList.values()[k].getItemStack());

                k++;

                if (k > EventList.values().length - 1)
                    break;
            }

            if (i == 44) {
                invs.add(Bukkit.createInventory(this, 54, "Items"));
                i = 8;
                j++;
            }
        }

        //Interaction menu
        int n = 0;
        for (Inventory inv : invs) {
            for (int i = 0; i < 9; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());
            for (int i = 45; i < 54; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());

            inv.setItem(48, ItemList.backArrow.getItemStack());
            inv.setItem(49, ItemList.closeBarrier.getItemStack());

            if (n != invs.size() - 1)
                inv.setItem(53, ItemList.nextArrow.getItemStack());
            if (n != 0)
                inv.setItem(45,ItemList.backArrow.getItemStack());
            n++;
        }
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem == null) return;

        if (slot > 8 && slot < 45) {
            EventList event = EventList.valueOf(clickedItem.getItemMeta().getDisplayName());

            ((AbilityCreationGUI) type).setEvent(event);
            AbilityCreationGUI.inv.setItem(21, new UtilsBuilder(clickedItem, "§aEvents", false).lore("§f" + clickedItem.getItemMeta().getDisplayName()).build().getItemStack());
            whoClicked.openInventory(AbilityCreationGUI.inv);
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(invs.get(currentInv));
                }
            }
            case 48 -> whoClicked.openInventory(AbilityCreationGUI.inv);
            case 49 -> whoClicked.closeInventory();
            case 53 -> {
                if (currentInv < invs.size() - 1) {
                    currentInv++;
                    whoClicked.openInventory(invs.get(currentInv));
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return invs.get(0);
    }
}
