package davide.customitems.events;

import davide.customitems.CustomItems;
import davide.customitems.api.DelayedTask;
import davide.customitems.api.Instruction;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class Events {
    private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

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

        new DelayedTask(() -> {
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

        new DelayedTask(() -> {
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

    public static void teleportPlayer(Player player, int distance) {
        Block b = player.getTargetBlock(null, distance);
        Location loc = new Location(b.getWorld(), b.getX(), b.getY() + 0.5f, b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    public static void addPotionEffect(PotionEffectType type, int duration, int amplifier, LivingEntity e) {
        e.addPotionEffect(new PotionEffect(type, duration, amplifier));
    }

    public static void spawnLingeringPotion(PotionEffectType p, int duration, int amplifier, LivingEntity e, World world) {
        //(Entity) AreaEffectCloudApplyEvent et = world.spawnEntity(e.getLocation(), EntityType.AREA_EFFECT_CLOUD); // (new PotionEffect(p, duration, amplifier));
    }
}
