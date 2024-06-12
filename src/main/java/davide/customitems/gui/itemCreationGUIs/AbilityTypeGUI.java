package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.*;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AbilityTypeGUI extends GUI {
    public static Inventory inv;

    private final IGUI type;

    public AbilityTypeGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Ability Type");
        this.type = type;
        setInv();
    }

    public void setInv() {
        super.setInv(inv);

        int i = 10;
        for (AbilityType type : AbilityType.values()) {
            if (i == 17) i += 2;

            AbilityType at = ((AbilityCreationGUI) this.type).getAbilityType();
            if (type == AbilityType.PASSIVE) {
                if (at != null && at == type)
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + type.name(), false).isGlint(true).build().getItemStack());
                else
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + type.name(), false).build().getItemStack());
            } else {
                if (at != null && at == type)
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + type.getPrefix(), false).isGlint(true).build().getItemStack());
                else
                    inv.setItem(i, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + type.getPrefix(), false).build().getItemStack());
            }
            i++;
        }

        inv.setItem(31, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE || clickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE)
            for (AbilityType at : AbilityType.values()) {
                if (at == AbilityType.PASSIVE) {
                    if (!(at.name().equals(clickedItem.getItemMeta().getDisplayName().substring(2)))) continue;
                } else {
                    if (!(at.getPrefix().equals(clickedItem.getItemMeta().getDisplayName().substring(2)))) continue;
                }

                for (ItemStack i : inv.getContents())
                    if (i.getType() == Material.GREEN_STAINED_GLASS_PANE)
                        inv.setItem(inv.first(i), new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§c" + i.getItemMeta().getDisplayName().substring(2), false).isGlint(false).build().getItemStack());

                inv.setItem(slot, new UtilsBuilder(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "§a" + clickedItem.getItemMeta().getDisplayName().substring(2), false).isGlint(true).build().getItemStack());
                ((AbilityCreationGUI) type).setAbilityType(at);
                whoClicked.openInventory(AbilityCreationGUI.inv);
                break;
            }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
