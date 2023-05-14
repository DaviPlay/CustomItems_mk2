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
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§e" + ItemCreationGUI.health).build().getItemStack());
        inv.setItem(15, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§6Critical Chance", false).lore("§e" + ItemCreationGUI.critChance).build().getItemStack());

        inv.setItem(27, ItemList.backArrow.getItemStack());
        inv.setItem(31, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 11 -> signReadDamage(whoClicked);
            case 13 -> signReadHealth(whoClicked);
            case 15 -> signReadCritChance(whoClicked);
            case 27 -> whoClicked.openInventory(ItemCreationGUI.inv);
            case 31 -> whoClicked.closeInventory();
        }
    }

    private void signReadDamage(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Damage"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    int damage;

                    try {
                        damage = Integer.parseInt(strings[0]);
                    } catch (NumberFormatException e) {
                        whoClicked.sendMessage("§cInsert a number");
                        return false;
                    }

                    ItemCreationGUI.damage = damage;
                    inv.setItem(11, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§cDamage", false).lore("§e" + damage).build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);

                    return true;
                }));

        menu.open(whoClicked);
    }

    private void signReadHealth(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Health"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    int health;

                    try {
                        health = Integer.parseInt(strings[0]);
                    } catch (NumberFormatException e) {
                        whoClicked.sendMessage("§cInsert a number");
                        return false;
                    }

                    ItemCreationGUI.health = health;
                    inv.setItem(13, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§e" + health).build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);

                    return true;
                }));

        menu.open(whoClicked);
    }

    private void signReadCritChance(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Critical Chance"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    int critChance;

                    try {
                        critChance = Integer.parseInt(strings[0]);
                    } catch (NumberFormatException e) {
                        whoClicked.sendMessage("§cInsert a number");
                        return false;
                    }

                    ItemCreationGUI.critChance = critChance;
                    inv.setItem(15, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§6CritChance", false).lore("§e" + critChance).build().getItemStack());
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
