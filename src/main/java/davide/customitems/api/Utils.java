package davide.customitems.api;

import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    /**
     * Throws a weapon that breaks after hitting a mob, hitting a solid block or reaching a set distance
     * @param player the player throwing the weapon
     * @param is the weapon being thrown
     * @param distanceMax the max distance a weapon can traverse before being destroyed
     * @throws IllegalArgumentException if distanceMax is lower than 1
     */
    @SuppressWarnings("deprecation")
    public static void throwItem(Player player, @NotNull ItemStack is, final int distanceMax) {
        if (distanceMax < 1)
            throw new IllegalArgumentException("Max distance should be higher than 1");

        final Vector dir = player.getEyeLocation().getDirection();

        ArmorStand as = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0).add(dir), ArmorStand.class);
        as.setHelmet(is);
        as.setHeadPose(new EulerAngle(190, as.getHeadPose().getY(), as.getHeadPose().getZ()));
        as.setInvisible(true);
        as.setSmall(true);
        as.setMarker(true);
        as.setCanPickupItems(false);
        as.setGravity(false);

        AtomicInteger i = new AtomicInteger();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CustomItems.getPlugin(CustomItems.class), () -> {
            if (i.get() > distanceMax)
                as.remove();

            if (as.isDead()) return;

            as.teleport(as.getLocation().add(dir));
            as.setHeadPose(new EulerAngle(as.getHeadPose().getX(), as.getHeadPose().getY() + 2.5, as.getHeadPose().getZ()));

            if (!as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.AIR && !as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.CAVE_AIR)
                for (Entity entity : as.getWorld().getNearbyEntities(as.getLocation(), 2, 2, 2))
                    if (entity instanceof LivingEntity livingEntity)
                        if (!(livingEntity instanceof ArmorStand))
                            if (as.getLocation().distanceSquared(livingEntity.getLocation()) <= 2) {
                                player.attack(livingEntity);
                                as.remove();
                                break;
                            }

            i.getAndIncrement();
        }, 0, 1);
    }

    /**
     * Throws a weapon that breaks after hitting a mob, hitting a solid block or reaching a set distance
     * @param player the player throwing the weapon
     * @param is the weapon being thrown
     * @param instruction the code to run after an enemy's hit
     * @param distanceMax the max distance a weapon can traverse before being destroyed
     * @throws IllegalArgumentException if distanceMax is lower than 1
     */
    @SuppressWarnings("deprecation")
    public static void throwItem(Player player, @NotNull ItemStack is, Instruction instruction, final int distanceMax) {
        if (distanceMax < 1)
            throw new IllegalArgumentException("Max distance should be higher than 1");

        final Vector dir = player.getEyeLocation().getDirection();

        ArmorStand as = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0).add(dir), ArmorStand.class);
        as.setHelmet(is);
        as.setHeadPose(new EulerAngle(190, as.getHeadPose().getY(), as.getHeadPose().getZ()));
        as.setInvisible(true);
        as.setSmall(true);
        as.setMarker(true);
        as.setCanPickupItems(false);
        as.setGravity(false);

        AtomicInteger i = new AtomicInteger();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CustomItems.getPlugin(CustomItems.class), () -> {
            if (i.get() > distanceMax)
                as.remove();

            if (as.isDead()) return;

            as.teleport(as.getLocation().add(dir));
            as.setHeadPose(new EulerAngle(as.getHeadPose().getX(), as.getHeadPose().getY() + 2.5, as.getHeadPose().getZ()));

            if (!as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.AIR && !as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.CAVE_AIR)
                for (Entity entity : as.getWorld().getNearbyEntities(as.getLocation(), 2, 2, 2))
                    if (entity instanceof LivingEntity livingEntity)
                        if (!(livingEntity instanceof ArmorStand))
                            if (as.getLocation().distanceSquared(livingEntity.getLocation()) <= 2) {
                                player.attack(livingEntity);
                                instruction.run(livingEntity);
                                as.remove();
                                break;
                            }

            i.getAndIncrement();
        }, 0, 1);
    }

    /**
     * Checks if the player has a complete set equipped <p>
     * Use only with the ArmorEquipEvent <p>
     * <b>THE TARGET ARMOR MUST BE IN ORDER FROM BOOTS TO HELMET</b>
     * @param armorContents the armor the player has equipped
     * @param targetArmor the complete set of armor the player NEEDS to have equipped
     * @param armorType the type of armor being equipped
     * @param armorPiece the new armor piece being worn
     * @return whether the player has the full set equipped or not
     */
    public static boolean hasFullSet(ItemStack[] armorContents, Item[] targetArmor, ArmorEquipEvent.ArmorType armorType, ItemStack armorPiece) {
        List<ItemMeta> armorMeta = new ArrayList<>();
        List<PersistentDataContainer> containers = new ArrayList<>();
        int amountOfArmor = 0;

        switch (armorType) {
            case BOOTS -> armorContents[0] = armorPiece;
            case LEGGINGS -> armorContents[1] = armorPiece;
            case CHESTPLATE -> armorContents[2] = armorPiece;
            case HELMET -> armorContents[3] = armorPiece;
        }

        for (ItemStack i : armorContents) {
            if (i == null) return false;
            armorMeta.add(i.getItemMeta());
        }
        for (ItemMeta m : armorMeta) {
            if (m == null) return false;
            containers.add(m.getPersistentDataContainer());
        }
        for (int i = 0; i < containers.size(); i++)
            if (containers.get(i).has(targetArmor[i].getKey(), PersistentDataType.INTEGER)) amountOfArmor++;

        return amountOfArmor == 4;
    }

    /**
     * Checks if the player has a complete set equipped <p>
     * <b>THE TARGET ARMOR MUST BE IN ORDER FROM BOOTS TO HELMET</b>
     * @param armorContents the armor the player has equipped
     * @param targetArmor the complete set of armor the player needs to have equipped
     * @return whether the player has the full set equipped or not
     */
    public static boolean hasFullSet(ItemStack[] armorContents, Item[] targetArmor) {
        List<ItemMeta> armorMeta = new ArrayList<>();
        List<PersistentDataContainer> containers = new ArrayList<>();
        int amountOfArmor = 0;

        for (ItemStack i : armorContents) {
            if (i == null) return false;
            armorMeta.add(i.getItemMeta());
        }
        for (ItemMeta m : armorMeta) {
            if (m == null) return false;
            containers.add(m.getPersistentDataContainer());
        }
        for (int i = 0; i < containers.size(); i++)
            if (containers.get(i).has(targetArmor[i].getKey(), PersistentDataType.INTEGER)) amountOfArmor++;

        return amountOfArmor == 4;
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

    public static boolean validateItem(@NotNull ItemStack is, Item targetItem, Player player, int abilityIndex, Event event) {
        Item item = Item.toItem(is);
        if (item == null) return true;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        UUID uuid = null;

        if (item.hasRandomUUID()) {
            uuid = container.get(item.getKey(), new UUIDDataType());
            if (!container.has(targetItem.getKey(), new UUIDDataType())) return true;
        } else {
            if (!container.has(targetItem.getKey(), PersistentDataType.INTEGER)) return true;
        }

        boolean purity = Utils.hasCustomItemInInv(ItemList.purity, player.getInventory());
        Ability ability = item.getAbilities().get(abilityIndex);
        int cooldown = Math.max(0, purity ? (int) Math.floor(ability.cooldown() * 0.75) : ability.cooldown());

        if (item.getAbilities() != null && cooldown > 0)
            if (item.hasRandomUUID()) {
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

        if (event instanceof PlayerInteractEvent e) {
            switch (item.getAbilities().get(abilityIndex).type()) {
                case RIGHT_CLICK -> {
                    if (e.getHand() != EquipmentSlot.HAND) return true;
                    if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                        return true;
                }
                case SHIFT_RIGHT_CLICK -> {
                    if (!player.isSneaking()) return true;
                    if (e.getHand() != EquipmentSlot.HAND) return true;
                    if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                        return true;
                }
                case LEFT_CLICK -> {
                    if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
                        return true;
                }
                case SHIFT_LEFT_CLICK -> {
                    if (!player.isSneaking()) return true;
                    if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
                        return true;
                }
                case HIT -> {
                    if (e.getAction() == Action.PHYSICAL)
                        return true;
                }
            }
        }

        if (item.hasRandomUUID())
            Cooldowns.setCooldown(container.get(item.getKey(), new UUIDDataType()), ability.key(), cooldown);
        else
            Cooldowns.setCooldown(player.getUniqueId(), ability.key(), cooldown);

        return false;
    }

    public static boolean validateItem(@NotNull ItemStack is, Item targetItem, Player player, Event event) {
        Item item = Item.toItem(is);
        if (item == null) return true;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        UUID uuid = null;

        if (item.hasRandomUUID()) {
            uuid = container.get(item.getKey(), new UUIDDataType());
            if (!container.has(targetItem.getKey(), new UUIDDataType())) return true;
        } else {
            if (!container.has(targetItem.getKey(), PersistentDataType.INTEGER)) return true;
        }

        boolean purity = Utils.hasCustomItemInInv(ItemList.purity, player.getInventory());
        Ability ability = item.getAbilities().get(0);
        int cooldown = Math.max(0, purity ? (int) Math.floor(ability.cooldown() * 0.75) : ability.cooldown());

        if (item.getAbilities() != null && cooldown > 0)
            if (item.hasRandomUUID()) {
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

        if (event instanceof PlayerInteractEvent e) {
            if (e.getClickedBlock() != null)
                if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return true;

            if (item.getAbilities() != null) {
                switch (item.getAbilities().get(0).type()) {
                    case RIGHT_CLICK -> {
                        if (e.getHand() != EquipmentSlot.HAND) return true;
                        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                            return true;
                    }
                    case SHIFT_RIGHT_CLICK -> {
                        if (!player.isSneaking()) return true;
                        if (e.getHand() != EquipmentSlot.HAND) return true;
                        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)
                            return true;
                    }
                    case LEFT_CLICK -> {
                        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR)
                            return true;
                    }
                    case SHIFT_LEFT_CLICK -> {
                        if (!player.isSneaking()) return true;
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

        if (item.hasRandomUUID())
            Cooldowns.setCooldown(container.get(item.getKey(), new UUIDDataType()), ability.key(), cooldown);
        else
            Cooldowns.setCooldown(player.getUniqueId(), ability.key(), cooldown);

        return false;
    }

    public static boolean validateArmor(@NotNull ItemStack is, @NotNull Item[] targetArmor) {
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
    public static ItemStack findItemInInv(@NotNull ItemStack is, @NotNull Inventory inv) {
        for (ItemStack i : inv.getContents())
            if (i != null && i.equals(is))
                return i;

        return null;
    }

    @Nullable
    public static ItemStack findCustomItemInInv(@NotNull Item item, @NotNull Inventory inv) {
        for (ItemStack i : inv.getContents())
            if (i != null && Item.isCustomItem(i))
                if (Item.toItem(i).getKey().equals(item.getKey()))
                    return i;

        return null;
    }

    public static boolean hasCustomItemInInv(@NotNull Item item, @NotNull Inventory inv) {
        for (ItemStack i : inv.getContents())
            if (i != null && Item.isCustomItem(i))
                if (Item.toItem(i).getKey().equals(item.getKey()))
                    return true;

        return false;
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
        DecimalFormat format = new DecimalFormat("0.##");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(f);
    }
}
