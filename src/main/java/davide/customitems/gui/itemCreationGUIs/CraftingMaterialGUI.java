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

public class CraftingMaterialGUI extends GUI {
    public static final List<Inventory> invs = new ArrayList<>();

    private final IGUI type;
    private final int slot;
    private final Inventory inv;

    public CraftingMaterialGUI(String searchPrompt, boolean isStack, int slot, Inventory inv, IGUI type) {
        this.type = type;
        this.slot = slot;
        this.inv = inv;
        invs.clear();
        invs.add(Bukkit.createInventory(this, 54, "Materials"));
        setInv(searchPrompt, isStack);
    }

    public void setInv(String searchPrompt, boolean isStack) {
        int i = 0, j = 9;
        if (!(type instanceof MobDropChanceGUI) && !isStack)
            for (List<Item> items : ItemList.items)
                for (Item item : items) {
                    if (Item.getName(item.getItemStack(1)).toLowerCase(Locale.ROOT).contains(searchPrompt.toLowerCase(Locale.ROOT))) {
                        invs.get(i).setItem(j, new ItemStack(item.getItemStack()));
                        j++;
                    }

                    if (j == 45) {
                        invs.add(Bukkit.createInventory(this, 54, "Materials"));
                        j = 8;
                        i++;
                    }
                }

        for (int k = 0; k < Material.values().length; k++) {
            if (type instanceof MobDropChanceGUI) {
                if (Material.values()[k].name().contains(searchPrompt.toUpperCase(Locale.ROOT)) && Material.values()[k].isBlock()) {
                    invs.get(i).setItem(j, new ItemStack(Material.values()[k]));
                    j++;
                }
            } else {
                if (Material.values()[k].name().contains(searchPrompt.toUpperCase(Locale.ROOT))) {
                    invs.get(i).setItem(j, new ItemStack(Material.values()[k]));
                    j++;
                }
            }

            if (j == 45) {
                invs.add(Bukkit.createInventory(this, 54, "Materials"));
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
        if (slot > 8 && slot < 45) {
            if (type instanceof MaterialCreationGUI m) {
                m.setItemStack(new ItemStack(clickedItem));
                MaterialCreationGUI.inv.setItem(10, new UtilsBuilder(clickedItem, "§aItemStack", false).build().getItemStack());
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
            else if (type instanceof ItemCreationGUI i) {
                i.setItemStack(new ItemStack(clickedItem));
                ItemCreationGUI.inv.setItem(10, new UtilsBuilder(clickedItem, "§aItemStack", false).build().getItemStack());
                whoClicked.openInventory(ItemCreationGUI.inv);
            }
            else if (type instanceof MobDropChanceGUI m) {
                m.getBlocks().add(clickedItem.getType());
                inv.setItem(30, new UtilsBuilder(new ItemStack(Material.STONE), "§aAdd a block", false).lore("§f" + m.getBlocks()).build().getItemStack());
                whoClicked.openInventory(MobDropChanceGUI.inv);
            } else {
                inv.setItem(this.slot, clickedItem);
                whoClicked.openInventory(inv);
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
            case 49 -> {
                if (type instanceof MaterialCreationGUI)
                    whoClicked.openInventory(MaterialCreationGUI.inv);
                else
                    whoClicked.openInventory(ItemCreationGUI.inv);
            }
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
        return inv;
    }
}
