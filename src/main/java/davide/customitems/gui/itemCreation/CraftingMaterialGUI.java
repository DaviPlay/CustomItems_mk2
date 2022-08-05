package davide.customitems.gui.itemCreation;

import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.builders.UtilsBuilder;
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

public class CraftingMaterialGUI implements IGUI {
    public static final List<Inventory> invs = new ArrayList<>();

    private final boolean isCraftingMat;
    private final int slot;

    public CraftingMaterialGUI(String searchPrompt, int slot, boolean isCraftingMat) {
        this.isCraftingMat = isCraftingMat;
        this.slot = slot;
        invs.clear();
        invs.add(Bukkit.createInventory(this, 54, "Crafting Materials"));
        setInvs(searchPrompt);
    }

    public void setInvs(String searchPrompt) {
        int i = 0, j = 9;
        for (int k = 0; k < Material.values().length; k++) {
            if (Material.values()[k].name().contains(searchPrompt.toUpperCase(Locale.ROOT)) && Material.values()[k].getMaxStackSize() >= 32) {
                invs.get(i).setItem(j, new ItemStack(Material.values()[k]));
                j++;
            }

            if (j == 45) {
                invs.add(Bukkit.createInventory(this, 54, "Crafting Materials"));
                j = 8;
                i++;
            }
        }

        int n = 0;
        for (Inventory inv : invs) {
            for (int k = 0; k < 9; k++)
                inv.setItem(k, ItemList.fillerGlass.getItemStack());
            for (int k = 45; k < 54; k++)
                inv.setItem(k, ItemList.fillerGlass.getItemStack());

            inv.setItem(49, ItemList.closeBarrier.getItemStack());

            if (n != invs.size() - 1)
                inv.setItem(53, ItemList.nextArrow.getItemStack());
            if (n != 0)
                inv.setItem(45,ItemList.backArrow.getItemStack());
            n++;
        }
    }

    int currentInv = 0;
    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem == null) return;

        if (!Item.isCustomItem(clickedItem)) {
            if (isCraftingMat) {
                CraftingRecipeGUI.inv.setItem(this.slot, clickedItem);
                whoClicked.openInventory(CraftingRecipeGUI.inv);
            }
            else {
                MaterialCreationGUI.itemStack = new ItemStack(clickedItem);
                MaterialCreationGUI.inv.setItem(10, new UtilsBuilder(clickedItem, "§aItemStack", false).build().getItemStack());
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }

            return;
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(invs.get(currentInv));
                }
            }
            case 49 -> whoClicked.openInventory(MaterialCreationGUI.inv);
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
        return null;
    }
}
