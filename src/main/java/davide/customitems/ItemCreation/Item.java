package davide.customitems.ItemCreation;

import davide.customitems.API.*;
import davide.customitems.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {
    private ItemStack itemStack;
    private Color color;
    private ItemType.Type type;
    private ItemRarity.Rarity rarity;
    private final List<AbilityType.Ability> abilities;
    private int delay;
    private boolean isGlint;
    private boolean isUnsafe;
    private final boolean isStackable;
    private final CraftingType.Crafting craftingType;
    private float exp = 0;
    private int cookingTime = 0;
    private HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private String name;
    private List<String> lore;

    private NamespacedKey key;

    //Materials
    public static final Item enchantedBone = new Item(new ItemStack(Material.BONE), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, false, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            null,
            null,
            null,
            null
    ), "Enchanted Bone");

    public static final Item enchantedString = new Item(new ItemStack(Material.STRING), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, false, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            null,
            null,
            null,
            null
    ), "Enchanted String");

    public static final Item enchantedGold = new Item(new ItemStack(Material.GOLD_INGOT), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, false, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            null,
            null,
            null,
            null
    ), "Enchanted Gold");

    public static final Item enchantedGoldBlock = new Item(new ItemStack(Material.GOLD_BLOCK), ItemType.Type.MATERIAL, ItemRarity.Rarity.RARE, null, true, false, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            enchantedGold.getItemStack(32),
            enchantedGold.getItemStack(32),
            enchantedGold.getItemStack(32),
            enchantedGold.getItemStack(32),
            enchantedGold.getItemStack(32),
            null,
            null,
            null,
            null
    ), "Enchanted Gold Block");

    public static final Item meltedSugar = new Item(new ItemStack(Material.WHITE_DYE, 4), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, false, 0, true,
            CraftingType.Crafting.FURNACE, 5, 10 * 20, Collections.singletonList(
            new ItemStack(Material.SUGAR)
    ), "Melted Sugar");

    //Cool items
    public static final Item recipeBook = new Item(new ItemStack(Material.KNOWLEDGE_BOOK), ItemType.Type.ITEM, ItemRarity.Rarity.COMMON, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            false, false, 0, false, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            new ItemStack(Material.STRING),
            null,
            new ItemStack(Material.STRING),
            new ItemStack(Material.BOOK),
            new ItemStack(Material.STRING),
            null,
            new ItemStack(Material.STRING),
            null
    ), "Recipe Book", "Shows all the custom crafting recipes");

    public static final Item stonk = new Item(new ItemStack(Material.GOLDEN_PICKAXE), ItemType.Type.TOOL, ItemRarity.Rarity.EPIC, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK), 120, false,
            new HashMap<Enchantment, Integer>(){{ put(Enchantment.DIG_SPEED, 6); put(Enchantment.DURABILITY, 10); }}, CraftingType.Crafting.SHAPED, Arrays.asList(
            enchantedGold.getItemStack(16),
            enchantedGold.getItemStack(16),
            enchantedGold.getItemStack(16),
            null,
            new ItemStack(Material.STICK),
            null,
            null,
            new ItemStack(Material.STICK),
            null
    ), "Stonk", "Gives haste 5 for 20 seconds");

    public static final Item explosiveWand = new Item(new ItemStack(Material.STICK), ItemType.Type.WAND, ItemRarity.Rarity.UNCOMMON, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            false, false, 0, true, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            null,
            new ItemStack(Material.TNT),
            null,
            new ItemStack(Material.STICK),
            null,
            new ItemStack(Material.STICK),
            null,
            null
    ), "Explosive Wand", "Creates an explosion that", "doesn't damages the player", "§8Destroyed on use");

    public static final Item ultimateBread = new Item(new ItemStack(Material.BREAD), ItemType.Type.FOOD, ItemRarity.Rarity.LEGENDARY, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            true, false, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.SUSPICIOUS_STEW),
            new ItemStack(Material.GOLDEN_CARROT),
            new ItemStack(Material.BREAD),
            new ItemStack(Material.SWEET_BERRIES),
            new ItemStack(Material.CHORUS_FRUIT),
            new ItemStack(Material.CAKE),
            new ItemStack(Material.COOKIE),
            new ItemStack(Material.COOKED_COD),
            new ItemStack(Material.HONEY_BOTTLE)
    ), "Ultimate Bread", "This special food gives you", "saturation for 5 minutes!", "§8§oStackable!");

    public static final Item soulBow = new Item(new ItemStack(Material.BOW), ItemType.Type.BOW, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.GENERIC),
            false, false, 0, true, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            enchantedBone.getItemStack(),
            enchantedString.getItemStack(),
            enchantedBone.getItemStack(),
            null,
            enchantedString.getItemStack(),
            null,
            enchantedBone.getItemStack(),
            enchantedString.getItemStack()
    ), "Soul Bow", "Spawns a wolf on impact", "that helps you in battle!", "§8§oCost: 1.5 Hearts");

    public static final Item cocaine = new Item(new ItemStack(Material.SUGAR), ItemType.Type.FOOD, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            true, false, 15, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8)
    ), "Cocaine", "Gives speed and jump boost...", "but at what cost");

    public static final Item aspectOfTheEnd = new Item(new ItemStack(Material.DIAMOND_SWORD), ItemType.Type.SWORD, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            false, false, 1, false, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            new ItemStack(Material.ENDER_EYE, 32),
            null,
            null,
            new ItemStack(Material.ENDER_EYE, 32),
            null,
            null,
            new ItemStack(Material.STICK),
            null
            ), "Aspect Of The End", "Teleports you 8 blocks", "from your current position", "in the direction you're facing");

    public static final Item grapplingHook = new Item(new ItemStack(Material.FISHING_ROD), ItemType.Type.TOOL, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.RIGHT_CLICK),
            false, false, 2, false, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            null,
            new ItemStack(Material.STICK),
            null,
            new ItemStack(Material.STICK),
            enchantedString.getItemStack(8),
            new ItemStack(Material.STICK),
            null,
            enchantedString.getItemStack(8)
    ), "Grappling Hook", "Makes you fly in the direction", "of the hook");

    public static final Item fireTalisman = new Item(new ItemStack(Material.GOLDEN_CARROT, 2), ItemType.Type.FOOD, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.GENERIC),
            true, false, 300, true, CraftingType.Crafting.SHAPED, Arrays.asList(
            new ItemStack(Material.BLAZE_POWDER),
            new ItemStack(Material.MAGMA_CREAM),
            new ItemStack(Material.BLAZE_POWDER),
            new ItemStack(Material.MAGMA_CREAM),
            new ItemStack(Material.GOLDEN_CARROT),
            new ItemStack(Material.MAGMA_CREAM),
            new ItemStack(Material.BLAZE_POWDER),
            new ItemStack(Material.MAGMA_CREAM),
            new ItemStack(Material.BLAZE_POWDER)
    ), "Fire Talisman", "On contact with a source of fire", "gives fire protection for 30s", "and regeneration 2 for 10s");

    public static final Item slimeBoots = new Item(new ItemStack(Material.LEATHER_BOOTS), Color.LIME, ItemType.Type.ARMOR, ItemRarity.Rarity.RARE, Collections.singletonList(AbilityType.Ability.GENERIC),
            false, false, 0, false, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            null,
            null,
            new ItemStack(Material.SLIME_BLOCK, 64),
            null,
            new ItemStack(Material.SLIME_BLOCK, 64),
            new ItemStack(Material.SLIME_BLOCK, 64),
            null,
            new ItemStack(Material.SLIME_BLOCK, 64)
    ), "Slime Boots", "Creates a pad of slime blocks", "that stops your fall");

    public static final Item midasStaff = new Item(new ItemStack(Material.TOTEM_OF_UNDYING), ItemType.Type.WAND, ItemRarity.Rarity.LEGENDARY, Arrays.asList(AbilityType.Ability.LEFT_CLICK, AbilityType.Ability.SHIFT_RIGHT_CLICK),
            false, true, 0, false, CraftingType.Crafting.SHAPED, Arrays.asList(
            null,
            enchantedGoldBlock.getItemStack(),
            null,
            enchantedGoldBlock.getItemStack(),
            enchantedGoldBlock.getItemStack(),
            enchantedGoldBlock.getItemStack(),
            null,
            enchantedGoldBlock.getItemStack(),
            null
    ), "Midas Staff", "Upon hitting a mob it", "turns into gold", "/s", "Gold, gold everywhere!");

    //Utils items
    public static final Item fillerGlass = new Item(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), null, null, null, false, false, 0, true, null, null, " ");
    public static final Item itemArrow = new Item(new ItemStack(Material.ARROW), null, null, null, false, false, 0, true, null, null, "Items");
    public static final Item matsArrow = new Item(new ItemStack(Material.ARROW), null, null, null, false, false, 0, true, null, null, "Materials");

    //Items array
    public static Item[][] items = {
            //Items
            {stonk, explosiveWand, ultimateBread, soulBow, cocaine, aspectOfTheEnd, grapplingHook, fireTalisman, slimeBoots, midasStaff},

            //Materials
            {enchantedBone, enchantedString, enchantedGold, enchantedGoldBlock, meltedSugar}
    };

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, List<AbilityType.Ability> ability, int delay, boolean isStackable, HashMap<Enchantment, Integer> enchantments,
                 CraftingType.Crafting craftingType, float exp, int cookingTime, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.abilities = ability;
        this.delay = delay;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.exp = exp;
        this.cookingTime = cookingTime;
        this.crafting = crafting;
        this.enchantments = enchantments;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, List<AbilityType.Ability> abilities, int delay, boolean isStackable, HashMap<Enchantment, Integer> enchantments,
                CraftingType.Crafting craftingType, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.abilities = abilities;
        this.delay = delay;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.crafting = crafting;
        this.enchantments = enchantments;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, Color color, ItemType.Type type, ItemRarity.Rarity rarity, List<AbilityType.Ability> ability, boolean isGlint, boolean isUnsafe, int delay, boolean isStackable,
                CraftingType.Crafting craftingType, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.color = color;
        this.type = type;
        this.rarity = rarity;
        this.abilities = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isUnsafe = isUnsafe;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, List<AbilityType.Ability> ability, boolean isGlint, boolean isUnsafe, int delay, boolean isStackable,
                CraftingType.Crafting craftingType, float exp, int cookingTime, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.abilities = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isUnsafe = isUnsafe;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.exp = exp;
        this.cookingTime = cookingTime;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, List<AbilityType.Ability> ability, boolean isGlint, boolean isUnsafe, int delay, boolean isStackable,
                CraftingType.Crafting craftingType, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.abilities = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isUnsafe = isUnsafe;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    private void create() {
        ItemMeta meta;

        //Checking if the item is leather armor
        switch (itemStack.getType()) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                meta = (LeatherArmorMeta) itemStack.getItemMeta();
                break;
            default:
                meta = itemStack.getItemMeta();
                break;
        }

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (name.charAt(0) == '§')
            key = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), name.toLowerCase(Locale.ROOT).replace(" ", "_").replace(name.charAt(1), '§').replace("§", ""));
        else
            key = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), name.toLowerCase(Locale.ROOT).replace(" ", "_"));
        container.set(key, PersistentDataType.INTEGER, 1);

        if (rarity != null)
            meta.setDisplayName(rarity.getColor() + name);
        else
            meta.setDisplayName(name);

        //Ability prefix
        String separator = "/s";
        int j = 0;
        if (abilities != null)
            for (AbilityType.Ability ability : abilities)
                if (!lore.isEmpty()) {
                    lore.add(j, "§6Item Ability:");

                    if (ability != AbilityType.Ability.GENERIC)
                        lore.add(j + 1, "§e§l" + ability.getPrefix());

                    lore.add(j, "");

                    for (; j < lore.size(); j++)
                        if (lore.get(j).equals(separator)) {
                            lore.remove(lore.get(j));
                            break;
                        }
                }

        //Making unmarked lore gray
        List<String> newLore = new ArrayList<>();
        if (!lore.isEmpty()) {
            for (String s : lore) {
                if (!(s.startsWith("§")))
                    s = "§7" + s;

                newLore.add(s);
                lore = newLore;
            }
        }

        //Adding the cooldown to the lore
        if (delay > 0)
            if (delay < 60)
                lore.add("§8§o" + delay + " sec cooldown");
            else
                lore.add("§8§o" + delay / 60 + " min cooldown");

        //More fancy spacing
        if (!lore.isEmpty())
            lore.add("");

        //Rarity Suffix
        if (type != null && rarity != null) {
            if (rarity == ItemRarity.Rarity.TEST) {
                lore.add("§cThis item is a test and thus unfinished,");
                lore.add("§cit may not work as intended");
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
            } else
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.name());
        }

        //Makes it glow without adding any enchantment
        if (isGlint) {
            NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
            Glow glow = new Glow(glowKey);

            if (!isUnsafe) {
                meta.addEnchant(glow, 1, true);
            } else {
                itemStack.addUnsafeEnchantment(glow, 1);
            }
        }

        //Adding the enchants and making them blue
        String str, enchName, lvl;

        if (enchantments != null)
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);

                if (!lore.isEmpty()) {
                    str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
                    enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
                    lvl = entry.getValue().toString();

                    lore.add(0, "§9" + enchName + " " + lvl);
                }
            }

        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
        }

        itemStack.setItemMeta(meta);

        //Recipe
        if (crafting != null)
            new Crafting(key, itemStack, craftingType, exp, cookingTime, crafting);
    }

    public static Item toItem(ItemStack is) {
        Item item = null;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item[] items : Item.items)
            for (Item i : items) {
                if (container.has(i.getKey(), PersistentDataType.INTEGER))
                    item = i;
            }

        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStack(int n) {
        ItemStack is = itemStack.clone();
        is.setAmount(n);

        return is;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemType.Type getType() {
        return type;
    }

    public void setType(ItemType.Type type) {
        this.type = type;
    }

    public ItemRarity.Rarity getRarity() {
        return rarity;
    }

    public void setRarity(ItemRarity.Rarity rarity) {
        this.rarity = rarity;
    }

    public List<AbilityType.Ability> getAbilities() {
        return abilities;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isGlint() {
        return isGlint;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public void setGlint(boolean glint, ItemStack item) {
        isGlint = glint;
        assert item.getItemMeta() != null;

        NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
        Glow glow = new Glow(glowKey);

        if (isGlint)
            if (isUnsafe)
                item.addUnsafeEnchantment(glow, 1);
            else
                item.getItemMeta().addEnchant(glow, 1, true);
        else
            if (isUnsafe)
                item.removeEnchantment(glow);
            else
                item.getItemMeta().removeEnchant(glow);
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public CraftingType.Crafting getCraftingType() {
        return craftingType;
    }

    public List<ItemStack> getCrafting() {
        return crafting;
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

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
