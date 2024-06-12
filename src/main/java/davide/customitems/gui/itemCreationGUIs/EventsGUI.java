package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsGUI extends GUI {
    public static List<Inventory> eventsInv = new ArrayList<>();
    private static int currentInv = 0;

    private final IGUI type;
    private final int slot;
    private final Inventory inv;

    public EventsGUI(String searchPrompt, int slot, Inventory inv, IGUI type) {
        this.type = type;
        this.slot = slot;
        this.inv = inv;
        eventsInv.add(Bukkit.createInventory(this, 54, "Choose an Event"));
        setInv(searchPrompt);
    }

    private void setInv(String searchPrompt) {
        int j = 0, k = 0;
        for (int i = 9; i < 45; i++) {
            if (Events.values()[k].name().toLowerCase(Locale.ROOT).contains(searchPrompt.toLowerCase(Locale.ROOT))) {
                eventsInv.get(j).setItem(i, Events.values()[k].getItemStack());

                k++;

                if (k > Events.values().length - 1)
                    break;
            }

            if (i == 44) {
                eventsInv.add(Bukkit.createInventory(this, 54, "Items"));
                i = 8;
                j++;
            }
        }

        //Interaction menu
        int n = 0;
        for (Inventory inv : eventsInv) {
            for (int i = 0; i < 9; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());
            for (int i = 45; i < 54; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());

            inv.setItem(48, ItemList.backArrow.getItemStack());
            inv.setItem(49, ItemList.closeBarrier.getItemStack());

            if (n != eventsInv.size() - 1)
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
            ((AbilityCreationGUI) type).setEvent(Events.valueOf(clickedItem.getItemMeta().getDisplayName()));
            AbilityCreationGUI.inv.setItem(21, new UtilsBuilder(new ItemStack(Material.ENDER_EYE), "§aEvents", false).lore("§f" + clickedItem.getItemMeta().getDisplayName()).build().getItemStack());
            whoClicked.openInventory(AbilityCreationGUI.inv);
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(eventsInv.get(currentInv));
                }
            }
            case 48 -> whoClicked.openInventory(AbilityCreationGUI.inv);
            case 49 -> whoClicked.closeInventory();
            case 53 -> {
                if (currentInv < eventsInv.size() - 1) {
                    currentInv++;
                    whoClicked.openInventory(eventsInv.get(currentInv));
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return eventsInv.get(0);
    }
}
