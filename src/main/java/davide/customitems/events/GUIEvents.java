package davide.customitems.events;

import davide.customitems.api.Utils;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.GameMode;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GUIEvents implements Listener, CommandExecutor, TabCompleter {
    List<String> arguments = new ArrayList<>();

    public GUIEvents() {
        for (List<Item> items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @EventHandler
    private void openInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory topInv = player.getOpenInventory().getTopInventory();
        Inventory clickedInv = e.getClickedInventory();
        List<Boolean> bools = new ArrayList<>();

        for (List<Item> items : ItemList.items)
            for (Item item : items)
                if (topInv.equals(CraftingInventories.getInv(item.getKey())))
                    bools.add(topInv.equals(CraftingInventories.getInv(item.getKey())));

        for (Inventory inv : ItemsGUI.itemInv)
            if (topInv.equals(inv))
                bools.add(topInv.equals(inv));

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

                            Utils.addToInventory(player, currentItem);
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
        Player player = (Player) e.getWhoClicked();
        ItemStack nextArrow = ItemList.nextArrow.getItemStack();
        ItemStack prevArrow = ItemList.backArrow.getItemStack();

        int currentInv = 0;

        if (item.equals(nextArrow)) {
            currentInv++;
            player.openInventory(ItemsGUI.itemInv.get(currentInv));
        }
        else if (item.equals(prevArrow)) {
            currentInv--;
            if (currentInv <= -1) {
                for (Inventory inv : ItemsGUI.itemInv)
                    if (e.getClickedInventory().equals(inv))
                        player.openInventory(ItemsGUI.itemInv.get(0));

                if (e.getClickedInventory() != null)
                    player.openInventory(ItemsGUI.itemInv.get(findItemInv(e.getClickedInventory())));

            } else
                player.openInventory(ItemsGUI.itemInv.get(currentInv));
        }
    }

    private int findItemInv(Inventory inv) {
        NamespacedKey key = null;

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getItemMeta() != null)
                if (Item.isCustomItem(item) && inv.getItem(25) != null && inv.getItem(25).equals(item)) {
                    key = Item.toItem(item).getKey();
                    break;
                }
        }

        int k = 0;
        for (Inventory i : ItemsGUI.itemInv) {
            for (ItemStack item : i.getContents())
                if (item != null && Item.isCustomItem(item) && Item.toItem(item).getKey().equals(key))
                    return k;
            k++;
        }

        return 0;
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
