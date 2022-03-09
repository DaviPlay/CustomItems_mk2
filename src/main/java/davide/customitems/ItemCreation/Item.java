package davide.customitems.ItemCreation;

import davide.customitems.API.AbilityType;
import davide.customitems.API.ItemRarity;
import davide.customitems.API.ItemType;
import davide.customitems.CustomItems;
import davide.customitems.API.Glow;
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
    private boolean isStackable;
    private HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private String name;
    private List<String> lore;

    public NamespacedKey key;

    //Cool items
    public static final Item damageSword = new Item(new ItemStack(Material.WOODEN_SWORD), ItemType.Type.SWORD, ItemRarity.Rarity.TEST, null, 0, new HashMap<Enchantment, Integer>(){{
        put(Enchantment.DAMAGE_ALL, 10);
        put(Enchantment.LUCK, 10);
    }}, Arrays.asList(
            null,
            new ItemStack(Material.GOLD_BLOCK, 32),
            null,
            null,
            new ItemStack(Material.GOLD_BLOCK),
            null,
            null,
            new ItemStack(Material.STICK),
            null
    ), "Damage Sword", "Goes bonk", "Really", "Not kidding...");

    public static final Item fastPick = new Item(new ItemStack(Material.WOODEN_PICKAXE), ItemType.Type.TOOL, ItemRarity.Rarity.TEST, null, 0, new HashMap<Enchantment, Integer>(){{put(Enchantment.DIG_SPEED, 10);}}, Arrays.asList(
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            null,
            new ItemStack(Material.STICK),
            null,
            null,
            new ItemStack(Material.STICK),
            null
    ), "Fast Pickaxe", "Goes brrr", "For real", "Just that...");

    public static final Item stonk = new Item(new ItemStack(Material.GOLDEN_PICKAXE), ItemType.Type.TOOL, ItemRarity.Rarity.EPIC, AbilityType.Ability.RIGHT_CLICK, 120, null, Arrays.asList(
            new ItemStack(Material.GOLD_BLOCK, 64),
            new ItemStack(Material.GOLD_BLOCK, 64),
            new ItemStack(Material.GOLD_BLOCK, 64),
            null,
            new ItemStack(Material.STICK),
            null,
            null,
            new ItemStack(Material.STICK),
            null
    ), "Stonk", "Mines really fucking fast");

    public static final Item explosiveWand = new Item(new ItemStack(Material.STICK), ItemType.Type.WAND, ItemRarity.Rarity.UNCOMMON, AbilityType.Ability.RIGHT_CLICK, true, 5, true, Arrays.asList(
            null,
            null,
            new ItemStack(Material.TNT),
            null,
            new ItemStack(Material.STICK),
            null,
            new ItemStack(Material.STICK),
            null,
            null
    ), "Explosive Wand", "goes BOOM...", "but no damage");

    //Utils items
    public static final Item fillerGlass = new Item(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), null, null, null, true, null, " ");

    //Items array
    public static Item[] items = {damageSword, fastPick, stonk, explosiveWand};

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, int delay, boolean isStackable, HashMap<Enchantment, Integer> enchantments, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isStackable = isStackable;
        this.crafting = crafting;
        this.enchantments = enchantments;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, int delay, HashMap<Enchantment, Integer> enchantments, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.crafting = crafting;
        this.enchantments = enchantments;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, boolean isGlint, int delay, boolean isStackable, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isGlint = isGlint;
        this.isStackable = isStackable;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, int delay, boolean isStackable, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.delay = delay;
        this.isStackable = isStackable;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, ItemType.Type type, ItemRarity.Rarity rarity, AbilityType.Ability ability, boolean isStackable, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.ability = ability;
        this.isStackable = isStackable;
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
        if (ability != null) {
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
            lore.add("§8" + delay + " sec cooldown");

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
            new Crafting(key, itemStack, crafting);
    }

    public static Item toItem(ItemStack is) {
        Item item = null;
        if (is == null) return null;

        for (Item i : Item.items)
            if (is.equals(i.getItemStack()))
                item = i;

        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
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
}
