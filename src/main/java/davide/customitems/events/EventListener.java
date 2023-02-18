package davide.customitems.events;

import davide.customitems.api.*;
import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.util.Vector;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class EventListener implements Listener {
    private final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public EventListener(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Recipe Book
    @EventHandler
    private void onRightClickRecipeBook(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.recipeBook, player)) return;

        player.openInventory(ItemsGUI.itemInv.get(0));
    }

    //Stonk
    private static final HashMap<UUID, Integer> blocksMinedStonk = new HashMap<>();
    private static final int BLOCKS_TO_MINE_TOTAL = 250;

    @EventHandler
    private void onBlockBreakStonk(BlockBreakEvent e) {
        if (e.getBlock().isPassable()) return;
        if (SpecialBlocks.isClickableBlock(e.getBlock())) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.stonk, player)) return;

        Item item = Item.toItem(is);
        if (item == null) return;

        assert is.getItemMeta() != null;
        List<String> lore = is.getItemMeta().getLore();
        if (lore == null) return;

        if (!blocksMinedStonk.containsKey(item.getRandomUUID(is)))
            blocksMinedStonk.put(item.getRandomUUID(is), 0);

        blocksMinedStonk.put(item.getRandomUUID(is), blocksMinedStonk.get(item.getRandomUUID(is)) + 1);

        if (blocksMinedStonk.get(item.getRandomUUID(is)) == BLOCKS_TO_MINE_TOTAL)
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 4));

        int index = -1;
        for (String line : lore)
            if (line.contains("§e"))
                index = lore.indexOf(line);

        lore.set(index, "§e" + getBlocksRemainingStonk(is) + " §8blocks remaining");
        Item.setLore(is, lore);
    }

    public static int getBlocksRemainingStonk(ItemStack is) {
        return BLOCKS_TO_MINE_TOTAL - blocksMinedStonk.get(Objects.requireNonNull(Item.toItem(is)).getRandomUUID(is));
    }

    public static int getBlocksMaxStonk() {
        return BLOCKS_TO_MINE_TOTAL;
    }

    //Vein Pick
    @EventHandler
    private void onBlockBreakVeinPick(BlockBreakEvent e) {
        if (!SpecialBlocks.isOre(e.getBlock().getType())) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.veinPick, player)) return;

        Block block = e.getBlock();
        List<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 1, 1));
        checkForSameOresVeinPick(block.getType(), blocks);
    }

    private void checkForSameOresVeinPick(Material type, List<Block> blocks) {
        for (Block b : blocks)
            if (b.getType() == type) {
                b.breakNaturally();
                checkForSameOresVeinPick(type, Utils.getBlocksInRadius(b, new Vector3(1, 1, 1)));
            }
    }

    //Replenisher
    @EventHandler
    private void onCropBreakReplenisher(BlockBreakEvent e) {
        if (!(e.getBlock().getBlockData() instanceof Ageable age) || e.getBlock().getType() == Material.SUGAR_CANE) return;

        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.replenisher, player)) return;

        Block crop = e.getBlock();
        crop.getDrops(is, player).forEach(drop -> player.getWorld().dropItemNaturally(crop.getLocation(), drop));

        age.setAge(0);
        crop.setBlockData(age);

        e.setCancelled(true);
    }

    //Ultimate Bread
    @EventHandler
    private void onRightClickUltimateBread(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.ultimateBread, player)) return;

        final int duration = 300;
        int newDuration = player.hasPotionEffect(PotionEffectType.SATURATION) ? Objects.requireNonNull(player.getPotionEffect(PotionEffectType.SATURATION)).getDuration() + duration : duration;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration * 20, 0));
        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    //Cocaine
    private int cocaineUses = 1;
    private final HashMap<UUID, Integer> timesUsedInCooldown = new HashMap<>();

    @EventHandler
    private void onRightClickCocaine(PlayerInteractEvent e) {
        final int usesMax = 5;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.cocaine, player)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.cocaine.getKey()))
            cocaineUses++;
        else {
            Cooldowns.setCooldown(player.getUniqueId(), ItemList.cocaine.getKey(), ItemList.cocaine.getDelay());
            cocaineUses = 1;
        }

        timesUsedInCooldown.put(player.getUniqueId(), cocaineUses);

        switch (timesUsedInCooldown.get(player.getUniqueId())) {
            case usesMax -> {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    PotionEffectType type = effect.getType();

                    player.removePotionEffect(type);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 1));
            }
            case usesMax + 1 -> {
                player.sendMessage("Congratulations, you overdosed!");
                player.setHealth(0);
                e.setCancelled(true);
            }
            default -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 1));
            }
        }

        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    //Aspect Of The End
    @EventHandler
    private void onRightClickAOTE(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.aspectOfTheEnd, player)) return;

        Block b = player.getTargetBlock(null, 8);
        Location loc = new Location(b.getWorld(), b.getX(), b.getY() + 0.5f, b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    //Explosive Staff
    @EventHandler
    private void onRightClickExplosiveStaff(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.explosiveStaff, player)) return;

        player.getWorld().createExplosion(player.getLocation(), 3);
        is.setAmount(is.getAmount() - 1);
    }

    @EventHandler
    private void onDamageExplosiveStaff(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.explosiveStaff, player)) return;

        e.setDamage(0);
    }

    //Lightning Staff
    @EventHandler
    private void onRightClickLightningStaff(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock()))
                return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.lightningStaff, player)) return;

        World world = player.getWorld();
        RayTraceResult ray = player.rayTraceBlocks(192, FluidCollisionMode.SOURCE_ONLY);

        if (ray == null) return;
        world.strikeLightning(ray.getHitPosition().toLocation(world));
    }

    //Fire Staff
    @EventHandler
    private void onClickFireStaff(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.fireStaff, player)) return;

        double pitch = ((player.getLocation().getPitch() + 90) * Math.PI) / 180;
        double yaw = ((player.getLocation().getYaw() + 90) * Math.PI) / 180;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);

        Vector vector = new Vector(x, z, y);
        Location loc = player.getEyeLocation().toVector().add(player.getEyeLocation().getDirection()).toLocation(player.getWorld());
        Fireball fireball = player.getWorld().spawn(loc, Fireball.class);
        fireball.setDirection(vector.multiply(10));
        fireball.setBounce(false);
        fireball.setIsIncendiary(true);
        fireball.setYield(4);
    }

    //Midas Staff
    @EventHandler
    private void onHitMidasStaff(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.midasStaff, player)) return;

        LivingEntity hit = (LivingEntity) e.getEntity();
        hit.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, hit.getLocation(), 20, 0, 0, 0, 0.25);
        Block b = hit.getLocation().getBlock();
        b.setType(Material.GOLD_BLOCK);
        hit.remove();
    }

    @EventHandler
    private void enchantMidasStaff(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock()))
                return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.midasStaff, player)) return;

        assert is.getItemMeta() != null;
        if (!player.isSneaking()) return;
        Objects.requireNonNull(Item.toItem(is)).setGlint(!is.getItemMeta().hasEnchants(), is);
    }

    @EventHandler
    private void onWalkMidasStaff(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        int first = player.getInventory().first(ItemList.midasStaff.getItemStack().getType());
        ItemStack is = first == -1 ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(first);

        if (is == null) return;
        if (Utils.validateItem(is, ItemList.midasStaff, player)) return;

        if (!Objects.requireNonNull(Item.toItem(is)).isGlint()) return;
        Block b = player.getLocation().subtract(0, 1, 0).getBlock();
        Material type = b.getState().getType();

        if (!b.isPassable())
            if (b.getType() != Material.GOLD_BLOCK) {
                b.setType(Material.GOLD_BLOCK);
                Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(type), 2 * 20);
            }
    }

    //Judger
    @EventHandler
    private void onHitJudger(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity hit)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.judger, player)) return;

        if (hit.getHealth() - e.getDamage() < 0.15 * Objects.requireNonNull(hit.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue())
            hit.setHealth(0);
    }

    //Throwing Axe
    @EventHandler
    private void onRightClickThrowingAxe(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.throwingAxe, player)) return;

        Utils.throwItem(player, is, 25);

        assert is.getItemMeta() != null;
        Cooldowns.setCooldown(is.getItemMeta().getPersistentDataContainer().get(ItemList.throwingAxe.getKey(), new UUIDDataType()), ItemList.throwingAxe.getKey(), ItemList.throwingAxe.getDelay());
    }

    //Venomous Dagger
    @EventHandler
    private void onHitVenomousDagger(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity entity)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.venomousDagger, player)) return;

        Item item = Item.toItem(is);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1));

        assert item != null;
        Cooldowns.setCooldown(item.getRandomUUID(is), item.getKey(), item.getDelay());
    }

    //VampiresFang
    @EventHandler
    private void onHitVampiresFang(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.vampiresFang, player)) return;

        double amountToHeal = (e.getDamage() * 25) / 100;

        player.setHealth(player.getHealth() + amountToHeal);
    }

    //Caladbolg
    @EventHandler
    private void onRightClickCaladbolg(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (Utils.validateItem(is, ItemList.caladbolg, player)) return;

        Reforge reforge = Reforge.getReforge(is);

        is.setType(Material.NETHERITE_SWORD);

        if (reforge != null && reforge.getDamageModifier() > 0)
            Item.setDamage(Item.getDamage(is) * 2 + reforge.getDamageModifier() * 2, is);
        else
            Item.setDamage(Item.getDamage(is) * 2, is);

        Cooldowns.setCooldown(container.get(Objects.requireNonNull(Item.toItem(is)).getKey(), new UUIDDataType()), ItemList.caladbolg.getKey(), ItemList.caladbolg.getDelay());

        final short duration = 10;
        float delay = (ItemList.caladbolg.getDelay() - (ItemList.caladbolg.getDelay() - duration)) * 20;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Inventory inv = player.getInventory();
            ItemStack[] items = inv.getContents();

            for (ItemStack i : items)
                if (i != null) {
                    ItemMeta meta1 = i.getItemMeta();

                    if (meta1 != null) {
                        PersistentDataContainer container1 = meta1.getPersistentDataContainer();

                        if (Objects.equals(container1.get(ItemList.caladbolg.getKey(), new UUIDDataType()), is.getItemMeta().getPersistentDataContainer().get(Objects.requireNonNull(Item.toItem(is)).getKey(), new UUIDDataType()))) {
                            Reforge r = Reforge.getReforge(i);

                            if (r != null && r.getDamageModifier() > 0)
                                Item.setDamage(Item.getDamage(i), i, r);
                            else
                                Item.setDamage(Item.getDamage(i), i);

                            i.setType(Material.DIAMOND_SWORD);
                            break;
                        }
                    }
                }
        }, (long) delay);
    }

    //Soul Bow
    private String soulBowUUID;

    @EventHandler
    private void onShootSoulBow(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        LivingEntity hit = (LivingEntity) e.getHitEntity();
        if (hit == null) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.soulBow, player)) return;

        if (player.getHealth() > 3)
            player.setHealth(player.getHealth() - 3);
        else {
            player.sendMessage("§cYou don't have enough health to do that!");
            return;
        }

        Wolf wolf = (Wolf) hit.getWorld().spawnEntity(hit.getLocation(), EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(player);
        wolf.setAdult();

        soulBowUUID = player.getUniqueId().toString();
        wolf.addScoreboardTag("wolf");
        player.addScoreboardTag(soulBowUUID);
    }

    @EventHandler
    private void onKillSoulBow(EntityDeathEvent e) {
        for (Entity w : e.getEntity().getNearbyEntities(10, 10, 10))
            if (w instanceof Wolf wolf)
                if (wolf.getScoreboardTags().contains("wolf")) {
                    Player owner = (Player) wolf.getOwner();
                    assert owner != null;
                    if (owner.getScoreboardTags().contains(soulBowUUID)) {
                        wolf.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, wolf.getLocation(), 20, 0, 0, 0, 0.25);
                        wolf.remove();
                    }
                }
    }

    //Short Bow
    @EventHandler
    private void onClickShortBow(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.shortBow, player)) return;

        e.setCancelled(true);

        ItemStack[] contents = player.getInventory().getStorageContents();

        player.launchProjectile(Arrow.class);

        for (ItemStack i : contents)
            if (player.getGameMode() != GameMode.CREATIVE && i != null && i.getType() == Material.ARROW) {
                i.setAmount(i.getAmount() - 1);
                break;
            }
    }

    //Grappling Hook
    @EventHandler
    private void onFishGrapplingHook(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.grapplingHook, player)) return;

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.3).setY(1));

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.grapplingHook.getKey(), ItemList.grapplingHook.getDelay());
    }

    //Hook Shot
    @EventHandler
    private void onFishHookShot(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.hookShot, player)) return;

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.75).setY(1.25));

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.hookShot.getKey(), ItemList.hookShot.getDelay());
    }

    //Slime Boots
    @EventHandler
    private void onFallSlimeBoots(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.slimeBoots, player)) return;

        if (player.getVelocity().getY() > -0.515) return;

        for (int i = 0; i < 5; i++) {
            Block block = player.getLocation().subtract(0, i, 0).getBlock();

            if (block.getType() == Material.SLIME_BLOCK) return;
            if (block.getType() != Material.AIR && block.getType() != Material.CAVE_AIR && !block.isPassable()) {
                ArrayList<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 0, 1));
                ArrayList<Material> states = new ArrayList<>();

                for (Block b : blocks)
                    states.add(b.getState().getType());

                for (int j = 0; j < blocks.size(); j++) {
                    if (blocks.get(j).getType() != Material.SLIME_BLOCK) {
                        blocks.get(j).setType(Material.SLIME_BLOCK);

                        int finalJ = j;
                        Bukkit.getScheduler().runTaskLater(plugin, () -> blocks.get(finalJ).setType(states.get(finalJ)), 2 * 20);
                    }
                }

                break;
            }
        }
    }

    //Spring Boots
    @EventHandler
    private void onJumpSpringBoots(PlayerJumpEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.springBoots, player)) return;

        if (player.isSneaking())
            player.setVelocity(player.getVelocity().multiply(0.5).setY(1));
    }

    //Farmer Boots
    @EventHandler
    private void onTrampleFarmerBoots(CropTrampleEvent e) {
        if (!(e.getTrampler() instanceof Player player)) return;
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.farmerBoots, player)) return;

        e.setCancelled(true);
    }

    //Shelmet
    @EventHandler
    private void onPlayerHitShelmet(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        ItemStack is = player.getInventory().getHelmet();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.shelmet, player)) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector()), 1);
    }

    final Item[] speedTargetArmor = {ItemList.speedHelmet, ItemList.speedChestplate, ItemList.speedLeggings, ItemList.speedBoots};
    //Speed Armor
    @EventHandler
    private void onEquipArmorSpeedArmor(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
            Player player = e.getPlayer();
            ItemStack is = e.getNewArmorPiece();
            if (Utils.validateArmor(is, speedTargetArmor)) return;

            player.setWalkSpeed(player.getWalkSpeed() + (0.1f * 0.2f));
        }
    }

    @EventHandler
    private void onUnequipSpeedArmor(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
            Player player = e.getPlayer();
            ItemStack is = e.getOldArmorPiece();
            if (Utils.validateArmor(is, speedTargetArmor)) return;

            player.setWalkSpeed(player.getWalkSpeed() - (0.1f * 0.2f));
        }
    }

    //Protector Armor
    @EventHandler
    private void onDamageProtectorArmor(EntityDamageEvent e) {
        final Item[] targetArmor = { ItemList.protectorBoots, ItemList.protectorLeggings, ItemList.protectorChestplate, ItemList.protectorHelmet };

        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        if (!Utils.hasFullSet(armorContents, targetArmor))
            return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
            return;

        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        if (e.getDamage() > 0.25 * maxHealth) return;

        e.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.sendMessage("§cYou have been protected!");

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getDelay());
    }

    //Fire Talisman
    @EventHandler
    private void onFireDamageFireTalisman(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.LAVA) return;

        int first = player.getInventory().first(ItemList.fireTalisman.getItemStack().getType());
        ItemStack is = first == -1 ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(first);

        if (is == null) return;
        if (Utils.validateItem(is, ItemList.fireTalisman, player)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

        is.setAmount(is.getAmount() - 1);
        Cooldowns.setCooldown(player.getUniqueId(), ItemList.fireTalisman.getKey(), ItemList.fireTalisman.getDelay());
    }

    @EventHandler
    private void onEatFireTalisman(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (Utils.validateItem(is, ItemList.fireTalisman, player)) return;

        e.setCancelled(true);
    }

    //Fire Armor
    @EventHandler
    private void onEquipFireArmor(ArmorEquipEvent e) {
        final Item[] targetArmor = { ItemList.fireBoots, ItemList.fireLeggings, ItemList.fireChestplate, ItemList.fireHelmet };

        if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        ItemStack is = e.getNewArmorPiece();
        if (Utils.validateArmor(is, targetArmor)) return;

        if (!Utils.hasFullSet(armorContents, targetArmor, e.getType(), e.getNewArmorPiece()))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Utils.hasFullSet(player.getInventory().getArmorContents(), targetArmor))
                    this.cancel();

                List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                if (!entities.isEmpty())
                    for (Entity entity : entities)
                        if (entity instanceof LivingEntity livingEntity) {

                            if (livingEntity.getFireTicks() <= 1)
                                livingEntity.setFireTicks(20);
                        }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    //Cheat Code
    @EventHandler
    private void onRightClickCheatCode(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.cheatCode, player)) return;

        if (player.getGameMode() == GameMode.CREATIVE)
            player.setGameMode(GameMode.SURVIVAL);
        else
            player.setGameMode(GameMode.CREATIVE);
    }
}
