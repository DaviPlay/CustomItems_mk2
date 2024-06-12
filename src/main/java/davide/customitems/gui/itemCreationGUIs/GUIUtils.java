package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.DelayedTask;
import davide.customitems.api.Instruction;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.crafting.CraftingType;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GUIUtils {

    @NotNull
    public static Rarity rarityCycle(Rarity rarity, Inventory inv) {
        rarity = Rarity.values()[rarity.ordinal() + 1];
        if (rarity.ordinal() == Rarity.values().length - 1)
            rarity = Rarity.values()[0];

        inv.setItem(14, new UtilsBuilder(new ItemStack(rarity.getDye()), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());

        return rarity;
    }

    public static void signRead(List<String> menuName, Instruction instruction, Player whoClicked, Inventory toOpen) {
        if (menuName.size() > 4) throw new IllegalArgumentException("The menu name must have a size of 4 or lower");
        if (menuName.size() < 4)
            for (int i = menuName.size() - 1; i <= 4; i++)
                menuName.add("");

        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(menuName)
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    instruction.run(strings);
                    new DelayedTask(() -> whoClicked.openInventory(toOpen));
                    return true;
                }));

        menu.open(whoClicked);
    }

    public static void signRead(List<String> menuName, Instruction instruction, Player whoClicked, List<Inventory> toOpen) {
        if (menuName.size() > 4) throw new IllegalArgumentException("The menu name must have a size of 4 or lower");
        if (menuName.size() < 4)
            for (int i = menuName.size() - 1; i <= 4; i++)
                menuName.add("");

        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(menuName)
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    instruction.run(strings);
                    new DelayedTask(() -> whoClicked.openInventory(toOpen.get(0)));
                    return true;
                }));

        menu.open(whoClicked);
    }

    public static CraftingType craftingTypeSwitch(CraftingType type, Inventory inv) {
        CraftingType craftingType = type;

        switch (craftingType) {
            case SHAPELESS -> {
                type = CraftingType.SHAPED;
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.SMITHING_TABLE), "§aCrafting Type", false).lore("§fShaped").build().getItemStack());
            }
            case SHAPED -> {
                type = CraftingType.FURNACE;
                inv.setItem(16, new UtilsBuilder(new ItemStack(Material.FURNACE), "§aCrafting Recipe", false).build().getItemStack());
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FURNACE), "§aCrafting Type", false).lore("§fFurnace").build().getItemStack());
                inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time & Exp", false).build().getItemStack());
            }
            case FURNACE -> {
                type = CraftingType.DROP;
                inv.setItem(16, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCrafting Recipe", false).lore("§eChoose the shaped, shapeless or furnace", "§ecrafting type to change the recipe").build().getItemStack());
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.CREEPER_HEAD), "§aCrafting Type", false).lore("§fDrop").build().getItemStack());
                inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time & Exp", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
                inv.setItem(32, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§aMob & Drop Chance", false).build().getItemStack());
            }
            case DROP -> {
                type = CraftingType.SHAPELESS;
                inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).build().getItemStack());
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
                inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cMob & Drop Chance", false).lore("§eChoose the Drop crafting type to change", "§ethe mob and the drop chance").build().getItemStack());
            }
        }

        return type;
    }
}
