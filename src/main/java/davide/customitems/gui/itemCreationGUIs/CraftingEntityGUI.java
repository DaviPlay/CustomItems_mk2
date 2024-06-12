package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CraftingEntityGUI extends GUI {
    public static final List<Inventory> invs = new ArrayList<>();

    private final IGUI type;
    private final Inventory inv;

    public CraftingEntityGUI(String searchPrompt, Inventory inv, IGUI type) {
        this.type = type;
        this.inv = inv;
        invs.clear();
        invs.add(Bukkit.createInventory(this, 54, "Entities"));
        setInvs(searchPrompt);
    }

    public void setInvs(String searchPrompt) {
        int i = 0, j = 9;

        for (int k = 0; k < EntityType.values().length; k++) {
            if (EntityType.values()[k].name().contains(searchPrompt.toUpperCase(Locale.ROOT).replace(" ", "_").trim()) && EntityType.values()[k].isAlive()) {
                invs.get(i).setItem(j, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§f" + EntityType.values()[k].name(), false).build().getItemStack());
                j++;
            }

            if (j == 45) {
                invs.add(Bukkit.createInventory(this, 54, "Entities"));
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
            if (type instanceof MobDropChanceGUI m) {
                m.getEntities().add(EntityType.valueOf(clickedItem.getItemMeta().getDisplayName().substring(2)));
                inv.setItem(12, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§aAdd a mob", false).lore("§f" + m.getEntities()).build().getItemStack());
                whoClicked.openInventory(MobDropChanceGUI.inv);
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
        return inv;
    }
}
