package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CookingTimeXpGUI extends GUI {
    public static Inventory inv;

    private final IGUI type;

    public CookingTimeXpGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Cooking Time & Exp");
        this.type = type;
        setInv();
    }

    private void setInv() {
        super.setInv(inv);

        if (type instanceof MaterialCreationGUI m) {
            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore("§f" + m.getCookingTime()).build().getItemStack());
            inv.setItem(14, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Experience", false).lore("§f" + m.getCookingExp()).build().getItemStack());
        } else if (type instanceof ItemCreationGUI i) {
            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore("§f" + i.getCookingTime()).build().getItemStack());
            inv.setItem(14, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Experience", false).lore("§f" + i.getCookingExp()).build().getItemStack());
        }
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 12, 14 -> signReadCookingTime(whoClicked, slot, type);
        }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    private void signReadCookingTime(Player whoClicked, int slot, IGUI type) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Cooking Time or Experience", "In Seconds"))
                .reopenIfFail(false)
                .response(((player, strings) -> {
                    try {
                        if (Integer.parseInt(strings[0]) <= 0) {
                            player.sendMessage("§cCan't input a number less or equal to 0");
                            Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInsert an integer");
                        Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                        return false;
                    }

                    int i = Integer.parseInt(strings[0]) * 20;

                    switch (slot) {
                        case 12 -> {
                            if (type instanceof MaterialCreationGUI m)
                                m.setCookingTime(i);
                            else if (type instanceof ItemCreationGUI ic)
                                ic.setCookingTime(i);

                            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore("§f" + i / 20 + "s").build().getItemStack());
                        }
                        case 14 -> {
                            if (type instanceof MaterialCreationGUI m)
                                m.setCookingExp(i);
                            else if (type instanceof ItemCreationGUI ic)
                                ic.setCookingExp(i);

                            inv.setItem(14, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Experience", false).lore("§f" + i / 20 + "s").build().getItemStack());
                        }
                    }

                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
