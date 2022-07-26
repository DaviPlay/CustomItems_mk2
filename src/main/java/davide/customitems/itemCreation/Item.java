package davide.customitems.itemCreation;

import davide.customitems.api.*;
import davide.customitems.crafting.Crafting;
import davide.customitems.crafting.CraftingType;
import davide.customitems.CustomItems;
import davide.customitems.itemCreation.builders.ItemBuilder;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Item {
    private final ItemStack itemStack;
    private final Color color;
    private final Type type;
    private final SubType subType;
    private final Rarity rarity;
    private final int damage;
    private final int health;
    private final int critChance;
    private final List<Ability> abilities;
    private int delay;
    private final boolean showDelay;
    private boolean isGlint;
    private final boolean hasRandomUUID;
    private final CraftingType craftingType;
    private final float exp;
    private final int cookingTime;
    private final HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private final String name;
    private List<String> lore;
    private NamespacedKey key;
    
    private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public Item(ItemBuilder builder) {
        this.itemStack = builder.itemStack;
        this.color = builder.color;
        this.type = builder.type;
        this.subType = builder.subType;
        this.rarity = builder.rarity;
        this.damage = builder.damage;
        this.abilities = builder.abilities;
        this.delay = builder.delay;
        this.health = builder.health;
        this.critChance = builder.critChance;
        this.showDelay = builder.showDelay;
        this.isGlint = builder.isGlint;
        this.hasRandomUUID = builder.hasRandomUUID;
        this.craftingType = builder.craftingType;
        this.exp = builder.exp;
        this.cookingTime = builder.cookingTime;
        this.enchantments = builder.enchantments;
        this.crafting = builder.crafting;
        this.name = builder.name;
        this.lore = builder.lore;

        create();
    }

    private void create() {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (name.charAt(0) == '§')
            key = new NamespacedKey(plugin, name.toLowerCase(Locale.ROOT).replace(" ", "_").replace(name.charAt(1), '§').replace("§", "").replace("\'", ""));
        else
            key = new NamespacedKey(plugin, name.toLowerCase(Locale.ROOT).replace(" ", "_").replace("\'", ""));

        //Binding a random uuid to the item
        if (hasRandomUUID)
            container.set(key, new UUIDDataType(), UUID.randomUUID());
        else
            container.set(key, PersistentDataType.INTEGER, 1);

        //Setting the name
        if (rarity != null)
            meta.setDisplayName(rarity.getColor() + name);
        else
            meta.setDisplayName(name);

        if (lore == null && rarity != null)
            lore = new ArrayList<>();

        //Stats
        int i, count = 0;
        //Damage
        if (lore != null && damage != 0) {
            lore.add(0, "Damage: §c" + damage);

            if (enchantments != null)
                lore.add(1, "");

            count++;
        }
        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);

        //Health
        if (lore != null && health != 0) {
            if (damage != 0)
                i = 1;
            else
                i = 0;

            lore.add(i, "Health: §c" + health);

            if (i == 0)
                if (enchantments != null)
                    lore.add(i + 1, "");

            count++;
        }
        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);

        //Crit Chance
        if (lore != null && critChance != 0) {
            if (damage != 0 && health != 0)
                i = 2;
            else if (damage != 0 ^ health != 0)
                i = 1;
            else
                i = 0;

            lore.add(i, "Crit: §c" + critChance + "%");

            if (i == 0)
                if (enchantments != null)
                    lore.add(i + 1, "");

            count++;
        }
        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);

        //Enchants
        int index = checkStats(count);

        if (lore != null && enchantments != null)
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);

                String str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
                String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
                if (enchName.equals("Sweeping"))
                    enchName = "Sweeping edge";
                String lvl = entry.getValue().toString();

                lore.add(index, "§9" + enchName + " " + lvl);
            }

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //Ability prefix
        String separator = "/s";
        int j;
        if (enchantments != null)
            j = index + 2;
        else if (count > 0)
            j = index - 1;
        else
            j = index;

        if (lore != null)
            if (abilities != null)
                for (Ability ability : abilities) {
                    lore.add(j, "§6Item Ability:");

                    if (ability != Ability.PASSIVE)
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

        if (lore != null && rarity != null)
            for (String s : lore) {
                if (!(s.startsWith("§")))
                    s = "§7" + s;

                newLore.add(s);
                lore = newLore;
            }

        //Adding the cooldown to the lore
        if (lore != null && showDelay)
            if (delay > 0) {
                if (delay < 60)
                    lore.add("§8§o" + delay + " sec cooldown");
                else
                    lore.add("§8§o" + delay / 60 + " min cooldown");
            }

        //More fancy spacing
        if (lore != null)
            lore.add("");

        //Rarity Suffix
        if (lore != null && rarity != null)
            if (rarity == Rarity.TEST) {
                lore.add("§cThis item is a test and thus unfinished,");
                lore.add("§cit may not work as intended");
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
            }
            else if (subType != null)
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + subType.name());
            else if (type != null)
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.name());

        meta.setLore(lore);

        if (meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
            meta.addItemFlags(ItemFlag.HIDE_DYE);
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        itemStack.setItemMeta(meta);

        //Makes it glow without adding any enchantment
        setGlint(isGlint, itemStack);

        //Recipe
        if (crafting != null)
            new Crafting(key, itemStack, craftingType, exp, cookingTime, crafting);
    }

    private int checkStats(int count) {
        int index;

        switch (count) {
            case 1:
                index = 2;
                break;
            case 2:
                index = 3;
                break;
            case 3:
                index = 4;
                break;

            default:
                index = 0;
        }

        return index;
    }

    public static boolean isCustomItem(ItemStack is) {
        return toItem(is) != null;
    }

    /**
     * Converts the given ItemStack to a custom item
     * @param is itemStack to convert
     * @return the converted item or null if the ItemStack is not a custom item
     */
    public static Item toItem(ItemStack is) {
        Item item = null;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (List<Item> items : ItemList.items)
            for (Item i : items) {
                if (i.hasRandomUUID) {
                    if (container.has(i.getKey(), new UUIDDataType()))
                        item = i;
                } else {
                    if (container.has(i.getKey(), PersistentDataType.INTEGER))
                        item = i;
                }
            }

        return item;
    }

    /**
     * Retrieve the custom item from its key, not recommended
     * @param key the namespacedKey of the custom item
     * @return the custom item matching the given key
     * @deprecated Not accurate <br>
     * Use {@link #toItem(ItemStack)} instead
     */
    @Deprecated
    public static Item toItem(String key) {
        Item item = null;

        for (List<Item> items : ItemList.items)
            for (Item i : items)
                if (key.equalsIgnoreCase(i.getKey().getKey()))
                    item = i;

        return item;
    }

    public static void addEnchantsToLore(Map<Enchantment, Integer> enchantments, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();

        int count = 0;
        if (getDamage(is) != 0)
            count++;
        if (getHealth(is) != 0)
            count++;
        if (getCritChance(is) != 0)
            count++;

        int index = item.checkStats(count);

        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);

            String str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
            String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
            if (enchName.equals("Sweeping"))
                enchName = "Sweeping edge";
            String lvl = entry.getValue().toString();

            lore.add(index, "§9" + enchName + " " + lvl);
            i++;
        }

        lore.add(index + i, "");

        if (item.getRarity() != null)
            if (item.getRarity() == Rarity.TEST) {
                lore.add("§cThis item is a test and thus unfinished,");
                lore.add("§cit may not work as intended");
                lore.add(item.getRarity().getColor() + "" + ChatColor.BOLD + item.getRarity().name() + " ITEM");
            }
            else if (item.getSubType() != null)
                lore.add(item.getRarity().getColor() + "" + ChatColor.BOLD + item.getRarity().name() + " " + item.getSubType().name());
            else if (item.getType() != null)
                lore.add(item.getRarity().getColor() + "" + ChatColor.BOLD + item.getRarity().name() + " " + item.getType().name());

        setLore(lore, is);
    }

    public static void removeEnchantsFromLore(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        lore.removeIf(s -> s.startsWith("§9"));
        setLore(lore, is);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStack(int n) {
        ItemStack is = itemStack.clone();
        is.setAmount(n);

        return is;
    }

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public SubType getSubType() {
        return subType;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public static int getDamage(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER);
    }

    public static void setDamage(int damage, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (getDamage(is) != 0)
            lore.set(0, "§7Damage: " + "§c" + damage);
        else
            lore.add(0, "§7Damage: " + "§c" + damage);

        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
        setLore(lore, is);
    }

    public static void setDamage(int damage, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        int reforgeDamage = reforge.getDamageModifier();

        if (reforgeDamage == 0) {
            if (getDamage(is) != 0) {
                lore.add(0, "§7Damage: " + "§c" + damage);
                container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
                setLore(lore, is);
            }
            return;
        }

        if (getDamage(is) != 0) {
            if (reforgeDamage > 0)
                lore.set(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(+" + reforgeDamage + ")");
            else
                lore.set(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(" + reforgeDamage + ")");
        } else {
            if (reforgeDamage > 0)
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(+" + reforgeDamage + ")");
            else
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(" + reforgeDamage + ")");
        }

        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage + reforgeDamage);
        setLore(lore, is);
    }

    public static int getHealth(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER);
    }

    public static void setHealth(int health, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0)
            i = 1;
        else
            i = 0;

        if (getHealth(is) != 0)
            lore.set(i, "§7Health: " + "§c" + health);
        else
            lore.add(i, "§7Health: " + "§c" + health);

        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);
        setLore(lore, is);
    }

    public static void setHealth(int health, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0)
            i = 1;
        else
            i = 0;

        int reforgeHealth = reforge.getHealthModifier();

        if (reforgeHealth == 0) {
            if (getHealth(is) != 0) {
                lore.add(i, "§7Health: " + "§c" + health);
                container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);
                setLore(lore, is);
            }
            return;
        }

        if (getHealth(is) != 0) {
            if (reforgeHealth > 0)
                lore.set(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(+" + reforgeHealth + ")");
            else
                lore.set(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(" + reforgeHealth + ")");
        } else {
            if (reforgeHealth > 0)
                lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(+" + reforgeHealth + ")");
            else
                lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(" + reforgeHealth + ")");
        }

        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health + reforgeHealth);
        setLore(lore, is);
    }

    public static int getCritChance(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER);
    }

    public static void setCritChance(int critChance, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0 && getHealth(is) != 0)
            i = 2;
        else if (getDamage(is) != 0 ^ getHealth(is) != 0)
            i = 1;
        else
            i = 0;

        if (getCritChance(is) != 0)
            lore.set(i, "§7Crit: " + "§c" + critChance + "%");
        else
            lore.add(i, "§7Crit: " + "§c" + critChance + "%");

        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
        setLore(lore, is);
    }

    public static void setCritChance(int critChance, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0 && getHealth(is) != 0)
            i = 2;
        else if (getDamage(is) != 0 ^ getHealth(is) != 0)
            i = 1;
        else
            i = 0;

        int reforgeCrit = reforge.getCritChanceModifier();

        if (reforgeCrit == 0) {
            if (getCritChance(is) > 0) {
                lore.add(i, "§7Crit: " + "§c" + critChance + "%");
                container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
                setLore(lore, is);
            }
            return;
        }

        if (getCritChance(is) != 0) {
            if (reforgeCrit > 0)
                lore.set(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(+" + reforgeCrit + "%)");
            else
                lore.set(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(" + reforgeCrit + "%)");
        } else {
            if (reforgeCrit > 0)
                lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(+" + reforgeCrit + "%)");
            else
                lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(" + reforgeCrit + "%)");
        }

        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance + reforgeCrit);
        setLore(lore, is);
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isShowDelay() {
        return showDelay;
    }

    public boolean isGlint() {
        return isGlint;
    }

    public void setGlint(boolean glint, ItemStack item) {
        isGlint = glint;
        assert item.getItemMeta() != null;

        NamespacedKey glowKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), CustomItems.getPlugin(CustomItems.class).getDescription().getName());
        Glow glow = new Glow(glowKey);

        if (isGlint)
            item.addUnsafeEnchantment(glow, 1);
        else
            item.removeEnchantment(glow);
    }

    public boolean hasRandomUUID() {
        return hasRandomUUID;
    }

    public static void setRandomUUID(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Item item = toItem(is);
        if (item == null) return;

        container.set(item.getKey(), new UUIDDataType(), UUID.randomUUID());
        is.setItemMeta(meta);
    }

    public UUID getRandomUUID(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Item item = toItem(is);
        if (item == null) return null;

        return container.get(item.getKey(), new UUIDDataType());
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public float getExp() {
        return exp;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public List<ItemStack> getCrafting() {
        return crafting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
    }

    public List<String> getLore() {
        return lore;
    }

    public static void setLore(List<String> lore, ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public NamespacedKey getKey() {
        return key;
    }
}
