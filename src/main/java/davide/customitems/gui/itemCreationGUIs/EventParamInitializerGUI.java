package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.lists.EventList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EventParamInitializerGUI extends GUI {
    public static Inventory inv;

    private final IGUI type;
    private final EventList event;

    public EventParamInitializerGUI(IGUI type, EventList event) {
        inv = Bukkit.createInventory(this, 27, "Set the event parameters");
        this.type = type;
        this.event = event;
        setInv(event);
    }

    public void setInv(EventList event) {
        super.setInv(inv);
        
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        super.onGUIClick(whoClicked, slot, clickedItem, clickType, inventory);

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
