package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.DelayedTask;
import davide.customitems.api.Instruction;
import davide.customitems.api.Utils;
import davide.customitems.crafting.CraftingType;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.GUI;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.*;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class ItemCreationGUI extends GUI {
    public static Inventory inv;
    private final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    private ItemStack itemStack;
    private String name;
    private Type type;
    private SubType subType;
    private int damage;
    private float attackSpeed;
    private int critChance;
    private float critDamage;
    private int health;
    private int defence;
    private final List<Ability> abilities;
    private final HashMap<Enchantment, Integer> enchantments;
    private final List<String> lore;
    private Rarity rarity;
    private CraftingType craftingType;
    private final List<ItemStack> shapeless;
    private final List<ItemStack> shaped;
    private final List<ItemStack> furnace;
    private final HashMap<Double, List<EntityType>> entityDrops;
    private final HashMap<Double, List<Material>> blockDrops;
    private int cookingTime;
    private int cookingExp;

    public ItemCreationGUI() {
        inv = Bukkit.createInventory(this, 54, "Creating a new Item...");
        itemStack = new ItemStack(Material.GOLD_INGOT);
        name = " ";
        type = Type.ITEM;
        subType = null;
        damage = 0;
        critChance = 0;
        critDamage = 0;
        health = 0;
        defence = 0;
        abilities = new ArrayList<>();
        enchantments = new HashMap<>();
        lore = new ArrayList<>();
        shapeless = new ArrayList<>();
        shaped = new ArrayList<>();
        furnace = new ArrayList<>();
        entityDrops = new HashMap<>();
        blockDrops = new HashMap<>();
        cookingTime = 0;
        cookingExp = 0;
        rarity = Rarity.COMMON;
        craftingType = CraftingType.SHAPELESS;
        setInv();
        new TypeGUI(this);
        new StatsGUI(this);
        new AbilitiesGUI(this);
        new CookingTimeXpGUI(this);
        new MobDropChanceGUI(this);
    }

    private void setInv() {
        super.setInv(inv);

        inv.setItem(10, new UtilsBuilder(itemStack, "§aItemStack", false).build().getItemStack());
        inv.setItem(11, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.STICK), "§aItem Type", false).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§aRarity", false).lore(rarity.getColor() + rarity.name()).build().getItemStack());
        inv.setItem(15, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§aAbilities", false).build().getItemStack());
        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aCrafting Recipe", false).build().getItemStack());
        inv.setItem(21, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§aStats", false).build().getItemStack());
        inv.setItem(23, new UtilsBuilder(new ItemStack(Material.ENCHANTED_BOOK), "§aEnchantments", false)
                .lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last one", "§cFirst line is the enchant name, second line is it's level").build().getItemStack());
        inv.setItem(28, new UtilsBuilder(new ItemStack(Material.FLETCHING_TABLE), "§aCrafting Type", false).lore("§fShapeless").build().getItemStack());
        inv.setItem(30, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cCooking Time & Exp", false).lore("§eChoose the Furnace crafting type to change", "§ethe cooking time and the cooking experience").build().getItemStack());
        inv.setItem(32, new UtilsBuilder(new ItemStack(Material.RED_STAINED_GLASS_PANE), "§cMob, Blocks & Drop Chance", false).lore("§eChoose the Drop crafting type to change", "§ethe mob and the drop chance").build().getItemStack());
        inv.setItem(34, new UtilsBuilder(new ItemStack(Material.ENCHANTING_TABLE), "§aBuild", false).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "ItemStack"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), true, -1, inv, getGUIType());
                }
            }, whoClicked, CraftingMaterialGUI.invs);
            case 11 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Name"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    String name = (strings[0] + " " + strings[1]).trim();
                    setName(name);
                    inv.setItem(11, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore("§f" + name).build().getItemStack());
                }
            }, whoClicked, inv);
            case 12 -> {
                if (clickType.isLeftClick())
                    GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", " 1 line of lore"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            String line = (strings[0] + " " + strings[1]).trim();
                            lore.add(line);
                            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + lore).build().getItemStack());
                        }
                    }, whoClicked, inv);
                else if (clickType.isRightClick()) {
                    if (!lore.isEmpty()) lore.remove(lore.size() - 1);
                    if (lore.isEmpty())
                        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());
                    else
                        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aLore", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + lore).build().getItemStack());
                }
            }
            case 13 -> whoClicked.openInventory(TypeGUI.inv);
            case 14 -> rarity = GUIUtils.rarityCycle(rarity, inv);
            case 15 -> whoClicked.openInventory(AbilitiesGUI.inv);
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
            case 21 -> whoClicked.openInventory(StatsGUI.inv);
            case 23 -> {
                if (clickType.isLeftClick()) {
                    GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Name & Level"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            int lvl;
                            try {
                                lvl = Integer.parseInt(strings[1]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInvalid level (2nd line)");
                                new DelayedTask(() -> whoClicked.openInventory(inv));
                                return;
                            }
                            new CraftingEnchantsGUI((strings[0]).trim(), lvl, getGUIType());
                        }
                    }, whoClicked, CraftingEnchantsGUI.invs);
                } else if (clickType.isRightClick()) {
                    if (!enchantments.isEmpty())
                        enchantments.remove(enchantments.keySet().iterator().next());

                    List<String> enchs = new ArrayList<>();
                    for (Map.Entry<Enchantment, Integer> e : enchantments.entrySet())
                        enchs.add(e.getKey().getKey().getKey().replace("_", " ") + " " + e.getValue().toString());

                    if (enchantments.isEmpty())
                        inv.setItem(23, new UtilsBuilder(new ItemStack(Material.ENCHANTED_BOOK), "§aEnchantments", false)
                                .lore("§eLeft click to add an enchantment", "§eRight click to remove the last one", "§cFirst line is the enchant name, second line is it's level").build().getItemStack());
                    else
                        inv.setItem(23, new UtilsBuilder(new ItemStack(Material.ENCHANTED_BOOK), "§aEnchantments", false)
                                .lore("§eLeft click to add an enchantment", "§eRight click to remove the last one", "§cFirst line is the enchant name, second line is it's level", "", "§f" + enchs).build().getItemStack());
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
            case 34 -> {
                try {
                    createItem(whoClicked);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    private void createItem(Player whoClicked) throws IOException {
        if (name.trim().isEmpty()) {
            whoClicked.sendMessage("§cThe item must have a name!");
            return;
        }

        Item item = null;
        switch (craftingType) {
            case SHAPELESS -> {
                for (ItemStack i : shapeless)
                    if (i != null) {
                        if (subType == null) {
                            item = new ItemBuilder(itemStack, name, true)
                                    .type(type)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.SHAPELESS)
                                    .crafting(shapeless)
                                    .hasRandomUUID(true)
                                    .build();
                        }
                        else {
                            item = new ItemBuilder(itemStack, name, true)
                                    .subType(subType)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.SHAPELESS)
                                    .crafting(shapeless)
                                    .hasRandomUUID(true)
                                    .build();
                        }

                        break;
                    }

                if (item == null) {
                    whoClicked.sendMessage("§cThe item's crafting recipe can't be empty");
                    return;
                }
            }
            case SHAPED -> {
                for (ItemStack i : shaped)
                    if (i != null) {
                        if (subType == null) {
                            item = new ItemBuilder(itemStack, name, true)
                                    .type(type)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.SHAPED)
                                    .crafting(shaped)
                                    .hasRandomUUID(true)
                                    .build();
                        }
                        else {
                            item = new ItemBuilder(itemStack, name, true)
                                    .subType(subType)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.SHAPED)
                                    .crafting(shaped)
                                    .hasRandomUUID(true)
                                    .build();
                        }

                        break;
                    }

                if (item == null) {
                    whoClicked.sendMessage("§cThe item's crafting recipe can't be empty");
                    return;
                }
            }
            case FURNACE -> {
                if (cookingExp == 0 || cookingTime == 0) {
                    whoClicked.sendMessage("You must define a cooking time and exp");
                    return;
                }

                for (ItemStack i : furnace)
                    if (i != null) {
                        if (subType == null) {
                            item = new ItemBuilder(itemStack, name, true)
                                    .type(type)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .cookingTime(cookingTime)
                                    .exp(cookingExp)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.FURNACE)
                                    .crafting(furnace)
                                    .hasRandomUUID(true)
                                    .build();
                        }
                        else {
                            item = new ItemBuilder(itemStack, name, true)
                                    .subType(subType)
                                    .rarity(rarity)
                                    .attackSpeed(attackSpeed)
                                    .damage(damage)
                                    .critChance(critChance)
                                    .critDamage(critDamage)
                                    .health(health)
                                    .defence(defence)
                                    .cookingTime(cookingTime)
                                    .exp(cookingExp)
                                    .abilities(abilities.toArray(new Ability[]{}))
                                    .enchantments(enchantments)
                                    .lore(lore.toArray(new String[]{}))
                                    .craftingType(CraftingType.FURNACE)
                                    .crafting(furnace)
                                    .hasRandomUUID(true)
                                    .build();
                        }
                        break;
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
                if (subType == null) {
                    item = new ItemBuilder(itemStack, name, true)
                            .type(type)
                            .rarity(rarity)
                            .attackSpeed(attackSpeed)
                            .damage(damage)
                            .critChance(critChance)
                            .critDamage(critDamage)
                            .health(health)
                            .defence(defence)
                            .abilities(abilities.toArray(new Ability[]{}))
                            .enchantments(enchantments)
                            .lore(lore.toArray(new String[]{}))
                            .craftingType(CraftingType.DROP)
                            .hasRandomUUID(true)
                            .build();
                }
                else {
                    item = new ItemBuilder(itemStack, name, true)
                            .subType(subType)
                            .rarity(rarity)
                            .attackSpeed(attackSpeed)
                            .damage(damage)
                            .critChance(critChance)
                            .critDamage(critDamage)
                            .health(health)
                            .defence(defence)
                            .entityDrops(entityDrops)
                            .blockDrops(blockDrops)
                            .abilities(abilities.toArray(new Ability[]{}))
                            .enchantments(enchantments)
                            .lore(lore.toArray(new String[]{}))
                            .craftingType(CraftingType.DROP)
                            .hasRandomUUID(true)
                            .build();
                }
            }
        }

        assert item != null;
        whoClicked.getInventory().addItem(item.getItemStack());
        new CraftingInventories(item);

        String tempKey = Utils.normalizeKey(name);
        GUIEvents.getArguments().add(tempKey);
        new GUIEvents(tempKey);
        new ItemsGUI();

        //Adding the item fields to userItems.yml to be loaded next server start
        List<String> enchs = new ArrayList<>();
        List<String> mobs = new ArrayList<>();
        List<String> blocks = new ArrayList<>();

        plugin.getUserItemsConfig().set("items." + tempKey + ".name", name);
        plugin.getUserItemsConfig().set("items." + tempKey + ".material", itemStack.getType().getKey().getKey().toUpperCase(Locale.ROOT));
        plugin.getUserItemsConfig().set("items." + tempKey + ".amount", itemStack.getAmount());

        if (subType == null)
            plugin.getUserItemsConfig().set("items." + tempKey + ".type", getType().name());
        else
            plugin.getUserItemsConfig().set("items." + tempKey + ".sub_type", getSubType().name());

        plugin.getUserItemsConfig().set("items." + tempKey + ".rarity", rarity.name());
        plugin.getUserItemsConfig().set("items." + tempKey + ".attack_speed", attackSpeed);
        plugin.getUserItemsConfig().set("items." + tempKey + ".damage", damage);
        plugin.getUserItemsConfig().set("items." + tempKey + ".crit_chance", critChance);
        plugin.getUserItemsConfig().set("items." + tempKey + ".crit_damage", critDamage);
        plugin.getUserItemsConfig().set("items." + tempKey + ".health", health);
        plugin.getUserItemsConfig().set("items." + tempKey + ".defence", defence);

        if (!abilities.isEmpty())
            for (Ability ability : abilities) {
                plugin.getUserItemsConfig().set("items." + tempKey + ".ability." + ability.key().getKey() + ".event", ability.event().name());
                plugin.getUserItemsConfig().set("items." + tempKey + ".ability." + ability.key().getKey() + ".ability_type", ability.type().name());
                plugin.getUserItemsConfig().set("items." + tempKey + ".ability." + ability.key().getKey() + ".name", ability.name());
                plugin.getUserItemsConfig().set("items." + tempKey + ".ability." + ability.key().getKey() + ".cooldown", ability.cooldown());
                plugin.getUserItemsConfig().set("items." + tempKey + ".ability." + ability.key().getKey() + ".description", Arrays.stream(ability.description()).toList());
            }

        if (!enchantments.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                String enchName = entry.getKey().getKey().getKey().replace("_", " ");
                enchName = enchName.substring(0, 1).toUpperCase(Locale.ROOT) + enchName.substring(1);
                if (enchName.contains("edge"))
                    enchName = "Sweeping edge";
                enchs.add(enchName + " " + entry.getValue());
            }
        }
        plugin.getUserItemsConfig().set("items." + tempKey + ".enchantments", enchs);

        plugin.getUserItemsConfig().set("items." + tempKey + ".lore", lore);
        plugin.getUserItemsConfig().set("items." + tempKey + ".crafting_type", craftingType.name());

        switch (craftingType) {
            case SHAPELESS -> {
                List<String> ss = new ArrayList<>();

                shapeless.forEach(i -> {
                    if (i == null)
                        ss.add("null");
                    else if (Item.isCustomItem(i))
                        ss.add(Item.toItem(i).getKey().getKey());
                    else {
                        ss.add(i.getType().getKey().getKey().toUpperCase(Locale.ROOT) + ", " + i.getAmount());
                    }
                });

                plugin.getUserItemsConfig().set("items." + tempKey + ".crafting_recipe", ss);
            }
            case SHAPED -> {
                List<String> sd = new ArrayList<>();

                shaped.forEach(i -> {
                    if (i == null)
                        sd.add("null");
                    else if (Item.isCustomItem(i))
                        sd.add(Item.toItem(i).getKey().getKey());
                    else
                        sd.add(i.getType().getKey().getKey().toUpperCase(Locale.ROOT) + ", " + i.getAmount());
                });

                plugin.getUserItemsConfig().set("items." + tempKey + ".crafting_recipe", sd);
            }
            case FURNACE -> {
                if (Item.isCustomItem(furnace.get(0)))
                    plugin.getUserItemsConfig().set("items." + tempKey + ".crafting_recipe", Item.toItem(furnace.get(0)).getKey().getKey() + ", " + furnace.get(0).getAmount());
                else {
                    String f = furnace.get(0).getType().getKey().getKey().toUpperCase(Locale.ROOT);
                    plugin.getUserItemsConfig().set("items." + tempKey + ".crafting_recipe", f + ", " + furnace.get(0).getAmount());
                }
            }
            case DROP -> {
                assert entityDrops != null;
                double chance;
                for (Map.Entry<Double, List<EntityType>> entry : entityDrops.entrySet()) {
                    chance = entry.getKey();

                    for (EntityType e : entry.getValue()) {
                        String mobName = e.getKey().getKey();
                        mobs.add(mobName);
                    }

                    plugin.getUserItemsConfig().set("items." + tempKey + ".entity_drops." + chance, mobs);
                }

                for (Map.Entry<Double, List<Material>> entry : blockDrops.entrySet()) {
                    chance = entry.getKey();

                    for (Material m : entry.getValue()) {
                        String mobName = m.getKey().getKey();
                        mobs.add(mobName);
                    }

                    plugin.getUserItemsConfig().set("items." + tempKey + ".block_drops." + chance, blocks);
                }
            }
        }

        plugin.getUserItemsConfig().save(plugin.getUserItemsFile());

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

    public Type getType() {
        return type == null ? subType.getType() : type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public float getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(float critDamage) {
        this.critDamage = critDamage;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
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
