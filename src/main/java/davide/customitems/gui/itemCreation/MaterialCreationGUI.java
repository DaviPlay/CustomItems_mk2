package davide.customitems.gui.itemCreation;

import davide.customitems.CustomItems;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.itemCreation.builders.MaterialBuilder;
import davide.customitems.itemCreation.builders.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaterialCreationGUI implements IGUI, Listener {
    public static Inventory inv;
    protected static ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
    protected static String name = " ";
    protected static Rarity rarity = Rarity.COMMON;
    protected static CraftingType craftingType = CraftingType.SHAPELESS;
    protected static List<ItemStack> crafting = new ArrayList<>();
    private int cookingTime = 0;
    private int cookingExp = 0;

    public MaterialCreationGUI() {
        inv = Bukkit.createInventory(this, 54, "Creating a new Material...");
        setInv();
        itemStack = new ItemStack(Material.GOLD_INGOT);
    }

    private void setInv() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        inv.setItem(10, new UtilsBuilder(itemStack, "§aItemStack", false).build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).build().getItemStack());
        inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
        inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
        inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Exp", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
        inv.setItem(34, new UtilsBuilder(new ItemStack(Material.ENCHANTING_TABLE), "§aBuild", false).build().getItemStack());

        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10 -> signReadCraftingMat(whoClicked, -1, false);
            case 12 -> signReadName(whoClicked);
            case 14 -> rarityCycle();
            case 16 -> {
                new CraftingRecipeGUI(crafting);
                whoClicked.openInventory(CraftingRecipeGUI.inv);
            }
            case 28 -> craftingTypeSwitch();
            case 30 -> {
                if (craftingType == CraftingType.FURNACE)
                    signReadCookingTime(whoClicked);
            }
            case 32 -> {
                if (craftingType == CraftingType.FURNACE)
                    signReadCookingExp(whoClicked);
            }
            case 34 -> {
                Item item = new MaterialBuilder(itemStack, name)
                        .rarity(rarity)
                        .cookingTime(cookingTime)
                        .exp(cookingExp)
                        .craftingType(craftingType)
                        .crafting(crafting)
                        .build();

                whoClicked.getInventory().addItem(item.getItemStack());
                new CraftingInventories(item);
                GUIEvents.getArguments().add(item.getKey().getKey());
            }
            case 45 -> whoClicked.openInventory(CreateItemGUI.inv);
            case 49 -> whoClicked.closeInventory();
        }
    }

    private void rarityCycle() {
        switch (rarity) {
            case COMMON -> {
                rarity = Rarity.UNCOMMON;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.LIME_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case UNCOMMON -> {
                rarity = Rarity.RARE;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case RARE -> {
                rarity = Rarity.EPIC;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.PURPLE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case EPIC -> {
                rarity = Rarity.LEGENDARY;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case LEGENDARY -> {
                rarity = Rarity.MYTHIC;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.MAGENTA_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case MYTHIC -> {
                rarity = Rarity.SUPREME;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
            case SUPREME -> {
                rarity = Rarity.COMMON;
                inv.setItem(14, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
            }
        }
    }

    private void signReadName(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Name"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    name = (strings[0] + " " + strings[1]).trim();
                    inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore(name).build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    public static void signReadCraftingMat(Player whoClicked, int slot, boolean isCraftingMat) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Crafting Material"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), slot, isCraftingMat);
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(CraftingMaterialGUI.invs.get(0)), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    private void signReadCookingTime(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Cooking Time", "In Seconds"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    try {
                        if (Integer.parseInt(strings[0]) <= 0) {
                            player.sendMessage("§cCooking Time can't be less or equal to 0");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cCooking Time can't be less or equal to 0");
                        return false;
                    }

                    cookingTime = Integer.parseInt(strings[0]) * 20;
                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore(cookingTime / 20 + "s").build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    private void signReadCookingExp(Player whoClicked) {
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

                    cookingExp = Integer.parseInt(strings[0]);
                    inv.setItem(32, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).lore(cookingExp + " exp").build().getItemStack());
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    private void craftingTypeSwitch() {
        switch (craftingType) {
            case SHAPELESS -> {
                craftingType = CraftingType.SHAPED;
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.SMITHING_TABLE), "§aCrafting Type", false).lore("§fShaped").build().getItemStack());
            }
            case SHAPED -> {
                craftingType = CraftingType.FURNACE;
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FURNACE), "§aCrafting Type", false).lore("§fFurnace").build().getItemStack());
                if (cookingTime > 0)
                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore(cookingTime / 20 + "s").build().getItemStack());
                else
                    inv.setItem(30, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooking Time", false).lore("§fIn seconds").build().getItemStack());
                if (cookingExp > 0)
                    new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).lore(cookingExp + " exp").build().getItemStack();
                else
                    inv.setItem(32, new UtilsBuilder(new ItemStack(Material.EXPERIENCE_BOTTLE), "§aCooking Exp", false).build().getItemStack());
            }
            case FURNACE -> {
                craftingType = CraftingType.SHAPELESS;
                inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
                inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
                inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Exp", false).lore("§eChoose the Furnace crafting type").build().getItemStack());
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
