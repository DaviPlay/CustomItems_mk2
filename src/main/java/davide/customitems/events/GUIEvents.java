package davide.customitems.events;

import davide.customitems.CustomItems;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.IGUI;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.gui.itemCreation.CraftingMaterialGUI;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GUIEvents implements Listener, CommandExecutor, TabCompleter {
    private static final List<String> arguments = new ArrayList<>();

    public GUIEvents() {
        for (List<Item> items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getInventory().getHolder() instanceof IGUI gui) {
            e.setCancelled(true);
            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getClick(), e.getInventory());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        Item item = Item.toItem(args[0]);
        if (item == null) {
            player.sendMessage("§cThat item doesn't exist!");
            return true;
        }
        Inventory inv = CraftingInventories.getInv(item.getKey());

        if (cmd.getName().equalsIgnoreCase("viewRecipe")) {
            if (inv == null) {
                player.sendMessage("§cThat item doesn't exist!");
                return true;
            }

            player.openInventory(CraftingInventories.getInv(item.getKey()));
        }

        return false;
    }

    public static void signReadCraftingMat(Player whoClicked, int slot, boolean isCraftingMat, Inventory inv) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Crafting Material"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), slot, isCraftingMat, inv);
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(CraftingMaterialGUI.invs.get(0)), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    public static void signReadAmount(Player whoClicked, ItemStack clickedItem, int slot, Inventory inv) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Amount of", "items required"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    try {
                        if (Integer.parseInt(strings[0]) < 1 || Integer.parseInt(strings[0]) > clickedItem.getMaxStackSize()) {
                            player.sendMessage("§cThe amount of items required can't be less then 1 or more then it's max stack size");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInsert a number");
                        return false;
                    }

                    clickedItem.setAmount(Integer.parseInt(strings[0]));
                    inv.setItem(slot, clickedItem);
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? arguments.stream().filter(item -> item.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()) : arguments;
    }

    public static List<String> getArguments() {
        return arguments;
    }
}
