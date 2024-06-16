package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.Utils;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CraftingEnchantsGUI extends GUI {
    public static final List<Inventory> invs = new ArrayList<>();
    private final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    private final IGUI type;
    private final int lvl;

    public CraftingEnchantsGUI(String searchPrompt, int lvl, IGUI type) {
        this.type = type;
        this.lvl = lvl;
        invs.clear();
        invs.add(Bukkit.createInventory(this, 54, "Enchantments"));
        setInv(searchPrompt);
    }

    public void setInv(String searchPrompt) {
        searchPrompt = searchPrompt.toLowerCase(Locale.ROOT).replace(" ", "_");

        int i = 0, j = 9;
        for (int k = 0; k < Enchantment.values().length; k++) {
            if (Enchantment.values()[k].getKey().getKey().contains(searchPrompt)) {
                String enchName = Enchantment.values()[k].getKey().getKey().replace("_", " ");
                enchName = enchName.substring(0, 1).toUpperCase(Locale.ROOT) + enchName.substring(1);
                if (enchName.contains("sw"))
                    enchName = "Sweeping edge";

                invs.get(i).setItem(j, new UtilsBuilder(new ItemStack(Material.ENCHANTED_BOOK), enchName, false).build().getItemStack());
                j++;
            }

            if (j == 45) {
                invs.add(Bukkit.createInventory(this, 54, "Enchantments"));
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
            ItemMeta clickedMeta = clickedItem.getItemMeta();
            if (clickedMeta == null) return;
            String key = clickedMeta.getDisplayName().toLowerCase(Locale.ROOT).replace(" ", "_");
            ((ItemCreationGUI) type).getEnchantments().put(Enchantment.getByKey(NamespacedKey.minecraft(key)), lvl);

            List<String> enchs = new ArrayList<>();
            for (Map.Entry<Enchantment, Integer> e : ((ItemCreationGUI) type).getEnchantments().entrySet())
                enchs.add(e.getKey().getKey().getKey().replace("_", " ") + " " + e.getValue().toString());


            type.getInventory().setItem(23, new UtilsBuilder(new ItemStack(Material.ENCHANTED_BOOK), "§aEnchantments", false)
                    .lore("§eLeft click to add an enchantment", "§eRight click to remove the last one", "§cFirst line is the name, the second is it's level", "", "§f" + enchs).build().getItemStack());

            whoClicked.openInventory(ItemCreationGUI.inv);
            return;
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(invs.get(currentInv));
                }
            }
            case 49 -> whoClicked.openInventory(ItemCreationGUI.inv);
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
        return invs.get(0);
    }
}
