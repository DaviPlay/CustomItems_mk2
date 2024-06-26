package davide.customitems.events;

import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.IGUI;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GUIEvents implements Listener, CommandExecutor, TabCompleter {
    private static final List<String> arguments = new ArrayList<>();

    private static final List<Inventory> lastInteracted = new ArrayList<>();

    public GUIEvents() {
        for (List<Item> items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    public GUIEvents(String key) {
        arguments.add(key);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getInventory().getHolder() instanceof IGUI gui) {
            e.setCancelled(true);

            if (lastInteracted.size() == 10)
                lastInteracted.remove(0);
            try {
                if (e.getInventory() != lastInteracted.get(lastInteracted.size() - 1))
                    lastInteracted.add(e.getInventory());
            } catch (IndexOutOfBoundsException ignored) {
                lastInteracted.add(e.getInventory());
            }

            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem(), e.getClick(), e.getInventory());
        }
    }

    public static Inventory getLastInv() {
        lastInteracted.remove(lastInteracted.size() - 1);
        Inventory inv = lastInteracted.get(lastInteracted.size() - 1);
        lastInteracted.remove(lastInteracted.size() - 1);

        return inv;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (player.hasPermission("customitems.gui")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? arguments.stream().filter(item -> item.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()) : arguments;
    }

    public static List<String> getArguments() {
        return arguments;
    }
}
