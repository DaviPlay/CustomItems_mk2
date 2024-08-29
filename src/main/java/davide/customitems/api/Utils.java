package davide.customitems.api;

import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.lists.ItemList;
import davide.customitems.playerStats.ChanceManager;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Utils {
    private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public static void recombItem(ItemStack result, ItemStack original) {
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta == null) return;
        PersistentDataContainer container = resultMeta.getPersistentDataContainer();
        if (container.has(new NamespacedKey(plugin, "recombed"), PersistentDataType.BOOLEAN)) return;
        if (Item.getRarity(result).ordinal() + 1 == Rarity.values().length - 2) return;

        Reforge reforge = Reforge.getReforge(original);
        int damage;
        int critChance;
        float critDamage;
        int health;
        int defence;
        int speed;
        int luck;

        if (reforge != null) {
            damage = Item.getBaseDamage(original, reforge, true);
            critChance = Item.getBaseCritChance(original, reforge);
            critDamage = Item.getBaseCritDamage(original, reforge);
            health = Item.getBaseHealth(original, reforge);
            defence = Item.getBaseDefence(original, reforge);
            speed = Math.round(Item.getBaseSpeed(original, reforge) * 1000f);
            luck = Item.getBaseDefence(original, reforge);
        } else {
            damage = Item.getDamage(original);
            critChance = Item.getCritChance(original);
            critDamage = Item.getCritDamage(original);
            health = Item.getHealth(original);
            defence = Item.getDefence(original);
            speed = Math.round(Item.getSpeed(original) * 1000f);
            luck = Item.getLuck(original);
        }

        Item.setRarity(result, Rarity.values()[Item.getRarity(result).ordinal() + 1]);

        ItemMeta afterMeta = result.getItemMeta();
        PersistentDataContainer afterContainer = afterMeta.getPersistentDataContainer();
        List<String> lore = afterMeta.getLore();
        if (lore == null) return;
        ChatColor color = Item.getRarity(result).getColor();
        lore.add(lore.size() - 1, color + "§l§kL §r" + color + "§lRARITY UPGRADED" + " §kO");
        afterMeta.setLore(lore);
        afterContainer.set(new NamespacedKey(plugin, "recombed"), PersistentDataType.BOOLEAN, true);
        result.setItemMeta(afterMeta);

        if (reforge != null) {
            ItemMeta refMeta = result.getItemMeta();
            PersistentDataContainer refContainer = refMeta.getPersistentDataContainer();
            refContainer.set(new NamespacedKey(plugin, "reforge"), PersistentDataType.STRING, reforge.getName());
            result.setItemMeta(refMeta);

            String name = Item.getRarity(result).getColor() + reforge.getName() + " " + Item.getName(result);
            Item.setName(name, result);

            Item.setStats(damage, critChance, critDamage, health, defence, speed, luck, result, true);
        }
    }

    //AutoRecomb
    public static void autoRecombUpgrade(ItemStack is, Player player) {
        if (!hasCustomItemInInv(ItemList.autoRecomb, player.getInventory())) return;

        ChanceManager.chanceCalculation(0.1, new Instruction() {
            @Override
            public void run() {
                recombItem(is, is);
                player.sendMessage("§e§lAUTO-RECOMB! §r§aYour Auto-Recombobulator just upgraded an item!");
            }
        }, player);
    }

    public static void addScrollDamage(ItemStack result, ItemStack original) {
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta == null) return;
        PersistentDataContainer container = resultMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "undead_scroll_dmg");
        if (container.has(key, PersistentDataType.INTEGER) && container.get(key, PersistentDataType.INTEGER) >= 5) return;

        container.set(key, PersistentDataType.INTEGER, container.has(key, PersistentDataType.INTEGER) ? container.get(key, PersistentDataType.INTEGER) + 1 : 1);
        result.setItemMeta(resultMeta);

        Reforge reforge = Reforge.getReforge(original);
        int damage;
        int critChance;
        float critDamage;
        int health;
        int defence;
        int speed;
        int luck;

        if (reforge != null) {
            damage = Item.getBaseDamage(original, reforge, false);
            critChance = Item.getBaseCritChance(original, reforge);
            critDamage = Item.getBaseCritDamage(original, reforge);
            health = Item.getBaseHealth(original, reforge);
            defence = Item.getBaseDefence(original, reforge);
            speed = Math.round(Item.getBaseSpeed(original, reforge) * 1000f);
            luck = Item.getBaseDefence(original, reforge);
        } else {
            damage = Item.getDamage(original);
            critChance = Item.getCritChance(original);
            critDamage = Item.getCritDamage(original);
            health = Item.getHealth(original);
            defence = Item.getDefence(original);
            speed = Math.round(Item.getSpeed(original) * 1000f);
            luck = Item.getLuck(original);
        }

        Item.setStats(damage + 1, critChance, critDamage, health, defence, speed, luck, result, true);
    }

    @NotNull
    public static String normalizeKey(String key) {
        return key.replace(" ", "_")
                .replace("\'", "")
                .replace(",", "")
                .replace("!", "")
                .replace("ö", "o")
                .replace("&", "and")
                .toUpperCase(Locale.ROOT);
    }

    /**
     * Checks if the player has a complete set equipped <p>
     * Use only with the ArmorEquipEvent <p>
     * @param armorContents the armor the player has equipped
     * @param armorType the type of armor being equipped
     * @param armorPiece the new armor piece being worn
     * @return whether the player has the full set equipped or not
     */
    public static boolean hasFullSet(ItemStack[] armorContents, ArmorEquipEvent.ArmorType armorType, ItemStack armorPiece) {
        List<ItemMeta> armorMeta = new ArrayList<>();
        List<PersistentDataContainer> containers = new ArrayList<>();
        int amountOfArmor = 0;

        switch (armorType) {
            case BOOTS -> armorContents[0] = armorPiece;
            case LEGGINGS -> armorContents[1] = armorPiece;
            case CHESTPLATE -> armorContents[2] = armorPiece;
            case HELMET -> armorContents[3] = armorPiece;
        }
        List<ItemStack> targetArmor = new ArrayList<>();
        String set = armorPiece.getItemMeta().getDisplayName().replace("Helmet", "").replace("Chestplate", "").replace("Leggings", "").replace("Boots", "").trim();

        for (ItemStack i : armorContents)
            if (i != null && i.getItemMeta().getDisplayName().replace("Helmet", "").replace("Chestplate", "").replace("Leggings", "").replace("Boots", "").trim().equals(set))
                targetArmor.add(i);

        if (targetArmor.size() != 4) return true;

        for (ItemStack i : armorContents) {
            if (i == null) return true;
            armorMeta.add(i.getItemMeta());
        }
        for (ItemMeta m : armorMeta) {
            if (m == null) return true;
            containers.add(m.getPersistentDataContainer());
        }
        for (int i = 0; i < containers.size(); i++)
            if (containers.get(i).has(Item.toItem(targetArmor.get(i)).getKey(), PersistentDataType.INTEGER)) amountOfArmor++;

        return amountOfArmor != 4;
    }

    /**
     * Checks if the player has a complete set equipped <p>
     * <b>THE TARGET ARMOR MUST BE IN ORDER FROM BOOTS TO HELMET</b>
     * @param armorContents the armor the player has equipped
     * @return whether the player has the full set equipped or not
     */
    public static boolean hasFullSet(ItemStack[] armorContents) {
        List<ItemMeta> armorMeta = new ArrayList<>();
        List<PersistentDataContainer> containers = new ArrayList<>();
        int amountOfArmor = 0;

        List<ItemStack> targetArmor = new ArrayList<>();
        if (armorContents[0] == null) return true;
        String set = armorContents[0].getItemMeta().getDisplayName().replace("Helmet", "").replace("Chestplate", "").replace("Leggings", "").replace("Boots", "").trim();

        for (ItemStack i : armorContents) {
            if (i == null) return true;
            if (i.getItemMeta().getDisplayName().replace("Helmet", "").replace("Chestplate", "").replace("Leggings", "").replace("Boots", "").trim().equals(set))
                targetArmor.add(i);
            else
                return true;
        }
        for (ItemStack i : armorContents) {
            if (i == null) return true;
            armorMeta.add(i.getItemMeta());
        }
        for (ItemMeta m : armorMeta) {
            if (m == null) return true;
            containers.add(m.getPersistentDataContainer());
        }
        for (int i = 0; i < containers.size(); i++)
            if (containers.get(i).has(Item.toItem(targetArmor.get(i)).getKey(), PersistentDataType.INTEGER)) amountOfArmor++;

        return amountOfArmor != 4;
    }

    /**
     * Checks the blocks in a radius from a specified point
     * @param target the point to search the blocks around
     * @param offset how many blocks to search in any direction
     * @return an ArrayList containing the blocks in a radius
     */
    public static ArrayList<Block> getBlocksInRadius(Block target, Vector3 offset){
        ArrayList<Block> blocks = new ArrayList<>();

        for (double x = target.getLocation().getX() - offset.x(); x <= target.getLocation().getX() + offset.x(); x++)
            for (double y = target.getLocation().getY() - offset.y(); y <= target.getLocation().getY() + offset.y(); y++)
                for (double z = target.getLocation().getZ() - offset.z(); z <= target.getLocation().getZ() + offset.z(); z++) {
                    Location loc = new Location(target.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }

        return blocks;
    }

    public static ArrayList<Block> getBlocksInRadius(Block target, int offsetX, int offsetY, int offsetZ){
        ArrayList<Block> blocks = new ArrayList<>();

        for (double x = target.getLocation().getX() - offsetX; x <= target.getLocation().getX() + offsetX; x++)
            for (double y = target.getLocation().getY() - offsetY; y <= target.getLocation().getY() + offsetY; y++)
                for (double z = target.getLocation().getZ() - offsetZ; z <= target.getLocation().getZ() + offsetZ; z++) {
                    Location loc = new Location(target.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }

        return blocks;
    }

    public static boolean validateItem(@NotNull ItemStack is, Player player, int abilityIdx, Event event) {
        Item item = Item.toItem(is);
        if (item == null) return true;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        boolean hasRandomUUID = item.hasRandomUUID();

        //Cooldown check
        boolean purity = Utils.hasCustomItemInInv(ItemList.purity, player.getInventory());
        List<Ability> abilities = item.getAbilities();
        if (abilities != null) {
            Ability ability = item.getAbilities().get(abilityIdx);
            int cooldown = Math.max(0, purity ? (int) Math.floor(ability.cooldown() * 0.75) : ability.cooldown());

            if (item.getAbilities() != null && cooldown > 0)
                if (hasRandomUUID) {
                    UUID uuid = container.get(item.getKey(), new UUIDDataType());

                    if (Cooldowns.checkCooldown(uuid, ability.key())) {
                        if (ability.showDelay())
                            player.sendMessage(Cooldowns.inCooldownMessage(uuid, ability.key()));
                        return true;
                    }
                } else {
                    if (Cooldowns.checkCooldown(player.getUniqueId(), ability.key())) {
                        if (ability.showDelay())
                            player.sendMessage(Cooldowns.inCooldownMessage(player.getUniqueId(), ability.key()));
                        return true;
                    }
                }

            if (hasRandomUUID)
                Cooldowns.setCooldown(container.get(item.getKey(), new UUIDDataType()), ability.key(), cooldown);
            else
                Cooldowns.setCooldown(player.getUniqueId(), ability.key(), cooldown);
        }

        //Event check
        if (event instanceof PlayerInteractEvent e) {
            if (e.getClickedBlock() != null) {

                if (item.getAbilities() != null) {
                    switch (item.getAbilities().get(abilityIdx).type()) {
                        case SHIFT_RIGHT_CLICK -> {
                            if (!player.isSneaking()) return true;
                            if (e.getHand() != EquipmentSlot.HAND) return true;
                            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                                return true;
                        }
                        case RIGHT_CLICK -> {
                            if (e.getHand() != EquipmentSlot.HAND) return true;
                            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                                return true;
                        }
                        case SHIFT_LEFT_CLICK -> {
                            if (!player.isSneaking()) return true;
                            if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
                                return true;
                        }
                        case LEFT_CLICK -> {
                            if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
                                return true;
                        }
                        case HIT -> {
                            if (e.getAction() == Action.PHYSICAL)
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    //TODO: rework this please
    public static boolean validateArmor(ItemStack is, Item[] targetArmor) {
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int armorCount = 0;

        for (Item item : targetArmor)
            if (container.has(item.getKey(), PersistentDataType.INTEGER)) {
                armorCount++;
            }

        return armorCount == 0;
    }

    public static void addToInventory(Player player, ItemStack... is) {
        Arrays.stream(is).iterator().forEachRemaining(item -> {
            if (player.getInventory().firstEmpty() == -1)
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            else
                player.getInventory().addItem(item);
        });
    }

    @Nullable
    public static ItemStack findItemInInv(ItemStack is, Inventory inv) {
        for (ItemStack i : inv.getContents())
            if (i != null && i.equals(is))
                return i;

        return null;
    }

    @Nullable
    public static ItemStack findCustomItemInInv(Item item, Inventory inv) {
        for (ItemStack i : inv.getContents())
            if (i != null && Item.isCustomItem(i))
                if (Item.toItem(i).getKey().equals(item.getKey()))
                    return i;

        return null;
    }

    @NotNull
    public static List<Item> getCustomItemsInInv(Inventory inv) {
        List<Item> items = new ArrayList<>();

        for (ItemStack i : inv.getContents())
            if (i != null && Item.isCustomItem(i))
                items.add(Item.toItem(i));

        return items;
    }

    public static boolean hasCustomItemInInv(Item item, Inventory inv) {
        return findCustomItemInInv(item, inv) != null;
    }

    @Deprecated
    public static int findItemInItemsInv(Inventory inv) {
        NamespacedKey key = null;

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getItemMeta() != null)
                if (Item.isCustomItem(item) && inv.getItem(25) != null && inv.getItem(25).equals(item)) {
                    key = Item.toItem(item).getKey();
                    break;
                }
        }

        int k = 0;
        for (Inventory i : ItemsGUI.itemInv) {
            for (ItemStack item : i.getContents())
                if (item != null && Item.isCustomItem(item) && Item.toItem(item).getKey().equals(key))
                    return k;
            k++;
        }

        return 0;
    }

    public static String trimFloatZeros(float f) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        return df.format(f);
    }

    public static double sineWaveFormula(double amplitude, double frequency, double positionInTime, double phase) {
        return amplitude * Math.sin(2 * Math.PI * frequency * positionInTime + phase);
    }
}
