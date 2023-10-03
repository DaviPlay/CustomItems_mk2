package davide.customitems.gui.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.*;
import davide.customitems.itemCreation.MaterialBuilder;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemCreationGUI implements IGUI {
    public static Inventory inv;
    protected static ItemStack itemStack;
    protected static String name;
    protected static Type type;
    protected static SubType subType;
    protected static int damage;
    protected static int critChance;
    protected static int critDamage;
    protected static int health;
    protected static int defence;
    protected static List<Ability> abilities;
    protected static HashMap<Enchantment, Integer> enchantments;
    protected static List<String> lore;
    protected static Rarity rarity;
    protected static CraftingType craftingType;

    protected static List<ItemStack> recipe;
    protected static int cookingTime = 0;
    protected static int cookingExp = 0;

    public ItemCreationGUI() {
        inv = Bukkit.createInventory(this, 54, "Creating a new Item...");
        itemStack = new ItemStack(Material.GOLD_INGOT);
        name = " ";
        type = null;
        subType = null;
        damage = 0;
        critChance = 0;
        critDamage = 0;
        health = 0;
        defence = 0;
        abilities = new ArrayList<>();
        enchantments = new HashMap<>();
        recipe = new ArrayList<>();
        cookingTime = 0;
        cookingExp = 0;
        rarity = Rarity.COMMON;
        craftingType = CraftingType.SHAPELESS;
        recipe = new ArrayList<>();
        setInv();
    }

    private void setInv() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, ItemList.fillerGlass.getItemStack());

        inv.setItem(10, new UtilsBuilder(itemStack, "§aItemStack", false).build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).build().getItemStack());
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.STICK), "§aType", false).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).build().getItemStack());
        inv.setItem(22, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§aStats", false).build().getItemStack());
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
            case 10 -> GUIUtils.signReadCraftingMat(whoClicked, -1, false, inv);
            case 12 -> GUIUtils.signReadName(whoClicked, inv, this);
            case 13 -> {
                new TypeGUI();
                whoClicked.openInventory(TypeGUI.inv);
            }
            case 14 -> rarity = GUIUtils.rarityCycle(rarity, inv);
            case 16 -> GUIUtils.openRecipeGUIs(whoClicked, craftingType, recipe, this);
            case 22 -> {
                new StatsGUI();
                whoClicked.openInventory(StatsGUI.inv);
            }
            case 28 -> craftingType = GUIUtils.craftingTypeSwitch(craftingType, inv, cookingTime, cookingExp);
            case 30 -> {
                if (craftingType == CraftingType.FURNACE)
                    GUIUtils.signReadCookingTime(whoClicked, inv, this);
            }
            case 32 -> {
                if (craftingType == CraftingType.FURNACE)
                    GUIUtils.signReadCookingExp(whoClicked, inv, this);
            }
            case 34 -> createItem(whoClicked);
            case 45 -> whoClicked.openInventory(CreateItemGUI.inv);
            case 49 -> whoClicked.closeInventory();
        }
    }

    private void createItem(Player whoClicked) {
        switch (craftingType) {
            case SHAPELESS, SHAPED -> {
                for (ItemStack i : recipe)
                    if (i != null) {
                        Item item = new MaterialBuilder(itemStack, name)
                                .rarity(rarity)
                                .craftingType(craftingType)
                                .crafting(recipe)
                                .build();

                        whoClicked.getInventory().addItem(item.getItemStack());
                        new CraftingInventories(item);
                        GUIEvents.getArguments().add(item.getKey().getKey());

                        return;
                    }

                whoClicked.sendMessage("§cThe item's crafting recipe can't be empty");
            }
            case FURNACE -> {
                if (cookingExp == 0 || cookingTime == 0) {
                    whoClicked.sendMessage("You must define a cooking time and exp");
                    return;
                }

                for (ItemStack i : recipe)
                    if (i != null) {
                        Item item = new MaterialBuilder(itemStack, name)
                                .rarity(rarity)
                                .cookingTime(cookingTime)
                                .exp(cookingExp)
                                .craftingType(craftingType)
                                .crafting(recipe)
                                .build();

                        whoClicked.getInventory().addItem(item.getItemStack());
                        new CraftingInventories(item);
                        GUIEvents.getArguments().add(item.getKey().getKey());

                        return;
                    }

                whoClicked.sendMessage("§cThe item's crafting recipe can't be empty");
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
