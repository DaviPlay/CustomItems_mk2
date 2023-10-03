package davide.customitems.events;

import davide.customitems.api.*;
import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Rarity;
import davide.customitems.lists.ItemList;
import davide.customitems.playerStats.ChanceManager;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EventListener implements Listener {
    private static CustomItems plugin;

    public EventListener(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        EventListener.plugin = plugin;
    }

    byte recCeil;
    private void recursiveBreakBlock(Material type, List<Block> blocks, ItemStack is) {
        for (Block b : blocks)
            if (b.getType() == type) {
                b.breakNaturally(is);
                if (recCeil == 24) return;
                Bukkit.getScheduler().runTaskLater(plugin, () -> recursiveBreakBlock(type, Utils.getBlocksInRadius(b, new Vector3(1, 1, 1)), is), 2);
                recCeil++;
            }
    }

    //Rune Shard
    @EventHandler
    private void onBlockBreakRuneShard(BlockBreakEvent e) {
        if (!SpecialBlocks.isOre(e.getBlock().getType()) && !SpecialBlocks.isStone(e.getBlock().getType())) return;
        Block block = e.getBlock();
        double dropChance;

        switch (e.getBlock().getType()) {
            case COAL_ORE, DEEPSLATE_COAL_ORE, COPPER_ORE, DEEPSLATE_COPPER_ORE -> dropChance = 0.1;
            case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE, LAPIS_ORE, DEEPSLATE_LAPIS_ORE, NETHER_QUARTZ_ORE, NETHER_GOLD_ORE -> dropChance = 0.5;
            case IRON_ORE, DEEPSLATE_IRON_ORE -> dropChance = 1;
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> dropChance = 2;
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> dropChance = 5;
            case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> dropChance = 10;
            case ANCIENT_DEBRIS -> dropChance = 50;
            default -> dropChance = 0.005;
        }

        ChanceManager.chanceCalculation(dropChance, () -> block.getWorld().dropItemNaturally(block.getLocation(), ItemList.runeShard.getItemStack()), e.getPlayer());
    }

    //Reforge Stone
    @EventHandler
    private void onAnvilPrepareReforgeStone(PrepareAnvilEvent e) {
        ItemStack stone = e.getInventory().getItem(1);
        if (stone == null) return;
        if (Utils.validateItem(stone, ItemList.reforgeStone, (Player) e.getInventory().getViewers().get(0), e)) return;

        ItemStack is = e.getInventory().getItem(0);
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        Reforge reforge;
        ItemStack result = is.clone();

        reforge = Reforge.randomReforge(item.getType());

        if (reforge == null) return;
        Reforge.setReforge(reforge, result);
        e.setResult(result);
        new DelayedTask(() -> e.getInventory().setRepairCost(3));
    }

    //Recombobulator
    @EventHandler
    private void onAnvilPrepareRecomb(PrepareAnvilEvent e) {
        ItemStack recomb = e.getInventory().getItem(1);
        if (recomb == null) return;
        if (Utils.validateItem(recomb, ItemList.recombobulator, (Player) e.getInventory().getViewers().get(0), e)) return;

        ItemStack is = e.getInventory().getItem(0);
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemStack result = is.clone();
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta == null) return;
        PersistentDataContainer container = resultMeta.getPersistentDataContainer();
        if (container.has(new NamespacedKey(plugin, "recombed"), PersistentDataType.BOOLEAN)) return;
        if (Item.getRarity(result).ordinal() + 1 == Rarity.values().length - 1) return;

        Utils.recombItem(result, is);

        e.setResult(result);
        new DelayedTask(() -> e.getInventory().setRepairCost(3));
    }

    //Recipe Book
    @EventHandler
    private void onRightClickRecipeBook(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.recipeBook, player, e)) return;

        new ItemsGUI(player);
    }

    //Stonk
    private static final int BLOCKS_TO_MINE_TOTAL_STONK = 250;

    @EventHandler
    private void onBlockBreakStonk(BlockBreakEvent e) {
        if (e.getBlock().isPassable()) return;
        if (SpecialBlocks.isClickableBlock(e.getBlock())) return;
        if (!SpecialBlocks.isOre(e.getBlock().getType()) && !SpecialBlocks.isStone(e.getBlock().getType())) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        if (Utils.validateItem(is, ItemList.stonk, player, e)) return;
        NamespacedKey key = new NamespacedKey(plugin, "blocks_mined");

        if (!container.has(key, PersistentDataType.INTEGER))
            container.set(key, PersistentDataType.INTEGER, 0);

        int blocksMined = container.get(key, PersistentDataType.INTEGER);

        container.set(key, PersistentDataType.INTEGER, blocksMined + 1);

        if (blocksMined == BLOCKS_TO_MINE_TOTAL_STONK - 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 4));
            container.set(key, PersistentDataType.INTEGER, 0);
        }

        is.setItemMeta(meta);

        int index = -1;
        for (String line : lore)
            if (line.contains("§e"))
                index = lore.indexOf(line);

        lore.set(index, "§e" + getBlocksRemainingStonk(is) + " §8blocks remaining");
        Item.setLore(is, lore);
    }

    private int getBlocksRemainingStonk(ItemStack is) {
        return BLOCKS_TO_MINE_TOTAL_STONK - is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER);
    }

    public static int getBlocksMaxStonk() {
        return BLOCKS_TO_MINE_TOTAL_STONK;
    }

    //Vein Pick
    @EventHandler
    private void onBlockBreakVeinPick(BlockBreakEvent e) {
        if (!SpecialBlocks.isOre(e.getBlock().getType())) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.veinPick, player, e)) return;

        Block block = e.getBlock();
        List<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 1, 1));
        recCeil = 0;
        recursiveBreakBlock(block.getType(), blocks, is);
    }

    //Severed Drill
    @EventHandler
    private void onBlockBreakSeveredDrill(BlockBreakEvent e) {
        if (!SpecialBlocks.isStone(e.getBlock().getType()) && !SpecialBlocks.isOre(e.getBlock().getType())) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.severedDrill, player, e)) return;

        List<Block> blocks = Utils.getBlocksInRadius(e.getBlock(), new Vector3(1, 1, 1));
        byte maxBlocks = 2, chanceToBreak = 10;
        for (Block b : blocks) {
            if (new Random().nextInt(100) < chanceToBreak) {
                b.breakNaturally(is);
                maxBlocks--;
            }
            if (maxBlocks == 0) return;
        }
    }

    //Mended Drill
    @EventHandler
    private void onBlockBreakMendedDrill(BlockBreakEvent e) {
        if (!SpecialBlocks.isStone(e.getBlock().getType()) && !SpecialBlocks.isOre(e.getBlock().getType())) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.mendedDrill, player, e)) return;

        List<Block> blocks = Utils.getBlocksInRadius(e.getBlock(), new Vector3(1, 1, 1));
        byte maxBlocks = 5, chanceToBreak = 25;
        for (Block b : blocks) {
            if (new Random().nextInt(100) < chanceToBreak) {
                b.breakNaturally(is);
                maxBlocks--;
            }
            if (maxBlocks == 0) return;
        }
    }

    //Treecapitator
    @EventHandler
    private void onBlockBreakTreecap(BlockBreakEvent e) {
        if (!SpecialBlocks.isLog(e.getBlock().getType())) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.treecapitator, player, e)) return;

        Block block = e.getBlock();
        List<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 1, 1));
        recCeil = 0;
        recursiveBreakBlock(block.getType(), blocks, is);
    }

    //Replenisher
    @EventHandler
    private void onCropBreakReplenisher(BlockBreakEvent e) {
        if (!(e.getBlock().getBlockData() instanceof Ageable age)) return;
        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.replenisher, player, e)) return;

        Block crop = e.getBlock();
        String cropName = crop.getType().name();
        switch (cropName) {
            case "POTATOES" -> cropName = "POTATO";
            case "CARROTS" -> cropName = "CARROT";
            case "BEETROOTS" -> cropName = "BEETROOT";
            case "SWEET_BERRY_BUSH" -> cropName = "SWEET_BERRIES";
        }

        for (ItemStack i : player.getInventory().getStorageContents()) {
            try {
                if (i.getType() == Material.matchMaterial(cropName + "_SEEDS") || i.getType() == Material.matchMaterial(cropName) || i.getType() == Material.matchMaterial(cropName.replace("STEM", "SEEDS"))) {
                    i.setAmount(i.getAmount() - 1);

                    crop.getDrops(is, player).forEach(drop -> {
                        if (drop.getType() != Material.AIR)
                            player.getWorld().dropItemNaturally(crop.getLocation(), drop);
                    });

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        age.setAge(0);
                        crop.setBlockData(age);

                        e.setCancelled(true);
                    }, 5);
                    return;
                }
            } catch (NullPointerException ignored) {}
        }
    }

    //Ultimate Bread
    @EventHandler
    private void onRightClickUltimateBread(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.ultimateBread, player, e)) return;

        final int duration = 300 * 20; // effect duration in seconds * ticks
        int newDuration = player.hasPotionEffect(PotionEffectType.SATURATION) ? player.getPotionEffect(PotionEffectType.SATURATION).getDuration() + duration : duration;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration, 0));
        is.setAmount(is.getAmount() - 1);
    }

    //Meth
    private int cocaineUses = 1;
    private final HashMap<UUID, Integer> timesUsedInCooldown = new HashMap<>();

    @EventHandler
    private void onRightClickMeth(PlayerInteractEvent e) {
        final int usesMax = 5;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.meth, player, e)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), Item.toItem(is).getAbilities().get(0).key())) {
            cocaineUses++;
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
        }

        e.getItem().setAmount(e.getItem().getAmount() - 1);
    }

    //Aspect Of The End
    @EventHandler
    private void onRightClickAOTE(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.aspectOfTheEnd, player, e)) return;

        Block b = player.getTargetBlock(null, 8);
        Location loc = new Location(b.getWorld(), b.getX(), b.getY() + 0.5f, b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(loc);
        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
    }

    //Explosive Staff
    @EventHandler
    private void onRightClickExplosiveStaff(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.explosiveStaff, player, e)) return;

        player.getWorld().createExplosion(player.getLocation(), 3);
        is.setAmount(is.getAmount() - 1);
    }

    @EventHandler
    private void onDamageExplosiveStaff(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.explosiveStaff, player, e)) return;

        e.setDamage(0);
    }

    //Lightning Staff
    @EventHandler
    private void onClickLightningStaff(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.lightningStaff, player, e)) return;

        RayTraceResult ray = player.rayTraceBlocks(192, FluidCollisionMode.SOURCE_ONLY);
        if (ray == null) return;
        World world = player.getWorld();
        world.strikeLightning(ray.getHitPosition().toLocation(world));
    }

    //Fire Staff
    @EventHandler
    private void onClickFireStaff(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.fireStaff, player, e)) return;

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
        if (Utils.validateItem(is, ItemList.midasStaff, player, 0, e)) return;

        LivingEntity hit = (LivingEntity) e.getEntity();
        hit.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, hit.getLocation(), 20, 0, 0, 0, 0.25);
        Block b = hit.getLocation().getBlock();
        b.setType(Material.GOLD_BLOCK);
        hit.remove();
    }

    @EventHandler
    private void enchantMidasStaff(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.midasStaff, player, 1, e)) return;

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
        if (Utils.validateItem(is, ItemList.midasStaff, player, e)) return;

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
        if (Utils.validateItem(is, ItemList.judger, player, e)) return;

        if (hit.getHealth() - e.getDamage() < 0.15 * Objects.requireNonNull(hit.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue())
            hit.setHealth(0);
    }

    //Throwing Axe
    @EventHandler
    private void onRightClickThrowingAxe(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.throwingAxe, player, e)) return;

        Utils.throwItem(player, is, 25);
    }

    //Venomous Dagger
    @EventHandler
    private void onHitVenomousDagger(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity entity)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.venomousDagger, player, e)) return;

        entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1));
    }

    //VampiresFang
    @EventHandler
    private void onHitVampiresFang(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.vampiresFang, player, e)) return;

        double amountToHeal = Math.min(((e.getDamage() * 25) / 100), player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.setHealth(player.getHealth() + amountToHeal);
    }

    //Shadow Fury
    List<Enemy> enemies;
    int defaultCritChance;
    DelayedTask shadowFuryTask;
    @EventHandler
    private void onRightClickShadowFury(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (Utils.validateItem(is, ItemList.shadowFury, player, e)) return;
        defaultCritChance = Item.getCritChance(is);

        final short[] i = {0};
        enemies = new ArrayList<>();
        for (Entity enemy : player.getNearbyEntities(16, 4, 16))
            if (enemy instanceof Enemy en) {
                enemies.add(en);
                if (i[0]++ == 4)
                    break;
            }

        if (enemies.isEmpty()) {
            player.sendMessage("§cThere are no enemies nearby!");
            return;
        }

        Item.setStats(Item.getDamage(is), 100, Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
        final int[] k = {0};

        shadowFuryTask = new DelayedTask(() -> {
            Enemy enemy = enemies.get(k[0]);
            tpBehindEntitiesShadowFury(player, enemy);
            player.attack(enemy);
            k[0]++;
        }, 0, 15);
    }

    byte hits = 0;
    @EventHandler
    private void onEntityDamageShadowFury(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ItemStack is = Utils.findCustomItemInInv(ItemList.shadowFury, player.getInventory());
        if (Item.getCritChance(is) != 100) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;

        if (hits++ == enemies.size() - 1) {
            shadowFuryTask.cancel();
            Item.setStats(Item.getDamage(is), defaultCritChance, Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
            hits = 0;
        }
    }

    @EventHandler
    private void onDropShadowFury(PlayerDropItemEvent e) {
        if (!Item.isCustomItem(e.getItemDrop().getItemStack()) || !Item.toItem(e.getItemDrop().getItemStack()).equals(ItemList.shadowFury)) return;
        ItemStack is = e.getItemDrop().getItemStack();
        if (Item.getCritChance(is) != 100) return;

        shadowFuryTask.cancel();
        Item.setStats(Item.getDamage(is), defaultCritChance, Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
    }

    private void tpBehindEntitiesShadowFury(Player player, @NotNull Enemy enemy) {
        Location enemyLoc = enemy.getLocation();
        double newX;
        double newZ;
        float nang = enemyLoc.getYaw() + 90;
        if (nang < 0) nang += 360;

        newX = Math.cos(Math.toRadians(nang));
        newZ = Math.sin(Math.toRadians(nang));
        Location newPlayerLoc = new Location(enemyLoc.getWorld(), enemyLoc.getX() - newX, enemyLoc.getY(), enemyLoc.getZ() - newZ, enemyLoc.getYaw(), enemyLoc.getPitch());

        player.teleport(newPlayerLoc);
    }

    //Caladbolg
    DelayedTask caladbolgTask;
    @EventHandler
    private void onRightClickCaladbolg(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        if (Utils.validateItem(is, ItemList.caladbolg, player, e)) return;

        is.setType(Material.DIAMOND_SWORD);
        Item.setStats(Item.getDamage(is) * 2, Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);

        caladbolgTask = new DelayedTask(() -> {
            ItemStack i = Utils.findCustomItemInInv(ItemList.caladbolg, player.getInventory());
            if (i == null) return;
            ItemMeta meta1 = i.getItemMeta();
            if (meta1 == null) return;
            PersistentDataContainer container1 = meta1.getPersistentDataContainer();

            if (Objects.equals(container1.get(ItemList.caladbolg.getKey(), new UUIDDataType()), is.getItemMeta().getPersistentDataContainer().get(Objects.requireNonNull(Item.toItem(is)).getKey(), new UUIDDataType()))) {
                Item.setStats(Item.getDamage(i) / 2, Item.getCritChance(i), Item.getCritDamage(i), Item.getHealth(i), Item.getDefence(i), i, false);
                i.setType(ItemList.caladbolg.getItemStack().getType());
            }
        }, 10 * 20); // duration of ability * ticks

        Item item = Item.toItem(is);
        assert item != null;
    }

    @EventHandler
    private void onDropCaladbolg(PlayerDropItemEvent e) {
        if (!Item.isCustomItem(e.getItemDrop().getItemStack()) || !Item.toItem(e.getItemDrop().getItemStack()).equals(ItemList.caladbolg)) return;
        ItemStack is = e.getItemDrop().getItemStack();
        if (is.getType() != Material.DIAMOND_SWORD) return;

        caladbolgTask.cancel();
        Item.setStats(Item.getDamage(is) / 2, Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
        is.setType(ItemList.caladbolg.getItemStack().getType());
    }

    @EventHandler
    private void onLeftClickBlueZenith(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.blueZenith, player, e)) return;

        RayTraceResult ray = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 8.5, (entity -> !entity.equals(player)));
        if (ray == null) return;
        if (ray.getHitEntity() == null) return;

        player.attack(ray.getHitEntity());
    }

    DelayedTask mjolnirTask;
    @EventHandler
    private void onDamageMjolnir(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) return;
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack is = Utils.findCustomItemInInv(ItemList.mjolnir, player.getInventory());
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.mjolnir, player, 2, e)) return;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "charges");

        if (mjolnirTask != null)
            mjolnirTask.cancel();

        if (!container.has(key, PersistentDataType.INTEGER))
            container.set(key, PersistentDataType.INTEGER, 0);

        int charges = container.get(key, PersistentDataType.INTEGER);
        if (charges > 19) return;

        setChargesMjolnir(is, charges + 1);

        mjolnirTask = new DelayedTask(() -> {
            ItemStack i = Utils.findCustomItemInInv(ItemList.mjolnir, player.getInventory());
            if (i == null) return;
            ItemMeta meta1 = is.getItemMeta();
            assert meta1 != null;
            PersistentDataContainer container1 = meta1.getPersistentDataContainer();
            NamespacedKey key1 = new NamespacedKey(plugin, "charges");

            if (!container1.has(key1, PersistentDataType.INTEGER))
                container1.set(key1, PersistentDataType.INTEGER, 0);

            int charges1 = container1.get(key1, PersistentDataType.INTEGER);
            if (charges1 == 0) mjolnirTask.cancel();

            setChargesMjolnir(i, charges1 - 1);
        }, 10 * 20, 10 * 20); // charge decay rate * ticks
    }

    public void setChargesMjolnir(ItemStack is, int charges) {
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "charges");

        if (!container.has(key, PersistentDataType.INTEGER))
            container.set(key, PersistentDataType.INTEGER, 0);

        int currentCharges = container.get(key, PersistentDataType.INTEGER);
        if (charges == currentCharges) return;

        container.set(key, PersistentDataType.INTEGER, charges);
        is.setItemMeta(meta);

        if (charges > currentCharges)
            for (int i = currentCharges; i < charges; i++) {
                switch (i + 1) {
                    case 2, 6, 12, 20 -> Item.setStats((int) Math.floor(Item.getDamage(is) * 1.25f), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
                }
                ItemMeta meta1 = is.getItemMeta();
                List<String> lore = meta1.getLore();
                if (lore == null) return;
                for (String line : lore)
                    if (line.contains("[")) {
                        int index = lore.indexOf(line);
                        String newLine = line.replaceFirst("§8", "§b");
                        lore.set(index, newLine);
                        Item.setLore(is, lore);
                        break;
                    }
            }
        else
            for (int i = currentCharges; i > charges; i--) {
                switch (i - 1) {
                    case 1, 5, 11, 19 -> Item.setStats((int) Math.ceil(Item.getDamage(is) / 1.25f), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
                }
                ItemMeta meta1 = is.getItemMeta();
                List<String> lore = meta1.getLore();
                if (lore == null) return;
                for (String line : lore)
                    if (line.contains("[")) {
                        int index = lore.indexOf(line);
                        int last = line.lastIndexOf("§b");
                        String newLine = line.substring(0, last) + "§8" + line.substring(last + 2);
                        lore.set(index, newLine);
                        Item.setLore(is, lore);
                        break;
                    }
            }
    }

    @EventHandler
    private void onHitMjolnir(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!(e.getEntity() instanceof LivingEntity entity)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.mjolnir, player, 0, e)) return;
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER)) return;
        if (container.get(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER) != 20) return;

        entity.getWorld().strikeLightning(entity.getLocation());
    }

    @EventHandler
    private void onRightClickMjolnir(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.mjolnir, player, 1, e)) return;

        Utils.throwItem(player, is, new Instruction() {

            public void run(Entity entity) {
                ItemStack i = Utils.findCustomItemInInv(ItemList.mjolnir, player.getInventory());
                if (i == null) return;
                ItemMeta meta = i.getItemMeta();
                assert meta != null;
                PersistentDataContainer container = meta.getPersistentDataContainer();

                if (!container.has(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER)) return;
                int charges = container.get(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER);
                if (charges < 2) return;

                player.getWorld().strikeLightning(entity.getLocation());

                setChargesMjolnir(i, charges - 2);
            }
        }, 25);
    }

    //Short Bow
    @EventHandler
    private void onClickShortBow(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.shortBow, player, e)) return;

        e.setCancelled(true);

        ItemStack[] contents = player.getInventory().getStorageContents();

        player.launchProjectile(Arrow.class);

        for (ItemStack i : contents)
            if (player.getGameMode() != GameMode.CREATIVE && i != null && i.getType() == Material.ARROW) {
                i.setAmount(i.getAmount() - 1);
                break;
            }
    }

    //Explosive Bow
    @EventHandler
    private void onProjectileHitExplosiveBow(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.explosiveBow, player, e)) return;

        player.getWorld().createExplosion(arrow.getLocation(), 3, false);
    }

    //Soul Bow
    private String soulBowUUID;
    private ItemStack soulBowItem;

    @EventHandler
    private void onShootSoulBow(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        soulBowItem = player.getInventory().getItemInMainHand();
    }

    @EventHandler
    private void onHitSoulBow(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        LivingEntity hit = (LivingEntity) e.getHitEntity();
        if (hit == null) return;
        if (Utils.validateItem(soulBowItem, ItemList.soulBow, player, e)) return;
        soulBowItem = null;

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

    //Grappling Hook
    @EventHandler
    private void onFishGrapplingHook(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.grapplingHook, player, e)) return;

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.3).setY(1));
    }

    //Hook Shot
    @EventHandler
    private void onFishHookShot(PlayerFishEvent e) {
        if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.hookShot, player, e)) return;

        Location playerLoc = player.getLocation();
        Location hookLoc = e.getHook().getLocation();
        Location change = hookLoc.subtract(playerLoc);
        player.setVelocity(change.toVector().multiply(0.75).setY(1.25));
    }

    //Fire Talisman
    @EventHandler
    private void onFireDamageFireTalisman(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.LAVA) return;

        int first = player.getInventory().first(ItemList.fireTalisman.getItemStack().getType());
        ItemStack is = first == -1 ? player.getInventory().getItemInOffHand() : player.getInventory().getItem(first);

        if (is == null) return;
        if (Utils.validateItem(is, ItemList.fireTalisman, player, e)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

        is.setAmount(is.getAmount() - 1);
    }

    @EventHandler
    private void onEatFireTalisman(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (Utils.validateItem(is, ItemList.fireTalisman, player, e)) return;

        e.setCancelled(true);
    }

    @EventHandler
    private void onKillRabbitFoot(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Rabbit rabbit)) return;
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();
        double dropChance = 0.0001;

        ChanceManager.chanceCalculation(dropChance, () -> rabbit.getWorld().dropItemNaturally(rabbit.getLocation(), ItemList.rabbitFoot.getItemStack()), player);
    }

    //Slime Boots
    @EventHandler
    private void onFallSlimeBoots(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.slimeBoots, player, e)) return;

        if (player.getVelocity().getY() > -0.515) return;

        for (int i = 0; i < 7; i++) {
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
        if (Utils.validateItem(is, ItemList.springBoots, player, e)) return;

        if (player.isSneaking())
            player.setVelocity(player.getVelocity().multiply(0.5).setY(1));
    }

    //Farmer Boots
    @EventHandler
    private void onTrampleFarmerBoots(CropTrampleEvent e) {
        if (!(e.getTrampler() instanceof Player player)) return;
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.farmerBoots, player, e)) return;

        e.setCancelled(true);
    }

    //Shelmet
    @EventHandler
    private void onPlayerHitShelmet(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        ItemStack is = player.getInventory().getHelmet();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.shelmet, player, e)) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setVelocity(new Vector()), 1);
    }

    final Item[] speedTargetArmor = { ItemList.speedHelmet, ItemList.speedChestplate, ItemList.speedLeggings, ItemList.speedBoots };
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

        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (e.getDamage() > 0.25 * maxHealth) return;

        e.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.sendMessage("§cYou have been protected!");
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
        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.cheatCode, player, e)) return;

        if (player.getGameMode() == GameMode.CREATIVE)
            player.setGameMode(GameMode.SURVIVAL);
        else
            player.setGameMode(GameMode.CREATIVE);
    }
}
