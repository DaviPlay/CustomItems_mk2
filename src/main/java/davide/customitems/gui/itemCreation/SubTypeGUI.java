package davide.customitems.gui.itemCreation;

import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.SubType;
import davide.customitems.itemCreation.Type;
import davide.customitems.itemCreation.builders.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubTypeGUI implements IGUI {
    public static Inventory inv;

    public SubTypeGUI(Type type) {
        inv = Bukkit.createInventory(this, 36, "Item Type");
        setInv(type);
    }

    private void setInv(Type type) {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        List<SubType> subs = Type.getSubTypes(type);

        for (int i = 10; i < subs.size() + 11; i++) {

            if (i == subs.size() + 10)
                inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), subs.get(0).getType().name(), false).build().getItemStack());
            else {
                SubType sub = subs.get(i - 10);
                if (sub.name().equals(ItemCreationGUI.subType.name()))
                    ItemCreationGUI.subType = sub;
                inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), sub.name(), false).build().getItemStack());
            }
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

        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE) {
            for (SubType s : SubType.values())
                if (s.name().equals(clickedItem.getItemMeta().getDisplayName())) {
                    ItemCreationGUI.subType = s;
                    whoClicked.openInventory(ItemCreationGUI.inv);
                    break;
                } else {
                    ItemCreationGUI.type = s.getType();
                    whoClicked.openInventory(ItemCreationGUI.inv);
                    break;
                }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
