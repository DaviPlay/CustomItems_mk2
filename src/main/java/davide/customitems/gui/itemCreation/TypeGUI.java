package davide.customitems.gui.itemCreation;

import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Type;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TypeGUI implements IGUI {
    public static Inventory inv;

    public TypeGUI() {
        inv = Bukkit.createInventory(this, 36, "Item Type");
        setInv();
    }

    private void setInv() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        for (int i = 10; i < Type.values().length + 10; i++) {
            Type type = Type.values()[i - 10];
            inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), type.name(), false).build().getItemStack());
        }

        inv.setItem(27, ItemList.backArrow.getItemStack());
        inv.setItem(31, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 27 -> whoClicked.openInventory(ItemCreationGUI.inv);
            case 31 -> whoClicked.closeInventory();
        }

        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE)
            for (Type t : Type.values())
                if (t.name().equals(clickedItem.getItemMeta().getDisplayName())) {
                    new SubTypeGUI(t);
                    whoClicked.openInventory(SubTypeGUI.inv);
                    break;
                }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
