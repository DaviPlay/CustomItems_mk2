package davide.customitems.events;

import davide.customitems.api.*;
import davide.customitems.CustomItems;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.persistence.PersistentDataType;
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
    private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    public EventListener(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //Rune Shard
    @EventHandler
    private void onBlockBreakReforgeShard(BlockBreakEvent e) {
        if (!SpecialBlocks.isOre(e.getBlock().getType()) && !SpecialBlocks.isStone(e.getBlock().getType())) return;
        Block block = e.getBlock();
        float dropChance;

        switch (e.getBlock().getType()) {
            case COAL_ORE, DEEPSLATE_COAL_ORE, COPPER_ORE, DEEPSLATE_COPPER_ORE -> dropChance = 0.1f;
            case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE, LAPIS_ORE, DEEPSLATE_LAPIS_ORE, NETHER_QUARTZ_ORE -> dropChance = 0.5f;
            case IRON_ORE, DEEPSLATE_IRON_ORE -> dropChance = 1;
            case GOLD_ORE, DEEPSLATE_GOLD_ORE -> dropChance = 2;
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> dropChance = 5;
            case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> dropChance = 10;
            default -> dropChance = 0.005f;
        }

        if (new Random().nextInt(100) <= dropChance) {
            World world = block.getWorld();
            Location loc = block.getLocation();

            world.dropItemNaturally(loc, ItemList.runeShard.getItemStack());
        }
    }

    //Reforge Stone
    @EventHandler
    private void onAnvilPrepareReforgeStone(PrepareAnvilEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL) return;
        ItemStack stone = e.getInventory().getItem(1);
        if (stone == null) return;
        if (Utils.validateItem(stone, ItemList.reforgeStone, (Player) e.getInventory().getViewers().get(0))) return;

        ItemStack is = e.getInventory().getItem(0);
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        Reforge reforge;
        ItemStack result = is.clone();

        if (item.getSubType() == null)
            reforge = Reforge.randomReforge(item.getType());
        else
            reforge = Reforge.randomReforge(item.getSubType().getType());

        if (reforge == null) return;
        Reforge.setReforge(reforge, result);
        e.setResult(result);
        Bukkit.getScheduler().runTaskLater(plugin, () -> e.getInventory().setRepairCost(3), 1);

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

        new ItemsGUI(player);
    }

    //Stonk
    private static final int BLOCKS_TO_MINE_TOTAL_STONK = 250;

    @EventHandler
    private void onBlockBreakStonk(BlockBreakEvent e) {
        if (e.getBlock().isPassable()) return;
        if (SpecialBlocks.isClickableBlock(e.getBlock())) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<String> lore = meta.getLore();
        if (lore == null) return;
        if (Utils.validateItem(is, ItemList.stonk, player)) return;

        if (!container.has(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER))
            container.set(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER, 0);

        int blocksMined = container.get(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER);

        container.set(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER, blocksMined + 1);

        if (blocksMined == BLOCKS_TO_MINE_TOTAL_STONK - 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 4));
            container.set(new NamespacedKey(plugin, "blocks_mined"), PersistentDataType.INTEGER, 0);
        }

        is.setItemMeta(meta);

        int index = -1;
        for (String line : lore)
            if (line.contains("§e"))
                index = lore.indexOf(line);

        lore.set(index, "§e" + getBlocksRemainingStonk(is) + " §8blocks remaining");
        Item.setLore(is, lore);
    }

    public static int getBlocksRemainingStonk(ItemStack is) {
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
        if (!(e.getBlock().getBlockData() instanceof Ageable age)) return;

        Player player = e.getPlayer();
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.replenisher, player)) return;

        Block crop = e.getBlock();

        String cropName = crop.getType().name();
        System.out.println("PLANTED: " + Material.matchMaterial(cropName));
        switch (cropName) {
            case "POTATOES" -> cropName = "POTATO";
            case "CARROTS" -> cropName = "CARROT";
            case "BEETROOTS" -> cropName = "BEETROOT";
            case "SWEET_BERRY_BUSH" -> cropName = "SWEET_BERRIES";
        }

        for (ItemStack i : player.getInventory().getStorageContents()) {
            try {
                System.out.println(i.getType());
                if (i.getType() == Material.matchMaterial(cropName + "_SEEDS") || i.getType() == Material.matchMaterial(cropName) || i.getType() == Material.matchMaterial(cropName.replace("STEM", "SEEDS"))) {
                    i.setAmount(i.getAmount() - 1);

                    crop.getDrops(is, player).forEach(drop -> {
                        if (drop.getType() != Material.AIR)
                            player.getWorld().dropItemNaturally(crop.getLocation(), drop);
                    });

                    age.setAge(0);
                    crop.setBlockData(age);

                    e.setCancelled(true);
                    return;
                }
            } catch (NullPointerException ignored) {}
        }

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
        if (Utils.validateItem(is, ItemList.meth, player)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.meth.getKey()))
            cocaineUses++;
        else {
            Cooldowns.setCooldown(player.getUniqueId(), ItemList.meth.getKey(), ItemList.meth.getAbilities().get(0).cooldown());
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

        RayTraceResult ray = player.rayTraceBlocks(192, FluidCollisionMode.SOURCE_ONLY);
        if (ray == null) return;
        World world = player.getWorld();
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
        Cooldowns.setCooldown(is.getItemMeta().getPersistentDataContainer().get(ItemList.throwingAxe.getKey(), new UUIDDataType()), ItemList.throwingAxe.getKey(), ItemList.throwingAxe.getAbilities().get(0).cooldown());
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
        Cooldowns.setCooldown(item.getRandomUUID(is), item.getKey(), item.getAbilities().get(0).cooldown());
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

        if (reforge != null && reforge.getDamageModifier() > 0) {
            Item.setStats((int)((Item.getDamage(is) - (float)reforge.getDamageModifier() / 2) * 2), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is);
            Reforge.setReforge(new Reforge("Ultra " + reforge.getName(), Type.MELEE, reforge.getDamageModifier() * 2, reforge.getCritChanceModifier(), reforge.getCritDamageModifier(), reforge.getHealthModifier(), reforge.getDefenceModifier()), is);
        }
        else
            Item.setStats(Item.getDamage(is) * 2, Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is);

        Item item = Item.toItem(is);
        assert item != null;
        Cooldowns.setCooldown(container.get(item.getKey(), new UUIDDataType()), ItemList.caladbolg.getKey(), ItemList.caladbolg.getAbilities().get(0).cooldown());

        final short duration = 10;
        float delay = (ItemList.caladbolg.getAbilities().get(0).cooldown() - (ItemList.caladbolg.getAbilities().get(0).cooldown() - duration)) * 20;
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

                            if (r != null && r.getDamageModifier() > 0) {
                                Item.setStats((Item.getDamage(i) + r.getDamageModifier()) / 2, Item.getCritChance(i), Item.getCritDamage(i), Item.getHealth(i), Item.getDefence(i), i);
                                Reforge.setReforge(Reforge.getReforge(reforge.getName()), i);
                            } else
                                Item.setStats(Item.getDamage(i) / 2, Item.getCritChance(i), Item.getCritDamage(i), Item.getHealth(i), Item.getDefence(i), i);

                            i.setType(Material.DIAMOND_SWORD);
                            break;
                        }
                    }
                }
        }, (long) delay);
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

    //Explosive Bow
    @EventHandler
    private void onProjectileHitExplosiveBow(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (Utils.validateItem(is, ItemList.explosiveBow, player)) return;

        player.getWorld().createExplosion(arrow.getLocation(), 3, false);
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

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.grapplingHook.getKey(), ItemList.grapplingHook.getAbilities().get(0).cooldown());
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

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.hookShot.getKey(), ItemList.hookShot.getAbilities().get(0).cooldown());
    }

    //Slime Boots
    @EventHandler
    private void onFallSlimeBoots(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getBoots();
        if (is == null) return;
        if (Utils.validateItem(is, ItemList.slimeBoots, player)) return;

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

        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        if (e.getDamage() > 0.25 * maxHealth) return;

        e.setCancelled(true);
        player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
        player.sendMessage("§cYou have been protected!");

        Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getAbilities().get(0).cooldown());
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
        Cooldowns.setCooldown(player.getUniqueId(), ItemList.fireTalisman.getKey(), ItemList.fireTalisman.getAbilities().get(0).cooldown());
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
