package davide.customitems.gui.itemCreation;

import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.builders.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreateItemGUI implements IGUI, CommandExecutor {
    public static Inventory inv;

    public CreateItemGUI() {
        inv = Bukkit.createInventory(this, 27, "Type");
        setInv();
    }

    private void setInv() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        inv.setItem(11, new UtilsBuilder(new ItemStack(Material.IRON_SWORD), "§6Item", false).build().getItemStack());
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aChoose", false).lore("§fChoose the type of item", "§fyou want to create").build().getItemStack());
        inv.setItem(15, new UtilsBuilder(new ItemStack(Material.DIAMOND), "§bMaterial", false).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 11 -> {
                new ItemCreationGUI();
                whoClicked.openInventory(ItemCreationGUI.inv);
            }
            case 15 -> {
                new MaterialCreationGUI();
                whoClicked.openInventory(MaterialCreationGUI.inv);
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (cmd.getName().equalsIgnoreCase("createItem"))
            player.openInventory(inv);

        return false;
    }
}