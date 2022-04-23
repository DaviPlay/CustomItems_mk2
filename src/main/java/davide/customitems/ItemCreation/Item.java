package davide.customitems.ItemCreation;

import davide.customitems.API.*;
import davide.customitems.CustomItems;
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
    private ItemStack itemStack;
    private final Color color;
    private final Type type;
    private final Rarity rarity;
    private final List<Ability> abilities;
    private int delay;
    private boolean isGlint;
    private final boolean isStackable;
    private final CraftingType craftingType;
    private final float exp;
    private final int cookingTime;
    private final HashMap<Enchantment, Integer> enchantments;
    private final List<ItemStack> crafting;
    private String name;
    private List<String> lore;

    private NamespacedKey key;

    public Item(ItemBuilder builder) {
        this.itemStack = builder.itemStack;
        this.color = builder.color;
        this.type = builder.type;
        this.rarity = builder.rarity;
        this.abilities = builder.abilities;
        this.delay = builder.delay;
        this.isGlint = builder.isGlint;
        this.isStackable = builder.isStackable;
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
        container.set(key, PersistentDataType.INTEGER, 1);

        if (rarity != null)
            meta.setDisplayName(rarity.getColor() + name);
        else
            meta.setDisplayName(name);

        //Ability prefix
        String separator = "/s";
        int j = 0;
        if (abilities != null)
            for (Ability ability : abilities)
                if (lore != null) {
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
        if (lore != null) {
            for (String s : lore) {
                if (!(s.startsWith("§")))
                    s = "§7" + s;

                newLore.add(s);
                lore = newLore;
            }
        }

        //Adding the cooldown to the lore
        if (delay > 0) {
            assert lore != null;
            if (delay < 60)
                lore.add("§8§o" + delay + " sec cooldown");
            else
                lore.add("§8§o" + delay / 60 + " min cooldown");
        }

        //More fancy spacing
        if (lore != null)
            lore.add("");

        //Rarity Suffix
        if (type != null && rarity != null && lore != null) {
            if (rarity == Rarity.TEST) {
                lore.add("§cThis item is a test and thus unfinished,");
                lore.add("§cit may not work as intended");
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " ITEM");
            } else
                lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.name() + " " + type.name());
        }

        //Adding the enchants and making them blue
        String str, enchName, lvl;

        if (enchantments != null)
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);

                if (lore != null) {
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

        //Makes it glow without adding any enchantment
        setGlint(isGlint, itemStack);

        //Recipe
        if (crafting != null)
            new Crafting(key, itemStack, craftingType, exp, cookingTime, crafting);
    }

    public static Item toItem(ItemStack is) {
        Item item = null;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        for (Item[] items : ItemList.items)
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

    public Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
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
