package davide.customitems.events;

import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.IGUI;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
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
        if (e.getInventory().getHolder() instanceof IGUI) {
            e.setCancelled(true);
            IGUI gui = (IGUI) e.getInventory().getHolder();
            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getClick(), e.getInventory());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        Item item = Item.toItem(args[0]);
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? arguments.stream().filter(item -> item.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()) : arguments;
    }

    public static List<String> getArguments() {
        return arguments;
    }
}
