package davide.customitems.api;

import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {

    /**
     * Throws a weapon that breaks after hitting a mob or reaching a set distance
     * @param player the player throwing the weapon
     * @param is the weapon being thrown
     * @param DISTANCE_MAX the max distance a weapon can traverse before being destroyed
     * @throws IllegalArgumentException if DISTANCE_MAX is lower then 1
     */
    public static void throwItem(Player player, @NotNull ItemStack is, final int DISTANCE_MAX) {
        if (DISTANCE_MAX < 1)
            throw new IllegalArgumentException("Max distance should be higher then 1");

        final Vector dir = player.getEyeLocation().getDirection();

        ArmorStand as = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0).add(dir), ArmorStand.class);
        as.setHelmet(ItemList.throwingAxe.getItemStack());
        as.setHeadPose(new EulerAngle(190, as.getHeadPose().getY(), as.getHeadPose().getZ()));
        as.setInvisible(true);
        as.setSmall(true);
        as.setMarker(true);
        as.setCanPickupItems(false);
        as.setGravity(false);

        AtomicInteger i = new AtomicInteger();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(CustomItems.getPlugin(CustomItems.class), () -> {
            if (as.isDead()) return;

            if (i.get() > DISTANCE_MAX)
                as.remove();

            as.teleport(as.getLocation().add(dir));
            as.setHeadPose(new EulerAngle(as.getHeadPose().getX(), as.getHeadPose().getY() + 2.5, as.getHeadPose().getZ()));

            if (!as.getLocation().add(dir).getBlock().isPassable() && as.getLocation().add(dir).getBlock().getType() != Material.AIR)
                for (Entity entity : as.getWorld().getNearbyEntities(as.getLocation(), 2, 2, 2))
                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;

                        if (!(livingEntity instanceof Player) && !(livingEntity instanceof ArmorStand))
                            if (as.getLocation().distanceSquared(livingEntity.getLocation()) <= 1) {
                                livingEntity.damage(Item.getDamage(is), player);
                                as.remove();
                                break;
                            }
                    }

            i.getAndIncrement();
        }, 0, 1);
    }

    /**
     * Checks if the player has a complete set equipped <p>
     * Use only with the ArmorEquipEvent <p>
     * <b>THE TARGET ARMOR MUST BE IN ORDER FROM BOOTS TO HELMET</b>
     * @param armorContents the armor the player has equipped
     * @param targetArmor the complete set of armor the player needs to have equipped
     * @param armorType the type of armor being equipped
     * @param armorPiece the new armor piece being worn
     * @return whether the player has the full set equipped or not
     */
    public static boolean hasFullSet(ItemStack[] armorContents, Item[] targetArmor, ArmorEquipEvent.ArmorType armorType, ItemStack armorPiece) {
        List<ItemMeta> armorMeta = new ArrayList<>();
        List<PersistentDataContainer> containers = new ArrayList<>();
        int amountOfArmor = 0;

        switch (armorType) {
            case BOOTS:
                armorContents[0] = armorPiece;
                break;
            case LEGGINGS:
                armorContents[1] = armorPiece;
                break;
            case CHESTPLATE:
                armorContents[2] = armorPiece;
                break;
            case HELMET:
                armorContents[3] = armorPiece;
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

        for(double x = target.getLocation().getX() - offset.getX(); x <= target.getLocation().getX() + offset.getX(); x++)
            for(double y = target.getLocation().getY() - offset.getY(); y <= target.getLocation().getY() + offset.getY(); y++)
                for(double z = target.getLocation().getZ() - offset.getZ(); z <= target.getLocation().getZ() + offset.getZ(); z++) {
                    Location loc = new Location(target.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }

        return blocks;
    }

    public static boolean validateItem(@NotNull ItemStack is, Item targetItem, Player player) {
        Item item = Item.toItem(is);
        if (item == null) return true;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        UUID uuid = null;
        if (item.hasRandomUUID())
            uuid = container.get(item.getKey(), new UUIDDataType());

        if (item.hasRandomUUID()) {
            if (!container.has(targetItem.getKey(), new UUIDDataType())) return true;
        } else {
            if (!container.has(targetItem.getKey(), PersistentDataType.INTEGER)) return true;
        }

        if (item.getDelay() > 0)
            if (item.hasRandomUUID()) {
                if (Cooldowns.checkCooldown(uuid, targetItem.getKey())) {
                    if (item.isShowDelay())
                        player.sendMessage(Cooldowns.inCooldownMessage(uuid, targetItem.getKey()));
                    return true;
                }
            } else {
                if (Cooldowns.checkCooldown(player.getUniqueId(), targetItem.getKey())) {
                    if (item.isShowDelay())
                        player.sendMessage(Cooldowns.inCooldownMessage(player.getUniqueId(), targetItem.getKey()));
                    return true;
                }
            }

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
}
