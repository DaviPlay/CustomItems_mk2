package davide.customitems.ItemCreation;

import davide.customitems.CustomItems;
import davide.customitems.API.Glow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {
    private ItemStack itemStack;
    private Type.ItemType type;
    private Rarity.ItemRarity rarity;
    private int delay;
    private boolean isGlint;
    private Enchantment[] enchantments;
    private int[] enchLevels;
    private List<ItemStack> crafting;
    private String name;
    private List<String> lore;

    public NamespacedKey key;

    public static final Item damageSword = new Item(new ItemStack(Material.WOODEN_SWORD), Type.ItemType.SWORD, Rarity.ItemRarity.TEST, new Enchantment[]{
            Enchantment.DAMAGE_ALL
    }, new int[]{
            10
    }, 0, Arrays.asList(
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

    public static final Item fastPick = new Item(new ItemStack(Material.WOODEN_PICKAXE), Type.ItemType.TOOL, Rarity.ItemRarity.TEST, new Enchantment[]{
            Enchantment.DIG_SPEED
    }, new int[]{
            10
    }, 0, Arrays.asList(
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            new ItemStack(Material.REDSTONE_BLOCK, 32),
            null,
            new ItemStack(Material.STICK),
            null,
            null,
            new ItemStack(Material.STICK),
            null), "Fast Pickaxe", "Goes brrr", "For real", "Just that...");

    public static final Item stonk = new Item(new ItemStack(Material.GOLDEN_PICKAXE), Type.ItemType.TOOL, Rarity.ItemRarity.EPIC, 120, null, "Stonk", "Mines really fucking fast");

    public static final Item explosiveWand = new Item(new ItemStack(Material.STICK), Type.ItemType.WAND, Rarity.ItemRarity.UNCOMMON, 5, null, "Explosive Wand", "goes BOOM...", "but no damage");


    public static Item[] items = {damageSword, fastPick, stonk, explosiveWand};

    public Item(ItemStack itemStack, Type.ItemType type, Rarity.ItemRarity rarity, Enchantment[] enchantments, int[] enchLevels, int delay, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.delay = delay;
        this.enchantments = enchantments;
        this.enchLevels = enchLevels;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    private Item(ItemStack itemStack, Type.ItemType type, Rarity.ItemRarity rarity, boolean isGlint, int delay, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.delay = delay;
        this.isGlint = isGlint;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, Type.ItemType type, Rarity.ItemRarity rarity, int delay, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
        this.delay = delay;
        this.crafting = crafting;
        this.name = name;
        this.lore = new LinkedList<>(Arrays.asList(lore));

        create();
    }

    public Item(ItemStack itemStack, Type.ItemType type, Rarity.ItemRarity rarity, List<ItemStack> crafting, String name, String... lore) {
        this.itemStack = itemStack;
        this.type = type;
        this.rarity = rarity;
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

        meta.setDisplayName(rarity.getColor() + name);

        //Fancy spacing
        if (enchantments != null && enchantments.length > 0)
            lore.add(0, "");

        //Making unmarked lore gray
        List<String> newLore = new ArrayList<>();
        for (String s : lore)
            if (!(s.startsWith("§"))) {
                s = "§7" + s;

                newLore.add(s);
            }

        if (newLore.size() > 0) setLore(newLore);

        lore.add("");

        if (rarity == Rarity.ItemRarity.TEST) {
            lore.add("§cThis item is a test and thus unfinished,");
            lore.add("§cit may not work as intended");
            lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
        } else
            lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.name());

        meta.setLore(lore);

        //Makes it glow without adding any enchantment
        if (isGlint) {
            NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
            Glow glow = new Glow(glowKey);
            meta.addEnchant(glow, 1 , true);
        }

        //Adding enchantments
        if (enchantments != null)
            for (Enchantment ench : enchantments)
                for (int enchLevel : enchLevels)
                    meta.addEnchant(ench, enchLevel, true);

        itemStack.setItemMeta(meta);

        //Recipe
        if (crafting != null)
            new Crafting(key, itemStack, crafting);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Type.ItemType getType() {
        return type;
    }

    public void setType(Type.ItemType type) {
        this.type = type;
    }

    public Rarity.ItemRarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity.ItemRarity rarity) {
        this.rarity = rarity;
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

    public void setGlint(boolean glint, ItemMeta meta) {
        isGlint = glint;

        NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
        Glow glow = new Glow(glowKey);

        if (isGlint)
            meta.addEnchant(glow, 1, true);
        else
            meta.removeEnchant(glow);
    }

    public Enchantment[] getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(Enchantment[] enchantments) {
        this.enchantments = enchantments;
    }

    public int[] getEnchLevels() {
        return enchLevels;
    }

    public void setEnchLevels(int[] enchLevels) {
        this.enchLevels = enchLevels;
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
