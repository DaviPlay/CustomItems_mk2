package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.api.Instruction;
import davide.customitems.api.Utils;
import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.gui.MatsGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.itemCreation.MaterialBuilder;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MaterialCreationGUI extends GUI {
    public static Inventory inv;
    private ItemStack itemStack;
    private String name;
    private final List<String> lore;
    private Rarity rarity;
    private CraftingType craftingType;
    private final List<ItemStack> shapeless;
    private final List<ItemStack> shaped;
    private final List<ItemStack> furnace;
    private final HashMap<Double, List<EntityType>> entityDrops;
    private final HashMap<Double, List<Material>> blockDrops;

    private int cookingTime = 0;
    private int cookingExp = 0;

    public MaterialCreationGUI() {
        inv = Bukkit.createInventory(this, 54, "Creating a new Material...");
        itemStack = new ItemStack(Material.GOLD_INGOT);
        name = "";
        lore = new ArrayList<>();
        rarity = Rarity.COMMON;
        craftingType = CraftingType.SHAPELESS;
        shapeless = new ArrayList<>();
        shaped = new ArrayList<>();
        furnace = new ArrayList<>();
        entityDrops = new HashMap<>();
        blockDrops = new HashMap<>();
        cookingTime = 0;
        cookingExp = 0;
        setInv();
        new CookingTimeXpGUI(this);
        new MobDropChanceGUI(this);
    }

    private void setInv() {
        super.setInv(inv);

        inv.setItem(10, new UtilsBuilder(itemStack, "§aItemStack", false).build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).lore("§fLeave empty to use the default crafting recipe for materials").build().getItemStack());
        inv.setItem(22, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());
        inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
        inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time & Exp", false).lore("§eChoose the Furnace crafting type to change", "§ethe cooking time and the cooking experience").build().getItemStack());
        inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cMob & Drop Chance", false).lore("§eChoose the Drop crafting type to change", "§ethe mob and the drop chance").build().getItemStack());
        inv.setItem(34, new UtilsBuilder(new ItemStack(Material.ENCHANTING_TABLE), "§aBuild", false).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Crafting Material"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), true, -1, inv, getGUIType());
                }
            }, whoClicked, CraftingMaterialGUI.invs);
            case 12 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Name"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    String name = (strings[0] + " " + strings[1]).trim();
                    setName(name);
                    inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore("§f" + name).build().getItemStack());
                }
            }, whoClicked, inv);
            case 14 -> rarity = GUIUtils.rarityCycle(rarity, inv);
            case 16 -> {
                switch (craftingType) {
                    case SHAPELESS -> {
                        new ShapelessRecipeGUI(shapeless, this);
                        whoClicked.openInventory(ShapelessRecipeGUI.inv);
                    }
                    case SHAPED -> {
                        new ShapedRecipeGUI(shaped, this);
                        whoClicked.openInventory(ShapedRecipeGUI.inv);
                    }
                    case FURNACE -> {
                        new FurnaceRecipeGUI(furnace, this);
                        whoClicked.openInventory(FurnaceRecipeGUI.inv);
                    }
                }
            }
            case 22 -> {
                if (clickType.isLeftClick())
                    GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", " 1 line of lore"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            String line = (strings[0] + " " + strings[1]).trim();
                            lore.add(line);
                            inv.setItem(22, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + lore).build().getItemStack());
                        }
                    }, whoClicked, inv);
                else if (clickType.isRightClick()) {
                    if (!lore.isEmpty()) {
                        lore.remove(lore.size() - 1);
                        inv.setItem(22, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + lore).build().getItemStack());
                    } else
                        inv.setItem(22, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());
                }
            }
            case 28 -> craftingType = GUIUtils.craftingTypeSwitch(craftingType, inv);
            case 30 -> {
                if (craftingType == CraftingType.FURNACE) {
                    whoClicked.openInventory(CookingTimeXpGUI.inv);
                }
            }
            case 32 -> {
                if (craftingType == CraftingType.DROP) {
                    whoClicked.openInventory(MobDropChanceGUI.inv);
                }
            }
            case 34 -> createItem(whoClicked);
        }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    private void createItem(Player whoClicked) {
        Item item = null;
        if (name.trim().isEmpty()) {
            whoClicked.sendMessage("§cThe item must have a name!");
            return;
        }
        switch (craftingType) {
            case SHAPELESS -> {
                if (shapeless.stream().allMatch(Objects::isNull))
                    item = new MaterialBuilder(itemStack, name)
                            .rarity(rarity)
                            .lore(lore.toArray(new String[]{}))
                            .build();
                else
                    item = new MaterialBuilder(itemStack, name)
                            .rarity(rarity)
                            .lore(lore.toArray(new String[]{}))
                            .craftingType(craftingType)
                            .crafting(shapeless)
                            .build();
            }
            case SHAPED -> {
                if (shaped.stream().allMatch(Objects::isNull))
                    item = new MaterialBuilder(itemStack, name)
                            .rarity(rarity)
                            .lore(lore.toArray(new String[]{}))
                            .build();
                else
                    item = new MaterialBuilder(itemStack, name)
                            .rarity(rarity)
                            .lore(lore.toArray(new String[]{}))
                            .craftingType(craftingType)
                            .crafting(shaped)
                            .build();
            }
            case FURNACE -> {
                if (cookingExp == 0 || cookingTime == 0) {
                    whoClicked.sendMessage("§cYou must define a cooking time and exp");
                    return;
                }

                for (ItemStack i : shaped) {
                    if (i != null) {
                        item = new MaterialBuilder(itemStack, name)
                                .rarity(rarity)
                                .lore(lore.toArray(new String[]{}))
                                .cookingTime(cookingTime)
                                .exp(cookingExp)
                                .craftingType(craftingType)
                                .crafting(shaped)
                                .build();

                        break;
                    }
                }

                if (item == null) {
                    whoClicked.sendMessage("§cThe item's crafting recipe can't be empty");
                    return;
                }
            }
            case DROP -> {
                if (entityDrops == null && blockDrops == null){
                    whoClicked.sendMessage("§cYou must define a drop chance and the associated mobs/blocks");
                    return;
                }
                item = new MaterialBuilder(itemStack, name)
                        .rarity(rarity)
                        .lore(lore.toArray(new String[]{}))
                        .craftingType(craftingType)
                        .entityDrops(entityDrops)
                        .blockDrops(blockDrops)
                        .build();
            }
        }

        assert item != null;
        whoClicked.getInventory().addItem(item.getItemStack());
        new CraftingInventories(item);
        String tempKey = Utils.normalizeKey(name);
        GUIEvents.getArguments().add(tempKey);
        new MatsGUI();
        whoClicked.closeInventory();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public void setCraftingType(CraftingType craftingType) {
        this.craftingType = craftingType;
    }

    public List<ItemStack> getShapeless() {
        return shapeless;
    }

    public List<ItemStack> getShaped() {
        return shaped;
    }

    public List<ItemStack> getFurnace() {
        return furnace;
    }

    public HashMap<Double, List<EntityType>> getEntityDrops() {
        return entityDrops;
    }

    public HashMap<Double, List<Material>> getBlockDrops() {
        return blockDrops;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getCookingExp() {
        return cookingExp;
    }

    public void setCookingExp(int cookingExp) {
        this.cookingExp = cookingExp;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
