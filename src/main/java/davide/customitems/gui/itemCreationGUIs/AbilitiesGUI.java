package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AbilitiesGUI extends GUI {
    public static Inventory inv;
    private final IGUI type;

    public AbilitiesGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 54, "Abilities");
        this.type = type;
        setInv();
    }

    private void setInv() {
        super.setInv(inv);

        for (int i = 10; i < 35; i++) {
            inv.setItem(i, ItemList.abilitiesGlass.getItemStack());

            if (i == 16 || i == 25) i += 2;
        }

        inv.setItem(40, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§aAdd a new ability").build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem.getType() == Material.YELLOW_DYE && clickedItem.getItemMeta().getDisplayName().startsWith("§6")) {
            short k = 0;
            if (slot > 25) k = 4;
            else if (slot > 16) k = 2;

            if (clickType == ClickType.RIGHT) {
                inventory.setItem(slot, ItemList.abilitiesGlass.getItemStack());

                for (int i = 10; i < 35; i++) {
                    if (i == 16 || i == 25) i += 2;
                    if (inventory.getItem(i).equals(ItemList.abilitiesGlass.getItemStack()) && inventory.getItem(i + 1).getType() == Material.YELLOW_DYE) {
                        inventory.setItem(i, inventory.getItem(i + 1).clone());
                        inventory.setItem(i + 1, ItemList.abilitiesGlass.getItemStack());
                    }
                }
                ((ItemCreationGUI) type).getAbilities().remove(slot - (10 - k));
            }
            else if (clickType.isLeftClick()) {
                Ability a = ((ItemCreationGUI) type).getAbilities().get(k);
                whoClicked.openInventory(new AbilityCreationGUI(a.name(), a.type(), a.cooldown(), Arrays.asList(a.description()), a.event(), slot, k, this).getInventory());
            }
        }

        switch (slot) {
            case 40 -> whoClicked.openInventory(new AbilityCreationGUI(this).getInventory());
            case 45 -> whoClicked.openInventory(ItemCreationGUI.inv);
            case 49 -> whoClicked.closeInventory();
        }
    }

    public IGUI getType() {
        return type;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
