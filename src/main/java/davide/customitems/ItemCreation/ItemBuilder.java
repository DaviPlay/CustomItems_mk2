package davide.customitems.ItemCreation;

import davide.customitems.Crafting.CraftingType;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class ItemBuilder {
    ItemStack itemStack;
    Color color;
    Type type;
    SubType subType;
    Rarity rarity;
    int damage;
    int health;
    int critChance;
    List<Ability> abilities;
    int delay;
    boolean showDelay = true;
    boolean isGlint;
    boolean isStackable;
    boolean hasRandomUUID;
    CraftingType craftingType;
    float exp;
    int cookingTime;
    HashMap<Enchantment, Integer> enchantments;
    List<ItemStack> crafting;
    String name;
    List<String> lore;

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

    public ItemBuilder health(int health) {
        this.health = health;
        return this;
    }

    public ItemBuilder critChance(int critChance) {
        this.critChance = critChance;
        return this;
    }

    public ItemBuilder isStackable(boolean isStackable) {
        this.isStackable = isStackable;
        return this;
    }

    public ItemBuilder abilities(Ability... abilities) {
        this.abilities = new ArrayList<>(Arrays.asList(abilities));
        return this;
    }

    public ItemBuilder delay(int delay) {
        this.delay = delay;
        return this;
    }

    public ItemBuilder showDelay(boolean showDelay) {
        this.showDelay = showDelay;
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

    public ItemBuilder craftingType(CraftingType craftingType) {
        this.craftingType = craftingType;
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

    public ItemBuilder lore(String... lore) {
        this.lore = new LinkedList<>(Arrays.asList(lore));
        return this;
    }

    public Item build() {
        Item item = new Item(this);
        validateItem(item);
        return item;
    }

    private void validateItem(Item item) {
        if (item.getCrafting() != null && item.getCraftingType() == null)
            throw new IllegalArgumentException("The crafting recipe must have a type");

        if (item.getCrafting() == null && item.getCraftingType() != null)
            throw new IllegalArgumentException("The item must have a crafting recipe for it to have a specified type");

        if (item.getCraftingType() == CraftingType.FURNACE)
            if (item.getExp() <= 0 || item.getCookingTime() <= 0)
                throw new IllegalArgumentException("The furnace crafting type must have a specified exp gain and cooking time");

        if (item.isGlint() && item.getEnchantments() != null)
            throw new IllegalArgumentException("The item cannot have enchantments if it's been tagged as 'isGlint'");

        if (item.getAbilities() == null && item.getDelay() > 0)
            throw new IllegalArgumentException("The item must have at least 1 ability for it to have a delay");

        if (!(item.getItemStack().getItemMeta() instanceof LeatherArmorMeta) && color != null)
            throw new IllegalArgumentException("The item must be leather armor for it to have a specified color");
    }
}
