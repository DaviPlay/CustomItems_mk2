package davide.customitems.gui.itemCreation;

import davide.customitems.CustomItems;
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

    public static void openRecipeGUIs(Player whoClicked, @NotNull CraftingType craftingType, List<ItemStack> recipe, IGUI type) {
        switch (craftingType) {
            case SHAPELESS -> {
                new ShapelessRecipeGUI(recipe, type);
                whoClicked.openInventory(ShapelessRecipeGUI.inv);
            }
            case SHAPED -> {
                new ShapedRecipeGUI(recipe, type);
                whoClicked.openInventory(ShapedRecipeGUI.inv);
            }
            case FURNACE -> {
                new FurnaceRecipeGUI(recipe, type);
                whoClicked.openInventory(FurnaceRecipeGUI.inv);
            }
        }
    }

    public static void signReadName(Player whoClicked, Inventory inv, IGUI type) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Name"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    String name = (strings[0] + " " + strings[1]).trim();
                    inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore(name).build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);

                    if (type instanceof MaterialCreationGUI)
                        MaterialCreationGUI.name = name;
                    else
                        ItemCreationGUI.name = name;

                    return true;
                }));

        menu.open(whoClicked);
    }

    public static CraftingType craftingTypeSwitch(CraftingType type, Inventory inv, int cookingTime, int cookingExp) {
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
                if (cookingTime > 0)
                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore(cookingTime / 20 + "s").build().getItemStack());
                else
                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore("§fIn seconds").build().getItemStack());

                if (cookingExp > 0)
                    inv.setItem(32, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).lore(cookingExp + " exp").build().getItemStack());
                else
                    inv.setItem(32, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).build().getItemStack());
            }
            case FURNACE -> {
                type = CraftingType.SHAPELESS;
                inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).build().getItemStack());
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
                inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
                inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Exp", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
            }
        }

        return type;
    }

    public static void signReadCookingTime(Player whoClicked, Inventory inv, IGUI type) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Cooking Time", "In Seconds"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    try {
                        if (Integer.parseInt(strings[0]) <= 0) {
                            player.sendMessage("§cCooking Time can't be less or equal to 0");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInsert a number");
                        return false;
                    }

                    int cookingTime = Integer.parseInt(strings[0]) * 20;

                    if (type instanceof MaterialCreationGUI)
                        MaterialCreationGUI.cookingTime = cookingTime;
                    else
                        ItemCreationGUI.cookingTime = cookingTime;


                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore(cookingTime / 20 + "s").build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    public static void signReadCookingExp(Player whoClicked, Inventory inv, IGUI type) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Cooking Exp"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    try {
                        if (Integer.parseInt(strings[0]) <= 0) {
                            player.sendMessage("§cCooking Exp can't be less or equal to 0");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cCooking Exp can't be less or equal to 0");
                        return false;
                    }

                    int cookingExp = Integer.parseInt(strings[0]);

                    if (type instanceof MaterialCreationGUI)
                        MaterialCreationGUI.cookingExp = cookingExp;
                    else
                        ItemCreationGUI.cookingExp = cookingExp;

                    inv.setItem(32, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).lore(cookingExp + " exp").build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
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
}
