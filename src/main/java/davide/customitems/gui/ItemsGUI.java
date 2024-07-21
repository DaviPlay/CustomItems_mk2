package davide.customitems.gui;

import davide.customitems.CustomItems;
import davide.customitems.api.Utils;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemsGUI extends GUI implements CommandExecutor {
    public static List<Inventory> itemInv = new ArrayList<>();
    private static int currentInv = 0;
    protected static boolean showAddInfo = false;

    private final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);
    public ItemsGUI() {
        itemInv.add(Bukkit.createInventory(this, 54, "Items"));
        setInv();
    }

    public ItemsGUI(Player player) {
        currentInv = 0;
        for (Inventory inv : itemInv)
            for (ItemStack is : inv)
                if (is != null && Item.isCustomItem(is) && Item.toItem(is).hasAddInfo())
                    if (showAddInfo && !Item.getLore(is).contains(Item.toItem(is).getAddInfo().getFirst())) {
                        Item.addAddInfoToLore(is);
                    } else if (!showAddInfo && Item.getLore(is).contains(Item.toItem(is).getAddInfo().getFirst())) {
                        Item.removeAddInfoFromLore(is);
                    }

        player.openInventory(ItemsGUI.itemInv.getFirst());
    }

    private void setInv() {
        List<Item> items = ItemList.items.getFirst();

        int j = 0, k = 0;
        for (int i = 9; i < 45; i++) {
            Item item = items.get(k);
            if ((plugin.getConfig().get(item.getKey().getKey()) != null && !plugin.getConfig().getBoolean(item.getKey().getKey())) && plugin.getUserItemsConfig().get("items." + item.getKey().getKey().toUpperCase(Locale.ROOT)) == null) {
                if (k > items.size() - 1)
                    break;
                else
                    k++;

                if (i > 9)
                    i--;
                else
                    i = 9;

                continue;
            }

            if (item.isShowInGui()) {
                ItemStack is = item.getItemStack(1);
                if (showAddInfo)
                    Item.addAddInfoToLore(is);
                itemInv.get(j).setItem(i, is);
            } else {
                if (k > items.size() - 1)
                    break;
                else
                    k++;

                if (i > 9)
                    i--;
                else
                    i = 9;

                continue;
            }

            k++;

            if (k > items.size() - 1)
                break;

            if (i == 44) {
                itemInv.add(Bukkit.createInventory(this, 54, "Items"));
                i = 8;
                j++;
            }
        }

        //Interaction menu
        int n = 0;
        for (Inventory inv : itemInv) {
            for (int i = 0; i < 9; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());
            for (int i = 45; i < 54; i++)
                inv.setItem(i, ItemList.fillerGlass.getItemStack());

            inv.setItem(48, ItemList.itemSword.getItemStack());
            inv.setItem(49, ItemList.closeBarrier.getItemStack());
            inv.setItem(50, ItemList.showAddInfo.getItemStack());

            if (n != itemInv.size() - 1)
                inv.setItem(53, ItemList.nextArrow.getItemStack());
            if (n != 0)
                inv.setItem(45,ItemList.backArrow.getItemStack());
            n++;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        new ItemsGUI(player);

        return false;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        if (clickedItem == null) return;
        ItemMeta meta = clickedItem.getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();
        Item item = Item.toItem(clickedItem);

        if (slot > 8 && slot < 45) {
            if (item != null && !ItemList.utilsItems.contains(item)) {
                if (container != null)
                    if (container.getKeys().contains(item.getKey()))
                        if (whoClicked.getGameMode() == GameMode.CREATIVE) {
                            if (clickType.isLeftClick()) {
                                ItemStack i = clickedItem.clone();

                                if (item.hasRandomUUID()) Item.setRandomUUID(i);
                                if (showAddInfo) Item.removeAddInfoFromLore(i);
                                Utils.addToInventory(whoClicked, i);
                            } else if (clickType.isRightClick()) {
                                if (CraftingInventories.getInv(item.getKey()) != null)
                                    whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                            }
                        } else {
                            if (CraftingInventories.getInv(item.getKey()) != null)
                                whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                        }
            }
        }

        switch (slot) {
            case 45 -> {
                if (currentInv > 0) {
                    currentInv--;
                    whoClicked.openInventory(itemInv.get(currentInv));
                }
            }
            case 48 -> new MatsGUI(whoClicked);
            case 49 -> whoClicked.closeInventory();
            case 50 -> {
                showAddInfo = !showAddInfo;
                MatsGUI.showAddInfo = showAddInfo;

                if (showAddInfo) {
                    for (Inventory inv : itemInv)
                        Item.setLore(inv.getItem(50), "", "§aShown!");

                    for (Inventory inv : MatsGUI.itemInv)
                        Item.setLore(inv.getItem(50), "", "§aShown!");
                } else {
                    for (Inventory inv : itemInv)
                        Item.setLore(inv.getItem(50), "", "§cHidden!");

                    for (Inventory inv : MatsGUI.itemInv)
                        Item.setLore(inv.getItem(50), "", "§cHidden!");
                }

                for (Inventory inv : itemInv)
                    for (ItemStack is : inv)
                        if (is != null && Item.isCustomItem(is))
                            if (showAddInfo)
                                Item.addAddInfoToLore(is);
                            else
                                Item.removeAddInfoFromLore(is);
            }
            case 53 -> {
                if (currentInv < itemInv.size() - 1) {
                    currentInv++;
                    whoClicked.openInventory(itemInv.get(currentInv));
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return itemInv.getFirst();
    }
}
