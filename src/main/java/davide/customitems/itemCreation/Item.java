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
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Item {
    private final ItemStack itemStack;
    private final Color color;
    private final Type type;
    private final SubType subType;
    private final Rarity rarity;
    private final int damage;
    private final int critChance;
    private final int health;
    private final int defence;
    private final List<Ability> abilities;
    private final boolean showDelay;
    private final boolean showInGui;
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
        this.abilities = builder.abilities;
        this.damage = builder.damage;
        this.critChance = builder.critChance;
        this.health = builder.health;
        this.defence = builder.defence;
        this.showDelay = builder.showDelay;
        this.showInGui = builder.showInGui;
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
        short count = 0;
        if (lore != null) {
            //Defence
            if (defence != 0) {
                lore.add(0, "Defence: §c" + defence);
                count++;
            }
            container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence);

            //Health
            if (health != 0) {
                lore.add(0, "Health: §c" + health);
                count++;
            }
            container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);

            //Crit Chance
            if (critChance != 0) {
                lore.add(0, "Crit: §c" + critChance + "%");
                count++;
            }
            container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);

            //Damage
            if (damage != 0) {
                lore.add(0, "Damage: §c" + damage);
                count++;
            }
            container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
        }

        //Enchants
        if (lore != null && enchantments != null) {
            if (count > 0)
                lore.add(count, "");

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);

                String str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
                String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
                if (enchName.equals("Sweeping"))
                    enchName = "Sweeping edge";
                String lvl = entry.getValue().toString();

                if (count > 0)
                    lore.add(count + 1, "§9" + enchName + " " + lvl);
                else
                    lore.add(count, "§9" + enchName + " " + lvl);
            }
        }

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //Abilities
        int idx;
        if (enchantments != null && count > 0)
            idx = count + 3;
        else if (enchantments != null)
            idx = count + 2;
        else idx = count;

        if (lore != null)
            if (abilities != null)
                for (Ability ability : abilities) {

                    lore.add(idx, "");
                    idx++;
                    lore.add(idx, "§6Item Ability: " + ability.name());
                    idx++;

                    if (ability.type() != AbilityType.PASSIVE) {
                        lore.add(idx, "§e§l" + ability.type().getPrefix());
                        idx++;
                    }

                    for (int k = 0; k < ability.description().length; k++) {
                        lore.add(idx, ability.description()[k]);
                        idx++;
                    }

                    //Ability cooldown
                    if (ability.cooldown() > 0) {
                        if (ability.cooldown() < 60)
                            lore.add(idx, "§8§o" + ability.cooldown() + " sec cooldown");
                        else
                            lore.add(idx, "§8§o" + ability.cooldown() / 60 + " min cooldown");
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

        //More fancy spacing
        if (lore != null && rarity != null)
            lore.add("");

        if (lore != null && type == Type.MATERIAL)
            lore.add("§eRight click to see recipes");

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

    public static boolean isCustomItem(ItemStack is) {
        return toItem(is) != null;
    }

    /**
     * Converts the given ItemStack to a custom item
     * @param is itemStack to convert
     * @return the converted item or null if the ItemStack is not a custom item
     */
    public static Item toItem(@NotNull ItemStack is) {
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

        for (Item i : ItemList.utilsItems)
            if (container.has(i.getKey(), PersistentDataType.INTEGER))
                item = i;

        return item;
    }

    /**
     * Retrieve the custom item from its key, not recommended
     * @param key the namespacedKey of the custom item
     * @return the custom item matching the given key
     */
    public static Item toItem(@NotNull String key) {
        Item item = null;

        for (List<Item> items : ItemList.items)
            for (Item i : items)
                if (key.equalsIgnoreCase(i.getKey().getKey()))
                    item = i;

        for (Item i : ItemList.utilsItems)
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
        if (getDefence(is) != 0)
            count++;

        int index = count == 0 ? 0 : count + 1;
        boolean first = false;

        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (meta.getEnchants().isEmpty())
                first = true;

            meta.addEnchant(entry.getKey(), entry.getValue(), true);

            String str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
            String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
            if (enchName.equals("Sweeping"))
                enchName = "Sweeping edge";
            String lvl = entry.getValue().toString();

            if (first)
                lore.add(index, "§9" + enchName + " " + lvl);

            i++;
        }

        lore.add(index + i, "");
        setLore(is, lore);
    }

    public static void removeEnchantsFromLore(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        lore.removeIf(s -> s.startsWith("§9"));
        setLore(is, lore);
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

    public static void setStats(int damage, int critChance, int health, int defence, ItemStack is) {
        if (!Item.isCustomItem(is)) return;
        Reforge reforge = Reforge.getReforge(is);

        Item.removeStatsFromLore(is);

        if (reforge != null) {
            //Damage
            Item.setDamage(damage - reforge.getDamageModifier(), is, reforge);
            //Crit Chance
            Item.setCritChance(critChance - reforge.getCritChanceModifier(), is, reforge);
            //Health
            Item.setHealth(health - reforge.getHealthModifier(), is, reforge);
            //Defence
            Item.setDefence(defence - reforge.getDefenceModifier(), is, reforge);
        } else {
            //Damage
            Item.setDamage(damage, is);
            //Crit Chance
            Item.setCritChance(critChance, is);
            //Health
            Item.setHealth(health, is);
            //Defence
            Item.setDefence(defence, is);
        }
    }

    public static int getBaseDamage(ItemStack is, Reforge r) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER) - r.getDamageModifier();
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

        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
        is.setItemMeta(meta);

        if (getDamage(is) != 0)
            lore.add(0, "§7Damage: " + "§c" + damage);

        setLore(is, lore);
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
        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage + reforgeDamage);
        is.setItemMeta(meta);

        if (reforgeDamage == 0) {
            if (getDamage(is) != 0) {
                lore.add(0, "§7Damage: " + "§c" + damage);
                container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
                setLore(is, lore);
            }
            return;
        }

        if (getDamage(is) + reforgeDamage != 0) {
            if (reforgeDamage > 0)
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(+" + reforgeDamage + ")");
            else
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(" + reforgeDamage + ")");
        }

        setLore(is, lore);
    }

    public static int getBaseCritChance(ItemStack is, Reforge r) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER) - r.getCritChanceModifier();
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

        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;
        if (getCritChance(is) != 0)
            lore.add(i, "§7Crit: " + "§c" + critChance + "%");

        setLore(is, lore);
    }

    public static void setCritChance(int critChance, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int reforgeCrit = reforge.getCritChanceModifier();
        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance + reforgeCrit);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;

        if (reforgeCrit == 0) {
            if (getCritChance(is) > 0) {
                lore.add(i, "§7Crit: " + "§c" + critChance + "%");
                container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
                setLore(is, lore);
            }
            return;
        }

        if (getCritChance(is) + reforgeCrit != 0) {
            if (reforgeCrit > 0)
                lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(+" + reforgeCrit + "%)");
            else
                lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(" + reforgeCrit + "%)");
        }

        setLore(is, lore);
    }

    public static int getBaseHealth(ItemStack is, Reforge r) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER) - r.getHealthModifier();
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

        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;
        if (getCritChance(is) != 0)
            i++;

        if (getHealth(is) != 0)
            lore.add(i, "§7Health: " + "§c" + health);

        setLore(is, lore);
    }

    public static void setHealth(int health, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int reforgeHealth = reforge.getHealthModifier();
        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health + reforgeHealth);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;
        if (getCritChance(is) != 0)
            i++;

        if (reforgeHealth == 0) {
            if (getHealth(is) != 0) {
                lore.add(i, "§7Health: " + "§c" + health);
                container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);
                is.setItemMeta(meta);
                setLore(is, lore);
            }
            return;
        }

        if (getHealth(is) + reforgeHealth != 0) {
            if (reforgeHealth > 0)
                lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(+" + reforgeHealth + ")");
            else
                lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(" + reforgeHealth + ")");
        }

        setLore(is, lore);
    }

    public static int getBaseDefence(ItemStack is, Reforge r) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER) - r.getDefenceModifier();
    }

    public static int getDefence(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER);
    }

    public static void setDefence(int defence, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;
        if (getCritChance(is) != 0)
            i++;
        if (getHealth(is) != 0)
            i++;

        if (getDefence(is) != 0)
            lore.add(i, "§7Defence: " + "§c" + defence);

        setLore(is, lore);
    }

    public static void setDefence(int defence, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Item item = toItem(is);
        if (item == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        int reforgeDefence = reforge.getDefenceModifier();
        container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence + reforgeDefence);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) != 0)
            i++;
        if (getCritChance(is) != 0)
            i++;
        if (getHealth(is) != 0)
            i++;

        if (reforgeDefence == 0) {
            if (getDefence(is) != 0) {
                lore.add(i, "§7Defence: " + "§c" + defence);
                container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence);
                setLore(is, lore);
            }
            return;
        }

        if (getDefence(is) + reforgeDefence != 0) {
            if (reforgeDefence > 0)
                lore.add(i, "§7Defence: " + "§c" + (defence + reforgeDefence) + " §8(+" + reforgeDefence + ")");
            else
                lore.add(i, "§7Defence: " + "§c" + (defence + reforgeDefence) + " §8(" + reforgeDefence + ")");
        }

        setLore(is, lore);
    }

    public static void removeStatsFromLore(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        if (getDamage(is) != 0)
            lore.remove(0);
        if (getCritChance(is) != 0)
            lore.remove(0);
        if (getHealth(is) != 0)
            lore.remove(0);
        if (getDefence(is) != 0)
            lore.remove(0);

        setLore(is, lore);
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public boolean isShowDelay() {
        return showDelay;
    }

    public boolean isShowInGui() {
        return showInGui;
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

    public static void setLore(ItemStack item, String... lore) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return key.equals(item.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
