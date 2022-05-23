package davide.customitems.ItemCreation;

import davide.customitems.API.*;
import davide.customitems.Crafting.Crafting;
import davide.customitems.Crafting.CraftingType;
import davide.customitems.CustomItems;
import davide.customitems.Lists.ItemList;
import davide.customitems.ReforgeCreation.Reforge;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

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
    private final boolean isStackable;
    private final boolean hasRandomUUID;
    private final CraftingType craftingType;
    private final float exp;
    private final int cookingTime;
    private final HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private final String name;
    private List<String> lore;

    private int temporaryDamage;
    private int temporaryHealth;
    private int temporaryCritChance;
    private NamespacedKey key;

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
        this.isStackable = builder.isStackable;
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
        Plugin plugin = CustomItems.getPlugin(CustomItems.class);
        if (name.charAt(0) == '§')
            key = new NamespacedKey(plugin, name.toLowerCase(Locale.ROOT).replace(" ", "_").replace(name.charAt(1), '§').replace("§", ""));
        else
            key = new NamespacedKey(plugin, name.toLowerCase(Locale.ROOT).replace(" ", "_"));

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
        temporaryDamage = damage;

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
        temporaryHealth = health;

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
        temporaryCritChance = critChance;

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

                    if (ability != Ability.GENERIC)
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

    public static Item toItem(ItemStack is) {
        Item item = null;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item[] items : ItemList.items)
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

    public static void setReforge(Reforge reforge, ItemStack is) {
        Item item = toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;

        if (getDamage(is) != 0)
            lore.remove(0);

        if (getHealth(is) != 0)
            lore.remove(0);

        if (getCritChance(is) != 0)
            lore.remove(0);

        setLore(lore, is);

        String name = item.rarity.getColor() + reforge.getName() +  " " + item.getName();
        item.setName(name, is);

        setDamageWithFirstReforge(item.getBaseDamage(), is, reforge.getDamageModifier());
        setHealthWithFirstReforge(item.getBaseHealth(), is, reforge.getHealthModifier());
        setCritChanceWithFirstReforge(item.getBaseCritChance(), is, reforge.getCritChanceModifier());
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

    public int getBaseDamage() {
        return damage;
    }

    public static int getDamage(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        Item item = Item.toItem(is);
        if (item == null) return 0;
        Reforge reforge = Reforge.getReforge(is);

        if (reforge != null)
            return item.getBaseDamage() + reforge.getDamageModifier();
        else
            return item.getBaseDamage();
    }

    public static int getTemporaryDamage(ItemStack is) {
        Item item = Item.toItem(is);
        if (item == null) return 0;

        return item.temporaryDamage;

        /*ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        List<String> lore = meta.getLore();
        if (lore == null) return 0;

        //Getting it through lore
        int damage = 0;
        for (String s : lore)
            if (s.startsWith("§7Damage: "))
                if (String.valueOf(s.charAt(13)).equals(" "))
                    damage = Integer.parseInt(s.substring(12, 13).trim());
                else
                    damage = Integer.parseInt(s.substring(12, 14).trim());

        return damage;*/
    }

    public static void setDamage(int damage, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (getDamage(is) != 0)
            lore.set(0, "§7Damage: " + "§c" + damage);
        else
            lore.add(0, "§7Damage: " + "§c" + damage);

        setLore(lore, is);
        item.temporaryDamage = damage;
    }

    public static void setDamageWithReforge(int damage, ItemStack is, int reforgeDamage) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (reforgeDamage == 0) {
            if (Item.toItem(is).getBaseDamage() > 0) {
                lore.add(0, "§7Damage: " + "§c" + damage);
                setLore(lore, is);
                item.temporaryDamage = Item.toItem(is).getBaseDamage();
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

        setLore(lore, is);
        item.temporaryDamage = damage;
    }

    private static void setDamageWithFirstReforge(int damage, ItemStack is, int reforgeDamage) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (reforgeDamage == 0) {
            if (Item.toItem(is).getBaseDamage() > 0) {
                lore.add(0, "§7Damage: " + "§c" + damage);
                setLore(lore, is);
                item.temporaryDamage = Item.toItem(is).getBaseDamage();
            }

            return;
        }

        if (reforgeDamage > 0)
            lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(+" + reforgeDamage + ")");
        else
            lore.add(0, "§7Damage: " + "§c" + (damage + reforgeDamage) + " §8(" + reforgeDamage + ")");

        setLore(lore, is);
        item.temporaryDamage = damage;
    }

    public int getBaseHealth() {
        return health;
    }

    public static int getHealth(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        Item item = Item.toItem(is);
        if (item == null) return 0;
        Reforge reforge = Reforge.getReforge(is);

        if (reforge != null)
            return item.getBaseHealth() + reforge.getHealthModifier();
        else
            return item.getBaseHealth();
    }

    public static int getTemporaryHealth(ItemStack is) {
        Item item = toItem(is);
        if (item == null) return 0;

        return item.temporaryHealth;
    }

    public static void setHealth(int health, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
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

        setLore(lore, is);
        item.temporaryHealth = health;
    }

    public static void setHealthWithReforge(int health, ItemStack is, int reforgeHealth) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0)
            i = 1;
        else
            i = 0;

        if (reforgeHealth == 0) {
            if (Item.toItem(is).getBaseHealth() > 0) {
                lore.add(i, "§7Health: " + "§c" + health);
                setLore(lore, is);
                item.temporaryHealth = Item.toItem(is).getBaseHealth();
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


        setLore(lore, is);
        item.temporaryHealth = health;
    }

    private static void setHealthWithFirstReforge(int health, ItemStack is, int reforgeHealth) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) != 0)
            i = 1;
        else
            i = 0;

        if (reforgeHealth == 0) {
            if (Item.toItem(is).getBaseHealth() > 0) {
                lore.add(i, "§7Health: " + "§c" + health);
                setLore(lore, is);
                item.temporaryHealth = Item.toItem(is).getBaseHealth();
            }

            return;
        }

        if (reforgeHealth > 0)
            lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(+" + reforgeHealth + ")");
        else
            lore.add(i, "§7Health: " + "§c" + (health + reforgeHealth) + " §8(" + reforgeHealth + ")");

        setLore(lore, is);
        item.temporaryHealth = health;
    }


    public int getBaseCritChance() {
        return critChance;
    }

    public static int getCritChance(ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return 0;
        Item item = Item.toItem(is);
        if (item == null) return 0;
        Reforge reforge = Reforge.getReforge(is);

        if (reforge != null)
            return item.getBaseCritChance() + reforge.getCritChanceModifier();
        else
            return item.getBaseCritChance();
    }

    public static int getTemporaryCritChance(ItemStack is) {
        Item item = toItem(is);
        if (item == null) return 0;

        return item.temporaryCritChance;
    }

    public static void setCritChance(int critChance, ItemStack is) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
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

        setLore(lore, is);
        item.temporaryCritChance = critChance;
    }

    public static void setCritChanceWithReforge(int critChance, ItemStack is, int reforgeCrit) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
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

        if (reforgeCrit == 0) {
            if (Item.toItem(is).getBaseCritChance() > 0) {
                lore.add(i, "§7Crit: " + "§c" + critChance + "%");
                setLore(lore, is);
                item.temporaryCritChance = Item.toItem(is).getBaseCritChance();
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

        setLore(lore, is);
        item.temporaryCritChance = critChance;
    }

    private static void setCritChanceWithFirstReforge(int critChance, ItemStack is, int reforgeCrit) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        Item item = toItem(is);
        if (item == null) return;

        int i;
        if (getDamage(is) > 0 && getHealth(is) > 0)
            i = 2;
        else if (getDamage(is) > 0 ^ getHealth(is) > 0)
            i = 1;
        else
            i = 0;

        if (reforgeCrit == 0) {
            if (Item.toItem(is).getBaseCritChance() > 0) {
                lore.add(i, "§7Crit: " + "§c" + critChance + "%");
                setLore(lore, is);
                item.temporaryCritChance = Item.toItem(is).getBaseCritChance();
            }

            return;
        }

        if (reforgeCrit > 0)
            lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(+" + reforgeCrit + "%)");
        else
            lore.add(i, "§7Crit: " + "§c" + (critChance + reforgeCrit) + "% §8(" + reforgeCrit + "%)");

        setLore(lore, is);
        item.temporaryCritChance = critChance;
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

    public static UUID getRandomUUID(ItemStack is) {
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
