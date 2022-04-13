package davide.customitems.ItemCreation;

import davide.customitems.API.*;
import davide.customitems.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {
    private ItemStack itemStack;
    private ItemType.Type type;
    private ItemRarity.Rarity rarity;
    private final AbilityType.Ability ability;
    private int delay;
    private boolean isGlint;
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
    public static final Item enchantedBone = new Item(new ItemStack(Material.BONE), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32),
            new ItemStack(Material.BONE, 32)
    ), "Enchanted Bone");

    public static final Item enchantedString = new Item(new ItemStack(Material.STRING), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32),
            new ItemStack(Material.STRING, 32)
    ), "Enchanted String");

    public static final Item enchantedGold = new Item(new ItemStack(Material.GOLD_INGOT), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32),
            new ItemStack(Material.GOLD_INGOT, 32)
    ), "Enchanted Gold");

    public static final Item meltedSugar = new Item(new ItemStack(Material.WHITE_DYE, 4), ItemType.Type.MATERIAL, ItemRarity.Rarity.UNCOMMON, null, true, 0, true, CraftingType.Crafting.FURNACE, 5, 10 * 20,
            Collections.singletonList(
            new ItemStack(Material.SUGAR)
    ), "Melted Sugar");

    //Cool items
    public static final Item stonk = new Item(new ItemStack(Material.GOLDEN_PICKAXE), ItemType.Type.TOOL, ItemRarity.Rarity.EPIC, AbilityType.Ability.RIGHT_CLICK, 120, false,
            new HashMap<Enchantment, Integer>(){{put(Enchantment.DIG_SPEED, 6);}}, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    public static final Item explosiveWand = new Item(new ItemStack(Material.STICK), ItemType.Type.WAND, ItemRarity.Rarity.UNCOMMON, AbilityType.Ability.RIGHT_CLICK, true, 0, true, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    public static final Item ultimateBread = new Item(new ItemStack(Material.BREAD), ItemType.Type.FOOD, ItemRarity.Rarity.LEGENDARY, AbilityType.Ability.RIGHT_CLICK, true, 0, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
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

    public static final Item soulBow = new Item(new ItemStack(Material.BOW), ItemType.Type.BOW, ItemRarity.Rarity.RARE, AbilityType.Ability.GENERIC, true, 0, true, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    public static final Item cocaine = new Item(new ItemStack(Material.SUGAR), ItemType.Type.FOOD, ItemRarity.Rarity.RARE, AbilityType.Ability.RIGHT_CLICK, true, 15, true, CraftingType.Crafting.SHAPELESS, Arrays.asList(
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8),
            meltedSugar.getItemStack(8)
    ), "Cocaine", "Gives speed and jump boost...", "but at what cost");

    public static final Item aspectOfTheEnd = new Item(new ItemStack(Material.DIAMOND_SWORD), ItemType.Type.SWORD, ItemRarity.Rarity.RARE, AbilityType.Ability.RIGHT_CLICK, true, 1, false, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    public static final Item grapplingHook = new Item(new ItemStack(Material.FISHING_ROD), ItemType.Type.TOOL, ItemRarity.Rarity.RARE, AbilityType.Ability.RIGHT_CLICK, true, 2, false, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    public static final Item fireTalisman = new Item(new ItemStack(Material.GOLDEN_CARROT, 2), ItemType.Type.FOOD, ItemRarity.Rarity.RARE, AbilityType.Ability.RIGHT_CLICK, true, 300, true, CraftingType.Crafting.SHAPED, Arrays.asList(
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

    //Utils items
    public static final Item fillerGlass = new Item(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), null, null, null, false, 0, true, null, null, " ");
    public static final Item itemArrow = new Item(new ItemStack(Material.ARROW), null, null, null, false, 0, true, null, null, "Items");
    public static final Item matsArrow = new Item(new ItemStack(Material.ARROW), null, null, null, false, 0, true, null, null, "Materials");

    //Items array
    public static Item[][] items = {
            //Items
            {stonk, explosiveWand, ultimateBread, soulBow, cocaine, aspectOfTheEnd, grapplingHook, fireTalisman},

            //Materials
            {enchantedBone, enchantedString, enchantedGold, meltedSugar}
    };

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, int delay, boolean isStackable, HashMap<Enchantment, Integer> enchantments,
                 CraftingType.Crafting craftingType, float exp, int cookingTime, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
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

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, int delay, boolean isStackable, HashMap<Enchantment, Integer> enchantments,
                CraftingType.Crafting craftingType, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.crafting = crafting;
        this.enchantments = enchantments;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, boolean isGlint, int delay, boolean isStackable,
                CraftingType.Crafting craftingType, float exp, int cookingTime, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.exp = exp;
        this.cookingTime = cookingTime;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, boolean isGlint, int delay, boolean isStackable,
                CraftingType.Crafting craftingType, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isStackable = isStackable;
        this.craftingType = craftingType;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    private void create() {
        //Asserting dominance
        ItemMeta meta = itemStack.getItemMeta();
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

        //Fancy spacing
        if (!lore.isEmpty())
            lore.add(0, "");

        //Ability prefix
        if (!lore.isEmpty() && ability != null) {
            lore.add(1, "§6Item Ability:");

            if (ability != AbilityType.Ability.GENERIC)
                lore.add(2, "§e§l" + ability.getPrefix());
        }

        //Making unmarked lore gray
        List<String> newLore = new ArrayList<>();
        for (String s : lore)
            if (!(s.startsWith("§"))) {
                s = "§7" + s;

                newLore.add(s);
            } else
                newLore.add(s);

        lore = newLore;

        //Adding the cooldown to the lore
        if (delay > 0)
            if (delay < 60)
                lore.add("§8§o" + delay + " sec cooldown");
            else
                lore.add("§8§o" + delay / 60 + " min cooldown");

        //Fancy spacing pt.2
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
            meta.addEnchant(glow, 1 , true);
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

        itemStack.setItemMeta(meta);

        //Recipe
        if (crafting != null)
            new Crafting(key, itemStack, craftingType, exp, cookingTime, crafting);
    }

    public static Item toItem(ItemStack is) {
        Item item = null;
        if (is == null) return null;

        for (Item[] items : Item.items)
            for (Item i : items)
                if (is.equals(i.getItemStack()))
                    item = i;

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

    public AbilityType.Ability getAbility() {
        return ability;
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

    public void setGlint(boolean glint, ItemMeta meta) {
        isGlint = glint;

        NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
        Glow glow = new Glow(glowKey);

        if (isGlint)
            meta.addEnchant(glow, 1, true);
        else
            meta.removeEnchant(glow);
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
