package davide.customitems.gui.itemCreation;

import davide.customitems.CustomItems;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.builders.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StatsGUI implements IGUI {
    public static Inventory inv;

    public StatsGUI() {
        inv = Bukkit.createInventory(this, 36, "Stats");
        setInv();
    }

    private void setInv() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        inv.setItem(11, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§cDamage", false).lore("§e" + ItemCreationGUI.damage).build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§6Critical Chance", false).lore("§e" + ItemCreationGUI.critChance).build().getItemStack());
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§eCritical Damage", false).lore("§e" + ItemCreationGUI.critChance).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§e" + ItemCreationGUI.health).build().getItemStack());
        inv.setItem(15, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§e" + ItemCreationGUI.health).build().getItemStack());

        inv.setItem(27, ItemList.backArrow.getItemStack());
        inv.setItem(31, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 11, 12, 13, 14, 15 -> signReadStat(whoClicked, slot);
            case 27 -> whoClicked.openInventory(ItemCreationGUI.inv);
            case 31 -> whoClicked.closeInventory();
        }
    }

    private void signReadStat(Player whoClicked, int slot) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Damage"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    int stat;

                    try {
                        stat = Integer.parseInt(strings[0]);
                    } catch (NumberFormatException e) {
                        whoClicked.sendMessage("§cInsert a number");
                        return false;
                    }

                    switch (slot) {
                        case 11 -> {
                            ItemCreationGUI.damage = stat;
                            inv.setItem(11, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§cDamage", false).lore("§e" + stat).build().getItemStack());
                        }
                        case 12 -> {
                            ItemCreationGUI.critChance = stat;
                            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§cCritical Chance", false).lore("§e" + stat).build().getItemStack());
                        }
                        case 13 -> {
                            ItemCreationGUI.critDamage = stat;
                            inv.setItem(13, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§eCritical Damage", false).lore("§e" + stat).build().getItemStack());
                        }
                        case 14 -> {
                            ItemCreationGUI.health = stat;
                            inv.setItem(14, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§e" + stat).build().getItemStack());
                        }
                        case 15 -> {
                            ItemCreationGUI.defence = stat;
                            inv.setItem(15, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§e" + stat).build().getItemStack());
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
