package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.SubType;
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

import java.util.List;

public class SubTypeGUI extends GUI {
    public static Inventory inv;

    private final IGUI guiType;

    public SubTypeGUI(Type type, IGUI guiType) {
        inv = Bukkit.createInventory(this, 45, "Item SubType");
        this.guiType = guiType;
        setInv(type);
    }

    private void setInv(Type type) {
        super.setInv(inv);

        ItemCreationGUI ic = (ItemCreationGUI) guiType;
        List<SubType> subs = Type.getSubTypes(type);

        int k = 10;
        for (int i = 10; i < subs.size() + 11; i++) {
            if (i == 17)
                i = 19;

            if (k == subs.size() + 10)
                inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + subs.get(0).getType().name(), false).build().getItemStack());
            else {
                SubType sub = subs.get(i - 10);
                if (ic.getSubType() == sub)
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + sub.name(), false).isGlint(true).build().getItemStack());
                else
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + sub.name(), false).build().getItemStack());
            }
            k++;
        }
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);

        ItemCreationGUI gui = (ItemCreationGUI) guiType;

        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE || clickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE)
            for (SubType s : Type.getSubTypes(gui.getType()))
                if (s.name().equals(clickedItem.getItemMeta().getDisplayName().substring(2))) {
                    for (ItemStack i : inv.getContents())
                        if (i.getType() == Material.GREEN_STAINED_GLASS_PANE)
                            inv.setItem(inv.first(i), new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + clickedItem.getItemMeta().getDisplayName().substring(2), false).isGlint(false).build().getItemStack());

                    inv.setItem(slot, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + clickedItem.getItemMeta().getDisplayName().substring(2), false).build().getItemStack());
                    gui.setType(null);
                    gui.setSubType(s);
                    whoClicked.openInventory(ItemCreationGUI.inv);
                    break;
                } else if (s.getType().name().equals(clickedItem.getItemMeta().getDisplayName().substring(2))) {
                    for (ItemStack i : inv.getContents())
                        if (i.getType() == Material.GREEN_STAINED_GLASS_PANE)
                            inv.setItem(inv.first(i), new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + clickedItem.getItemMeta().getDisplayName().substring(2), false).isGlint(false).build().getItemStack());

                    inv.setItem(slot, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + clickedItem.getItemMeta().getDisplayName().substring(2), false).build().getItemStack());
                    whoClicked.openInventory(ItemCreationGUI.inv);
                    break;
                }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
