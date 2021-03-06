package davide.customitems.events;

import davide.customitems.api.*;
import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.gui.GUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
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

    public EventListener(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Aspect Of The End
    @EventHandler
    private void onRightClickAOTE(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

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

    //Caladbolg
    @EventHandler
    private void onRightClickCaladbolg(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

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
        int delay = (ItemList.caladbolg.getDelay() - (ItemList.caladbolg.getDelay() - duration)) * 20;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> {
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
                                Item.setDamage(Item.getDamage(i) / 2, i, r);
                            else
                                Item.setDamage(Item.getDamage(i) / 2, i);

                            i.setType(Material.DIAMOND_SWORD);
                            break;
                        }
                    }
                }
        }, delay);
    }

    //Cocaine
    private int cocaineUses = 1;
    private final HashMap<UUID, Integer> timesUsedInCooldown = new HashMap<>();

    @EventHandler
    private void onRightClickCocaine(PlayerInteractEvent e) {
        final int usesMax = 5;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

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
            case usesMax:
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    PotionEffectType type = effect.getType();

                    player.removePotionEffect(type);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 1));
                break;

            case usesMax + 1:
                player.sendMessage("Congratulations, you overdosed!");
                player.setHealth(0);
                e.setCancelled(true);
                break;

            default:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 1));
                break;
        }

        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    //Explosive Wand
    @EventHandler
    private void onRightClickExplosiveWand(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.explosiveWand, player)) return;

        player.getWorld().createExplosion(player.getLocation(), 3);
        is.setAmount(is.getAmount() - 1);
    }

    @EventHandler
    private void onDamageExplosiveWand(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
        Player player = (Player) e.getEntity();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.explosiveWand, player)) return;

        e.setDamage(0);
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
                            if (entity instanceof LivingEntity) {
                                LivingEntity livingEntity = (LivingEntity) entity;

                                if (livingEntity.getFireTicks() <= 1)
                                    livingEntity.setFireTicks(20);
                            }
                }
            }.runTaskTimer(CustomItems.getPlugin(CustomItems.class), 0, 1);
    }

    //Fire Talisman
    @EventHandler
    private void onFireDamageFireTalisman(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.LAVA) return;

        Player player = (Player) e.getEntity();

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

    //Farmer Boots
    @EventHandler
    private void onTrampleFarmerBoots(CropTrampleEvent e) {
        if (!(e.getTrampler() instanceof Player)) return;
        Player player = (Player) e.getTrampler();
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.farmerBoots, player)) return;

        e.setCancelled(true);
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

    //Judger
    @EventHandler
    private void onHitJudger(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity hit = (LivingEntity) e.getEntity();
        Player player = (Player) e.getDamager();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.judger, player)) return;

        if (hit.getHealth() - e.getDamage() < 0.15 * Objects.requireNonNull(hit.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue())
            hit.setHealth(0);
    }

    //Lightning Staff
    @EventHandler
    private void onRightClickLightningStaff(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType()))
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

    //Midas Staff
    @EventHandler
    private void onHitMidasStaff(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        LivingEntity hit = (LivingEntity) e.getEntity();

        Player player = (Player) e.getDamager();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.midasStaff, player)) return;

        hit.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, hit.getLocation(), 20, 0, 0, 0, 0.25);
        Block b = hit.getLocation().getBlock();
        b.setType(Material.GOLD_BLOCK);
        hit.remove();
    }

    @EventHandler
    private void enchantMidasStaff(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType()))
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
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> b.setType(type), 2 * 20);
            }
    }

    //Protector Armor
    @EventHandler
    private void onDamageProtectorArmor(EntityDamageEvent e) {
        final Item[] targetArmor = { ItemList.protectorBoots, ItemList.protectorLeggings, ItemList.protectorChestplate, ItemList.protectorHelmet };

        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        ItemStack[] armorContents = player.getInventory().getArmorContents();

        if (!Utils.hasFullSet(armorContents, targetArmor))
            return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
            return;

        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        if (e.getDamage() > 0.25 * maxHealth) return;

        e.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.sendMessage("??cYou have been protected!");

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getDelay());
    }

    //Recipe Book
    @EventHandler
    private void onRightClickRecipeBook(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.recipeBook, player)) return;

        player.openInventory(GUI.itemInv);
    }

    //Short Bow
    @EventHandler
    private void onClickShortBow(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

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
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> blocks.get(finalJ).setType(states.get(finalJ)), 2 * 20);
                    }
                }

                break;
            }
        }
    }

    //Soul Bow
    private String soulBowUUID;

    @EventHandler
    private void onShootSoulBow(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) e.getEntity();
        if (!(arrow.getShooter() instanceof Player)) return;
        LivingEntity hit = (LivingEntity) e.getHitEntity();
        if (hit == null) return;
        Player player = (Player) arrow.getShooter();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.soulBow, player)) return;

        if (player.getHealth() > 3)
            player.setHealth(player.getHealth() - 3);
        else {
            player.sendMessage("??cYou don't have enough health to do that!");
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
        LivingEntity shot = e.getEntity();

        for (Entity w : shot.getLocation().getChunk().getEntities())
            if (w instanceof Wolf) {
                Wolf wolf1 = (Wolf) w;

                if (wolf1.getScoreboardTags().contains("wolf")) {
                    Player owner = (Player) wolf1.getOwner();
                    assert owner != null;
                    if (owner.getScoreboardTags().contains(soulBowUUID)) {
                        wolf1.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, wolf1.getLocation(), 20, 0, 0, 0, 0.25);
                        wolf1.remove();
                    }
                }
            }
    }

    //Speed Armor
    @EventHandler
    private void onEquipArmorSpeedArmor(ArmorEquipEvent e) {
        final Item[] targetArmor = {ItemList.speedHelmet, ItemList.speedChestplate, ItemList.speedLeggings, ItemList.speedBoots};

        if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
            Player player = e.getPlayer();
            ItemStack is = e.getNewArmorPiece();
            if (Utils.validateArmor(is, targetArmor)) return;

            player.setWalkSpeed(player.getWalkSpeed() + 0.1f);
        }
        else if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
            Player player = e.getPlayer();
            ItemStack is = e.getOldArmorPiece();
            if (Utils.validateArmor(is, targetArmor)) return;

            player.setWalkSpeed(player.getWalkSpeed() - 0.1f);
        }
    }

    //Spring Boots
    @EventHandler
    private void onJumpSpringBoots(PlayerJumpEvent e) {
        Player player = e.getPlayer();

        if (player.isSneaking())
            player.setVelocity(player.getVelocity().multiply(0.5).setY(3));
    }

    //Stonk
    private static final HashMap<UUID, Integer> blocksMinedStonk = new HashMap<>();
    private static final int BLOCKS_TO_MINE_TOTAL = 250;

    @EventHandler
    private void onBlockBreakStonk(BlockBreakEvent e) {
        if (e.getBlock().isPassable()) return;
        if (SpecialBlocks.isClickableBlock(e.getBlock().getType())) return;

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
            if (line.contains("??e"))
                index = lore.indexOf(line);

        lore.set(index, "??e" + getBlocksRemainingStonk(is) + " ??8blocks remaining");
        Item.setLore(lore, is);
    }

    public static int getBlocksRemainingStonk(ItemStack is) {
        return BLOCKS_TO_MINE_TOTAL - blocksMinedStonk.get(Objects.requireNonNull(Item.toItem(is)).getRandomUUID(is));
    }

    public static int getBlocksMaxStonk() {
        return BLOCKS_TO_MINE_TOTAL;
    }

    //Throwing Axe
    @EventHandler
    private void onRightClickThrowingAxe(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.throwingAxe, player)) return;

        Utils.throwItem(player, is, 25);

        assert is.getItemMeta() != null;
        Cooldowns.setCooldown(is.getItemMeta().getPersistentDataContainer().get(ItemList.throwingAxe.getKey(), new UUIDDataType()), ItemList.throwingAxe.getKey(), ItemList.throwingAxe.getDelay());
    }

    //Ultimate Bread
    @EventHandler
    private void onRightClickUltimateBread(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.ultimateBread, player)) return;

        final int duration = 300;
        int newDuration = player.hasPotionEffect(PotionEffectType.SATURATION) ? Objects.requireNonNull(player.getPotionEffect(PotionEffectType.SATURATION)).getDuration() + duration : duration;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration * 20, 0));
        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    //VampiresFang
    @EventHandler
    private void onHitVampiresFang(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getDamager();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.vampiresFang, player)) return;

        double amountToHeal = (e.getDamage() * 25) / 100;

        player.setHealth(player.getHealth() + amountToHeal);
    }
}
