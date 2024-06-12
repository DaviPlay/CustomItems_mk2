package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
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

public class TypeGUI extends GUI {
    public static Inventory inv;

    private final IGUI igui;

    public TypeGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Item Type");
        this.igui = type;
        setInv();
    }

    private void setInv() {
        super.setInv(inv);

        int i = 10;
        for (Type type : Type.values()) {
            if (type == Type.MATERIAL) {
                i--;
                continue;
            }
            if (type.equals(Type.ITEM))
                inv.setItem(i, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + type.name(), false).isGlint(true).build().getItemStack());
            else
                inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + type.name(), false).build().getItemStack());
            i++;
        }
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);

        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE || clickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE)
            for (Type t : Type.values()) {
                if (t.name().equals(clickedItem.getItemMeta().getDisplayName().substring(2))) {
                    for (ItemStack i : inv.getContents())
                        if (i.getType() == Material.GREEN_STAINED_GLASS_PANE)
                            inv.setItem(inv.first(i), new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + i.getItemMeta().getDisplayName().substring(2), false).isGlint(false).build().getItemStack());

                    inv.setItem(slot, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + clickedItem.getItemMeta().getDisplayName().substring(2), false).isGlint(true).build().getItemStack());
                    ((ItemCreationGUI) igui).setSubType(null);
                    ((ItemCreationGUI) igui).setType(t);
                    if (t == Type.ITEM) {
                        whoClicked.openInventory(ItemCreationGUI.inv);
                        break;
                    }
                    new SubTypeGUI(t, igui);
                    whoClicked.openInventory(SubTypeGUI.inv);
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
