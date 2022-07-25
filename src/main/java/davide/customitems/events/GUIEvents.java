package davide.customitems.events;

import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.GameMode;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GUIEvents implements Listener, CommandExecutor, TabCompleter {
    List<String> arguments = new ArrayList<>();

    public GUIEvents() {
        for (Item[] items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @EventHandler
    private void openInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory topInv = player.getOpenInventory().getTopInventory();
        Inventory clickedInv = e.getClickedInventory();
        List<Boolean> bools = new ArrayList<>();

        for (Item[] items : ItemList.items)
            for (Item item : items)
                if (topInv.equals(ItemsGUI.itemInv) || topInv.equals(ItemsGUI.materialInv) || topInv.equals(CraftingInventories.getInv(item.getKey())))
                    bools.add(topInv.equals(ItemsGUI.itemInv) || topInv.equals(ItemsGUI.materialInv) || topInv.equals(CraftingInventories.getInv(item.getKey())));

        if (!bools.contains(true)) return;

        if (clickedInv == null) return;
        if (e.getCurrentItem() == null) return;

        ItemStack currentItem = e.getCurrentItem();
        ItemMeta meta = currentItem.getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();
        Item item = Item.toItem(currentItem);

        if (item != null) {
            if (container != null)
                if (container.getKeys().contains(item.getKey()))
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        if (e.getClick().isLeftClick()) {
                            if (item.hasRandomUUID())
                                Item.setRandomUUID(currentItem);

                            player.getInventory().addItem(currentItem);
                        } else if (e.getClick().isRightClick()) {
                            if (CraftingInventories.getInv(item.getKey()) != null)
                                player.openInventory(CraftingInventories.getInv(item.getKey()));
                        }
                    } else {
                        if (CraftingInventories.getInv(item.getKey()) != null)
                            player.openInventory(CraftingInventories.getInv(item.getKey()));
                    }
        }

        e.setCancelled(true);
    }

    @EventHandler
    private void menuArrow(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        ItemStack itemArrow = ItemList.itemArrow.getItemStack();
        ItemStack matsArrow = ItemList.matsArrow.getItemStack();
        Player player = (Player) e.getWhoClicked();

        if (item.equals(itemArrow))
            player.openInventory(ItemsGUI.itemInv);
        else if (item.equals(matsArrow))
            player.openInventory(ItemsGUI.materialInv);
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
}
