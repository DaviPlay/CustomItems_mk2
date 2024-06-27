package davide.customitems.itemCreation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import davide.customitems.api.*;
import davide.customitems.crafting.Crafting;
import davide.customitems.crafting.CraftingType;
import davide.customitems.CustomItems;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Item {
    private final ItemStack itemStack;
    private final Color color;
    private final Type type;
    private final SubType subType;
    private final Rarity rarity;
    private final int damage;
    private final float attackSpeed;
    private final int critChance;
    private final float critDamage;
    private final int health;
    private final int defence;
    private final List<Ability> abilities;
    private final boolean showInGui;
    private boolean isGlint;
    private final boolean hasRandomUUID;
    private final CraftingType craftingType;
    private final HashMap<Double, List<EntityType>> entityDrops;
    private final HashMap<Double, List<Material>> blockDrops;
    private final float exp;
    private final int cookingTime;
    private final HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private final String name;
    private List<String> lore;
    private List<String> addInfo;
    private NamespacedKey key;
    private final boolean addToList;
    
    private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public Item(ItemBuilder builder) {
        this.itemStack = builder.itemStack;
        this.color = builder.color;
        this.type = builder.type;
        this.subType = builder.subType;
        this.rarity = builder.rarity;
        this.abilities = builder.abilities;
        this.damage = builder.damage;
        this.attackSpeed = builder.attackSpeed;
        this.critChance = builder.critChance;
        this.critDamage = builder.critDamage;
        this.health = builder.health;
        this.defence = builder.defence;
        this.showInGui = builder.showInGui;
        this.isGlint = builder.isGlint;
        this.hasRandomUUID = builder.hasRandomUUID;
        this.craftingType = builder.craftingType;
        this.entityDrops = builder.entityDrops;
        this.blockDrops = builder.blockDrops;
        this.exp = builder.exp;
        this.cookingTime = builder.cookingTime;
        this.enchantments = builder.enchantments;
        this.crafting = builder.crafting;
        this.name = builder.name;
        this.lore = builder.lore;
        this.addInfo = builder.addInfo;

        this.addToList = builder.addToList;
        create();
    }

    private void create() {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (addToList) {
            if (name.charAt(0) == '§')
                key = new NamespacedKey(plugin, Utils.normalizeKey(name).replaceFirst(String.valueOf(name.charAt(1)), "§").replace("§", ""));
            else
                key = new NamespacedKey(plugin, Utils.normalizeKey(name));

            if (plugin.getConfig().get(key.getKey()) != null && !plugin.getConfig().getBoolean(key.getKey())) {
                try {
                    if (type == Type.MATERIAL)
                        ItemList.items.get(1).remove(this);
                    else
                        ItemList.items.get(0).remove(this);
                } catch (IndexOutOfBoundsException e) {
                    return;
                }

                return;
            }
        }

        //Binding a random uuid to the item
        if (addToList) {
            if (hasRandomUUID)
                container.set(key, new UUIDDataType(), UUID.randomUUID());
            else
                container.set(key, PersistentDataType.INTEGER, 1);
        }

        //Attack speed modifier
        if (addToList && attackSpeed != 0) {
            Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
            attributes.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.attack_speed", attackSpeed, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.HAND));
            meta.setAttributeModifiers(attributes);
        }

        if (addToList && rarity != null)
            container.set(new NamespacedKey(plugin, "rarity"), PersistentDataType.STRING, rarity.name());

        //Setting the name
        if (rarity != null)
            meta.setDisplayName(rarity.getColor() + name);
        else
            meta.setDisplayName(name);

        if (lore == null && rarity != null)
            lore = new ArrayList<>();

        if (lore != null && !lore.isEmpty())
            lore.add(0, "");

        //Making unmarked description gray
        List<String> newDesc = new ArrayList<>();
        if (lore != null && rarity != null)
            for (String s : lore) {
                if (!(s.startsWith("§")))
                    s = "§8§o" + s;

                newDesc.add(s);
                lore = newDesc;
            }

        //Stats
        short count = 0;
        if (lore != null) {
            //Defence
            if (defence != 0) {
                lore.add(0, "§7Defence: §c" + defence);
                count++;
            }
            container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence);

            //Health
            if (health != 0) {
                lore.add(0, "§7Health: §c" + health);
                count++;
            }
            container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);

            //Crit Damage Multiplier
            if (critDamage > 1) {
                lore.add(0, "§7Crit Damage: §c" + Utils.trimFloatZeros(critDamage) + "x");
                count++;
            }
            container.set(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT, critDamage);

            //Crit Chance
            if (critChance > 1) {
                lore.add(0, "§7Crit Chance: §c" + critChance + "%");
                count++;
            }
            container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);

            //Damage
            if (damage > 1) {
                lore.add(0, "§7Damage: §c" + damage);
                count++;
            }
            container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
        }

        //Enchants
        if (lore != null && (enchantments != null && !enchantments.isEmpty())) {
            lore.add(count, "");

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);

                String str = entry.getKey().getKey().toString().replace("minecraft:", "").replace("_", " ");
                String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
                if (enchName.equals("Sweeping"))
                    enchName = "Sweeping edge";
                String lvl = entry.getValue().toString();

                lore.add(count + 1, "§9" + enchName + " " + lvl);
            }
        }

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        //Abilities
        int idx;
        if (enchantments != null && !enchantments.isEmpty() && count > 0)
            idx = count + enchantments.size() + 1;
        else if (enchantments != null && enchantments.size() > 1)
            idx = enchantments.size() + 1;
        else
            idx = count;

        if (lore != null)
            if (abilities != null && !abilities.isEmpty())
                for (Ability ability : abilities) {
                    if (ability.name().equals("_")) continue;

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
                    if (ability.cooldown() > 0 && ability.showDelay()) {
                        if (ability.cooldown() < 60)
                            lore.add(idx, "§8§o" + ability.cooldown() + " sec cooldown");
                        else
                            lore.add(idx, "§8§o" + ability.cooldown() / 60 + " min cooldown");
                    }
                }

        //Making unmarked lines in lore gray
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
                if (type == Type.MATERIAL) lore.add("");
                lore.add("§cThis item is a test and thus unfinished,");
                lore.add("§cit may not work as intended");
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
            }
            else if (subType != null)
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + subType.name().replace("_", " "));
            else
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.name().replace("_", " "));

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
        if (addToList && craftingType != null && craftingType != CraftingType.NONE && crafting != null)
            new Crafting(key, itemStack, craftingType, exp, cookingTime, crafting);
    }

    public static boolean isCustomItem(ItemStack is) {
        return toItem(is) != null;
    }

    public static boolean isCustomItem(String key) {
        return toItem(key) != null;
    }

    /**
     * Converts the given ItemStack to a custom item
     * @param is itemStack to convert
     * @return the converted item or null if the ItemStack is not a custom item
     */
    @Nullable
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

        if (item == null)
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
    @Nullable
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

    public static void addEnchantsToLore(Map<Enchantment, Integer> enchantments, @NotNull ItemStack is, boolean firstMulti) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();

        int count = 0;
        if (getDamage(is) > 1)
            count++;
        if (getCritChance(is) > 1)
            count++;
        if (getCritDamage(is) > 1)
            count++;
        if (getHealth(is) != 0)
            count++;
        if (getDefence(is) != 0)
            count++;

        if (meta.getEnchants().isEmpty()) return;
        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (entry.getKey() == Enchantment.getByKey(new NamespacedKey(plugin, plugin.getDescription().getName())) || entry.getKey() == Enchantment.LURE)
                continue;

            if (((meta.getEnchants().size() < 2 && i == 0) || (firstMulti && i == 0)))
                lore.add(count + i, "");

            meta.addEnchant(entry.getKey(), entry.getValue(), true);

            String enchName = entry.getKey().getKey().getKey().replace("_", " ");
            enchName = enchName.substring(0, 1).toUpperCase(Locale.ROOT) + enchName.substring(1);
            if (enchName.equals("Sweeping"))
                enchName = "Sweeping edge";
            String lvl = entry.getValue().toString();

            lore.add(count, "§9" + enchName + " " + lvl);
            i++;
        }
        lore.add(count, "");

        meta.setLore(lore);
        is.setItemMeta(meta);
    }

    public static void removeEnchantsFromLore(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        int count = 0;
        if (getDamage(is) > 1)
            count++;
        if (getCritChance(is) > 1)
            count++;
        if (getCritDamage(is) > 1)
            count++;
        if (getHealth(is) != 0)
            count++;
        if (getDefence(is) != 0)
            count++;

        lore.remove(count);

        Map<Enchantment, Integer> enchants = meta.getEnchants();
        if (enchants.isEmpty()) return;
        for (Enchantment e : enchants.keySet()) {
            if (e == Enchantment.LURE) continue;
            String str = e.getKey().getKey().replace("_", " ");
            String enchName = str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
            lore.removeIf(s -> s.contains(enchName));
        }

        meta.setLore(lore);
        is.setItemMeta(meta);
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
        return type == null ? subType.getType() : type;
    }

    public SubType getSubType() {
        return subType;
    }

    public static Rarity getRarity(ItemStack is) {
        if (is == null) return null;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "rarity"), PersistentDataType.STRING))
            return null;

        return Rarity.valueOf(container.get(new NamespacedKey(plugin, "rarity"), PersistentDataType.STRING));
    }

    public static void setRarity(ItemStack is, Rarity rarity) {
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null || rarity == null) return;

        meta.setDisplayName(rarity.getColor() + Item.getName(is));

        if (rarity == Rarity.TEST) {
            if (item.getType() == Type.MATERIAL) lore.add("");
            lore.add(lore.size() - 1, "§cThis item is a test and thus unfinished,");
            lore.add(lore.size() - 1, "§cit may not work as intended");
            lore.set(lore.size() - 3, rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
        }
        else if (item.getSubType() != null)
            lore.set(lore.size() - 1, rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + item.getSubType().name());
        else
            lore.set(lore.size() - 1, rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + item.getType().name());

        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "rarity"), PersistentDataType.STRING, rarity.name());
        is.setItemMeta(meta);
    }

    public static void setStats(int damage, int critChance, float critDamage, int health, int defence, ItemStack is, boolean keepReforgeDamage) {
        if (!Item.isCustomItem(is)) return;
        Reforge reforge = Reforge.getReforge(is);

        Item.removeStatsFromLore(is);

        if (reforge != null) {
            if (keepReforgeDamage) {
                //Damage
                Item.setDamage(damage, is, reforge);
                //Crit Chance
                Item.setCritChance(critChance, is, reforge);
                //Crit Damage
                Item.setCritDamage(critDamage, is, reforge);
                //Health
                Item.setHealth(health, is, reforge);
                //Defence
                Item.setDefence(defence, is, reforge);
            } else {
                //Damage
                Item.setDamage(damage - Reforge.getDamageModifier(is, reforge), is, reforge);
                //Crit Chance
                Item.setCritChance(critChance - Reforge.getCritChanceModifier(is, reforge), is, reforge);
                //Crit Damage
                Item.setCritDamage(critDamage - Reforge.getCritDamageModifier(is, reforge), is, reforge);
                //Health
                Item.setHealth(health - Reforge.getHealthModifier(is, reforge), is, reforge);
                //Defence
                Item.setDefence(defence - Reforge.getDefenceModifier(is, reforge), is, reforge);
            }
        } else {
            //Damage
            Item.setDamage(damage, is);
            //Crit Chance
            Item.setCritChance(critChance, is);
            //Crit Damage
            Item.setCritDamage(critDamage, is);
            //Health
            Item.setHealth(health, is);
            //Defence
            Item.setDefence(defence, is);
        }
    }

    public static int getBaseDamage(ItemStack is, Reforge r) {
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER) - Reforge.getDamageModifier(is, r);
    }

    public static int getDamage(ItemStack is) {
        if (is == null) return 0;
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

        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
        is.setItemMeta(meta);

        if (getDamage(is) > 1)
            lore.add(0, "§7Damage: " + "§c" + damage);

        setLore(is, lore);
    }

    public static void setDamage(int damage, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;

        int reforgeDamage = Reforge.getDamageModifier(is, reforge);
        container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage + reforgeDamage);
        is.setItemMeta(meta);

        if (reforgeDamage == 0) {
            if (getDamage(is) > 1) {
                lore.add(0, "§7Damage: " + "§c" + damage);
                container.set(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER, damage);
                setLore(is, lore);
            }
            return;
        }

        if (getDamage(is) > 1) {
            if (reforgeDamage > 0)
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(+" + reforgeDamage + ")");
            else
                lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(" + reforgeDamage + ")");
        }

        setLore(is, lore);
    }

    public static int getBaseCritChance(ItemStack is, Reforge r) {
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER) - Reforge.getCritChanceModifier(is, r);
    }

    public static int getCritChance(ItemStack is) {
        if (is == null) return 0;
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

        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;

        if (getCritChance(is) > 1)
            lore.add(i, "§7Crit Chance: " + "§c" + critChance + "%");

        setLore(is, lore);
    }

    public static void setCritChance(int critChance, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;

        int reforgeCrit = Reforge.getCritChanceModifier(is, reforge);
        container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance + reforgeCrit);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;

        if (reforgeCrit == 0) {
            container.set(new NamespacedKey(plugin, "crit_chance"), PersistentDataType.INTEGER, critChance);
            if (getCritChance(is) > 1) {
                lore.add(i, "§7Crit Chance: " + "§c" + critChance + "%");
                setLore(is, lore);
            }
            return;
        }

        if (getCritChance(is) + reforgeCrit > 1) {
            if (reforgeCrit > 0)
                lore.add(i, "§7Crit Chance: " + "§c" + (critChance + reforgeCrit) + "% §8(+" + reforgeCrit + "%)");
            else
                lore.add(i, "§7Crit Chance: " + "§c" + (critChance + reforgeCrit) + "% §8(" + reforgeCrit + "%)");
        }

        setLore(is, lore);
    }

    public static float getBaseCritDamage(ItemStack is, Reforge r) {
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT))
            return 0;

        return container.get(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT) - Reforge.getCritDamageModifier(is, r);
    }

    public static float getCritDamage(ItemStack is) {
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT))
            return 0;

        return container.get(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT);
    }

    public static void setCritDamage(float critDamage, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;

        container.set(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT, critDamage);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;

        if (getCritDamage(is) > 1)
            lore.add(i, "§7Crit Damage: §c" + Utils.trimFloatZeros(critDamage) + "x");

        setLore(is, lore);
    }

    public static void setCritDamage(float critDamage, ItemStack is, Reforge reforge) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;

        float reforgeCritDamage = Reforge.getCritDamageModifier(is, reforge);
        container.set(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT, critDamage + reforgeCritDamage);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;

        if (reforgeCritDamage == 0) {
            container.set(new NamespacedKey(plugin, "crit_damage"), PersistentDataType.FLOAT, critDamage);
            if (getCritDamage(is) > 1) {
                lore.add(i, "§7Crit Damage: §c" + Utils.trimFloatZeros(critDamage) + "x");
                is.setItemMeta(meta);
                setLore(is, lore);
            }
            return;
        }

        if (getCritDamage(is) + reforgeCritDamage > 1) {
            if (reforgeCritDamage > 1)
                lore.add(i, "§7Crit Damage: §c" + Utils.trimFloatZeros(critDamage + reforgeCritDamage) + "x §8(+" + Utils.trimFloatZeros(reforgeCritDamage) + "x)");
            else
                lore.add(i, "§7Crit Damage: §c" + Utils.trimFloatZeros(critDamage + reforgeCritDamage) + "x §8(" + Utils.trimFloatZeros(reforgeCritDamage) + "x)");
        }

        setLore(is, lore);
    }

    public static int getBaseHealth(ItemStack is, Reforge r) {
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER) - Reforge.getHealthModifier(is, r);
    }

    public static int getHealth(ItemStack is) {
        if (is == null) return 0;
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

        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;
        if (getCritDamage(is) > 1)
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

        int reforgeHealth = Reforge.getHealthModifier(is, reforge);
        container.set(new NamespacedKey(plugin, "health"), PersistentDataType.INTEGER, health + reforgeHealth);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;
        if (getCritDamage(is) > 1)
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
        if (is == null) return 0;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER))
            return 0;

        return container.get(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER) - Reforge.getDefenceModifier(is, r);
    }

    public static int getDefence(ItemStack is) {
        if (is == null) return 0;
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

        container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;
        if (getCritDamage(is) > 1)
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
        List<String> lore = meta.getLore();
        if (lore == null) return;

        int reforgeDefence = Reforge.getDefenceModifier(is, reforge);
        container.set(new NamespacedKey(plugin, "defence"), PersistentDataType.INTEGER, defence + reforgeDefence);
        is.setItemMeta(meta);

        int i = 0;
        if (getDamage(is) > 1)
            i++;
        if (getCritChance(is) > 1)
            i++;
        if (getCritDamage(is) > 1)
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

        if (getDamage(is) > 1)
            lore.remove(0);
        if (getCritChance(is) > 1)
            lore.remove(0);
        if (getCritDamage(is) > 1)
            lore.remove(0);
        if (getHealth(is) != 0)
            lore.remove(0);
        if (getDefence(is) != 0)
            lore.remove(0);

        setLore(is, lore);
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public List<Ability> getAbilities() {
        return abilities;
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

        if (Bukkit.getBukkitVersion().contains("1.20.4") || Bukkit.getBukkitVersion().contains("1.20.6") || Bukkit.getBukkitVersion().contains("1.21")) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "glint");

            container.set(key, PersistentDataType.BOOLEAN, isGlint);

            if (isGlint)
                item.addUnsafeEnchantment(Enchantment.LURE, 1);
            else
                item.removeEnchantment(Enchantment.LURE);

            return;
        }

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

    public HashMap<Double, List<EntityType>> getEntityDropChances() {
        return entityDrops;
    }

    public HashMap<Double, List<Material>> getBlockDropChances() {
        return blockDrops;
    }

    public CraftingType getCraftingType() {
        return craftingType;
    }

    public List<ItemStack> getCrafting() {
        return crafting;
    }

    public static String getName(@NotNull ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        Reforge r = Reforge.getReforge(is);
        String name = meta.getDisplayName();
        String s = "";

        if (!name.isBlank())
            s = name.replaceFirst(String.valueOf(name.charAt(1)), "§").replace("§", "");
        if (r != null)
            return s.replace(r.getName() + " ", "");

        return s;
    }

    public String getName() {
        return getName(itemStack);
    }

    public static void setName(String name, ItemStack is) {
        ItemMeta meta = is.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        is.setItemMeta(meta);
    }

    public static List<String> getLore(@NotNull ItemStack is) {
        ItemMeta meta = is.getItemMeta();

        assert meta != null;
        return meta.getLore();
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

    public List<String> getAddInfo() {
        return addInfo;
    }

    public boolean hasAddInfo() {
        return getAddInfo() != null;
    }

    public void setAddInfo(List<String> addInfo) {
        this.addInfo = addInfo;
    }

    public static void addAddInfoToLore(ItemStack is) {
        List<String> lore = Item.getLore(is);
        List<String> addInfo = Item.toItem(is).getAddInfo();
        if (addInfo == null) return;

        lore.add(1, "");
        lore.addAll(1, addInfo);
        Item.setLore(is, lore);
    }

    public static void removeAddInfoFromLore(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        Item item = Item.toItem(is);
        if (item == null) return;
        List<String> addInfo = item.getAddInfo();

        if (item.hasAddInfo()) {
            for (String s : addInfo)
                lore.remove(s);

            lore.remove(1);
        }

        Item.setLore(is, lore);
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
