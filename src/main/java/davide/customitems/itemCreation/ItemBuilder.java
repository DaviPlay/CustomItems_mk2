package davide.customitems.itemCreation;

import davide.customitems.crafting.CraftingType;
import davide.customitems.lists.ItemList;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class ItemBuilder {
    protected ItemStack itemStack;
    protected Color color;
    protected Type type;
    protected SubType subType;
    protected Rarity rarity;
    protected int damage = 1;
    protected float attackSpeed = 0;
    protected int critChance = 1;
    protected float critDamage;
    protected int health;
    protected int defence;
    protected List<Ability> abilities;
    protected boolean showInGui = true;
    protected boolean isGlint;
    protected boolean hasRandomUUID;
    protected CraftingType craftingType;
    protected HashMap<Double, List<EntityType>> entityDrops;
    protected HashMap<Double, List<Material>> blockDrops;
    protected float exp;
    protected int cookingTime;
    protected HashMap<Enchantment, Integer> enchantments;
    protected List<ItemStack> crafting;
    protected String name;
    protected List<String> lore;
    protected List<String> addInfo;

    protected boolean addToList = true;

    public ItemBuilder(ItemStack itemStack, String name, boolean addToList) {
        this.itemStack = itemStack;
        this.name = name;
        this.addToList = addToList;
    }

    public ItemBuilder(ItemStack itemStack, String name) {
        this.itemStack = itemStack;
        this.name = name;
    }

    public ItemBuilder color(Color color) {
        this.color = color;
        return this;
    }

    public ItemBuilder type(Type type) {
        this.type = type;
        return this;
    }

    public ItemBuilder subType(SubType subType) {
        this.subType = subType;
        return this;
    }

    public ItemBuilder rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public ItemBuilder damage(int damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder attackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
        return this;
    }

    public ItemBuilder critChance(int critChance) {
        this.critChance = critChance;
        return this;
    }

    public ItemBuilder critDamage(float critDamage) {
        this.critDamage = critDamage;
        return this;
    }

    public ItemBuilder health(int health) {
        this.health = health;
        return this;
    }

    public ItemBuilder defence(int defence) {
        this.defence = defence;
        return this;
    }

    public ItemBuilder abilities(Ability... abilities) {
        this.abilities = new ArrayList<>(Arrays.asList(abilities));
        return this;
    }

    public ItemBuilder showInGui(boolean showInGui) {
        this.showInGui = showInGui;
        return this;
    }

    public ItemBuilder isGlint(boolean isGlint) {
        this.isGlint = isGlint;
        return this;
    }

    public ItemBuilder hasRandomUUID(boolean hasRandomUUID) {
        this.hasRandomUUID = hasRandomUUID;
        return this;
    }

    /**
     * <b>DO NOT USE THE SHAPELESS CRAFTING TYPE WITH CUSTOM ITEMS</b>
     * @param craftingType default SHAPED
    */
    public ItemBuilder craftingType(CraftingType craftingType) {
        this.craftingType = craftingType;
        return this;
    }

    public ItemBuilder entityDrops(HashMap<Double, List<EntityType>> entityDrops) {
        this.entityDrops = entityDrops;
        return this;
    }

    public ItemBuilder blockDrops(HashMap<Double, List<Material>> blockDrops) {
        this.blockDrops = blockDrops;
        return this;
    }

    public ItemBuilder exp(float exp) {
        this.exp = exp;
        return this;
    }

    public ItemBuilder cookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
        return this;
    }

    public ItemBuilder enchantments(HashMap<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder crafting(List<ItemStack> crafting) {
        this.crafting = crafting;
        return this;
    }

    /**
     * Every string is a line, so passing 2 string
     * will make the lore 2 lines long. <br>
     * @param lore the lore of the items
     */
    public ItemBuilder lore(String... lore) {
        this.lore = new LinkedList<>(Arrays.asList(lore));
        return this;
    }

    /**
     * Adds useful information when toggled in implemented inventories
     * @param addInfo additional lore lines to be added only in pertinent GUIs
     */
    public ItemBuilder addInfo(String... addInfo) {
        List<String> newDesc = new ArrayList<>();

        if (addInfo != null)
            for (String s : addInfo) {
                if (!(s.startsWith("§")))
                    s = "§8" + s;

                newDesc.add(s);
                addInfo = newDesc.toArray(new String[]{});
            }

        assert addInfo != null;
        this.addInfo = new LinkedList<>(Arrays.asList(addInfo));
        return this;
    }

    public Item build() {
        Item item = new Item(this);

        if (addToList) {
            if (ItemList.items.isEmpty())
                ItemList.items.add(new ArrayList<>());

            ItemList.items.get(0).add(item);
        }

        validateItem(item);
        return item;
    }

    public void validateItem(Item item) {
        if (item.getCrafting() != null && item.getCraftingType() == null)
            throw new IllegalArgumentException("The crafting recipe must have a type");

        if (item.getCrafting() == null && item.getCraftingType() != null && (item.getCraftingType() != CraftingType.NONE && item.getCraftingType() != CraftingType.DROP))
            throw new IllegalArgumentException("The item must have a crafting recipe for it to have a specified type");

        if ((item.getEntityDropChances() == null && item.getBlockDropChances() == null) && item.getCraftingType() == CraftingType.DROP)
            throw new IllegalArgumentException("The item must have a specified entity to drop itself from");

        if (item.getCraftingType() == CraftingType.FURNACE)
            if (item.getExp() <= 0 || item.getCookingTime() <= 0)
                throw new IllegalArgumentException("The furnace crafting type must have a specified exp gain and cooking time");

        if (item.isGlint() && item.getEnchantments() != null)
            throw new IllegalArgumentException("The item cannot have enchantments if it's been tagged as 'isGlint'");

        if (!(item.getItemStack().getItemMeta() instanceof LeatherArmorMeta) && color != null)
            throw new IllegalArgumentException("The item must be leather armor for it to have a specified color");
    }

    //private String normalize(String s) {
    //    return s.substring(0, 1).toUpperCase() + s.substring(1).replace('_', ' ').substring(s.indexOf(" "), s.indexOf(" ") + 1).toUpperCase();
    //}
}
