package davide.customitems.lists;

import davide.customitems.CustomItems;
import davide.customitems.api.*;
import davide.customitems.crafting.CraftingType;
import davide.customitems.events.Events;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.itemCreation.*;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.ItemBuilder;
import davide.customitems.itemCreation.MaterialBuilder;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.playerStats.ChanceManager;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.util.*;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class ItemList {
    private final static CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

    //Items 2d List (0 = items, 1 = materials)
    public static List<List<Item>> items = new ArrayList<>();
    public static List<Item> utilsItems = new ArrayList<>();

    //Materials
    public static final Item enchantedSeed = new MaterialBuilder(new ItemStack(Material.WHEAT_SEEDS), "Enchanted Seed")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedCobble = new MaterialBuilder(new ItemStack(Material.COBBLESTONE), "Enchanted Cobblestone")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item magmaRod = new MaterialBuilder(new ItemStack(Material.BLAZE_ROD), "Magma Rod")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.FURNACE)
            .crafting(Collections.singletonList(new ItemStack(Material.BLAZE_ROD, 4)))
            .exp(20)
            .cookingTime(15 * 20)
            .build();

    public static final Item meltedSugar = new MaterialBuilder(new ItemStack(Material.WHITE_DYE, 4), "Melted Sugar")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.FURNACE)
            .crafting(Collections.singletonList(new ItemStack(Material.SUGAR)))
            .exp(10)
            .cookingTime(5 * 20)
            .build();
    public static final Item enchantedBone = new MaterialBuilder(new ItemStack(Material.BONE), "Enchanted Bone")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedString = new MaterialBuilder(new ItemStack(Material.STRING), "Enchanted String")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedSilk = new MaterialBuilder(new ItemStack(Material.COBWEB), "Enchanted Silk")
            .compact(enchantedString.getItemStack())
            .rarity(Rarity.RARE)
            .build();

    public static final Item enchantedSpiderEye = new MaterialBuilder(new ItemStack(Material.SPIDER_EYE), "Enchanted Spider Eye")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedCopper = new MaterialBuilder(new ItemStack(Material.COPPER_INGOT), "Enchanted Copper")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedIron = new MaterialBuilder(new ItemStack(Material.IRON_INGOT), "Enchanted Iron")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedIronBlock = new MaterialBuilder(new ItemStack(Material.IRON_BLOCK), "Enchanted Iron Block")
            .compact(enchantedIron.getItemStack())
            .rarity(Rarity.RARE)
            .build();

    public static final Item enchantedGold = new MaterialBuilder(new ItemStack(Material.GOLD_INGOT), "Enchanted Gold")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedGoldBlock = new MaterialBuilder(new ItemStack(Material.GOLD_BLOCK), "Enchanted Gold Block")
            .compact(enchantedGold.getItemStack())
            .rarity(Rarity.RARE)
            .build();

    public static final Item enchantedDiamond = new MaterialBuilder(new ItemStack(Material.DIAMOND), "Enchanted Diamond")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item enchantedDiamondBlock = new MaterialBuilder(new ItemStack(Material.DIAMOND_BLOCK), "Enchanted Diamond Block")
            .compact(enchantedDiamond.getItemStack())
            .rarity(Rarity.RARE)
            .build();

    public static final Item enchantedNetherite = new MaterialBuilder(new ItemStack(Material.NETHERITE_INGOT), "Enchanted Netherite")
            .rarity(Rarity.UNCOMMON)
            .build();

    public static final Item runeShard = new MaterialBuilder(new ItemStack(Material.ECHO_SHARD), "Rune Shard")
            .rarity(Rarity.RARE)
            .craftingType(CraftingType.DROP)
            .blockDrops(new HashMap<>(){{
                put(0.005, SpecialBlocks.getStones());
                put(0.1, Arrays.asList(Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE));
                put(0.5, Arrays.asList(Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_QUARTZ_ORE));
                put(1D, Arrays.asList(Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE));
                put(2D, Arrays.asList(Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE));
                put(5D, Arrays.asList(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE));
                put(10D, Arrays.asList(Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE));
                put(50D, Collections.singletonList(Material.ANCIENT_DEBRIS));
            }})
            .addInfo("Drop Chance:",
                    "§8- §7Stone Variants §8= §e0.005% (1/20.000)",
                    "§8- Coal & §cCopper §8= §e0.1% (1/1.000)",
                    "§8- §cRedstone§8, §9Lapis §8& §fQuartz §8= §e0.5% (1/200)",
                    "§8- §fIron §8= §e1% (1/100)",
                    "§8- §6Gold §8= §e2% (1/50)",
                    "§8- §bDiamond §8= §e5% (1/20)",
                    "§8- §aEmerald §8= §e10% (1/10)",
                    "§8- §4Ancient Debris §8= §e50% (1/2)")
            .lore("Rarely found embedded in precious ores,", "down deep in the dark and scary coves...")
            .build();

    //Cool items ;>
    public static final Item recipeBook = new ItemBuilder(new ItemStack(Material.BOOK), "Knowledge Book")
            .type(Type.ITEM)
            .rarity(Rarity.COMMON)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    new ItemsGUI(player);
                }
            }, AbilityType.RIGHT_CLICK, "Omniscience", 0, false, "Shows all the custom recipes added", "by the CustomItems plugins"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    new ItemStack(Material.STRING),
                    null,
                    new ItemStack(Material.STRING),
                    new ItemStack(Material.BOOK),
                    new ItemStack(Material.STRING),
                    null,
                    new ItemStack(Material.STRING),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item backpack = new ItemBuilder(new ItemStack(Material.CAULDRON), "Backpack")
            .type(Type.TOOL)
            .rarity(Rarity.UNCOMMON)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof PlayerInteractEvent e)) return;

                            Player player = e.getPlayer();
                            ItemStack is = player.getInventory().getItemInMainHand();
                            if (Utils.validateItem(is, player, 0, e)) return;
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();
                            Inventory inv = Bukkit.createInventory(e.getPlayer(), 27, "Backpack");

                            String contents = (String) plugin.getBase64Config().get(container.get(Item.toItem(is).getKey(), new UUIDDataType()).toString());
                            if (contents == null) {
                                player.openInventory(inv);
                                return;
                            }

                            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(contents));
                            try {
                                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                                dataInput.close();
                                try {
                                    ItemStack[] items = (ItemStack[]) dataInput.readObject();
                                    for (ItemStack i : items) {
                                        if (i == null) continue;
                                        ItemMeta iMeta = i.getItemMeta();
                                        if (iMeta == null) continue;
                                        Item item = Item.toItem(i);
                                        if (item == null) continue;

                                        if (iMeta.getDisplayName().contains("glint")) {
                                            item.setGlint(true, i);
                                            iMeta.setDisplayName(iMeta.getDisplayName().replace("glint", ""));
                                            i.setItemMeta(iMeta);
                                        }
                                    }
                                    inv.setContents(items);
                                } catch (EOFException ignored) {}
                            } catch (IOException | ClassNotFoundException exception) {
                                exception.printStackTrace();
                            }

                            player.openInventory(inv);
                }
            }, AbilityType.RIGHT_CLICK, "Storage", 0, false, "Opens the backpack to", "reveal it's contents"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof InventoryClickEvent e)) return;

                            Inventory inv = e.getView().getTopInventory();
                            if (inv == null) return;
                            if (!(inv.getHolder() instanceof Player player) || !e.getView().getTitle().equals("Backpack"))
                                return;
                            if (e.getCurrentItem() == null && e.getCursor() == null) return;
                            ItemStack is = player.getInventory().getItemInMainHand();
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();
                            ItemStack[] contents = inv.getContents();
                            if (contents == null) return;

                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            try {
                                for (ItemStack i : contents) {
                                    if (i == null) continue;
                                    ItemStack itemStack = i.clone();
                                    Item item = Item.toItem(itemStack);
                                    if (item == null) continue;
                                    if (!item.isGlint()) continue;

                                    item.setGlint(false, itemStack);
                                    ItemMeta iMeta = itemStack.getItemMeta();
                                    if (iMeta == null) continue;
                                    iMeta.setDisplayName(iMeta.getDisplayName() + "glint");
                                    itemStack.setItemMeta(iMeta);
                                }

                                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
                                dataOutput.writeObject(contents);
                                dataOutput.close();
                                plugin.getBase64Config().set(container.get(Item.toItem(is).getKey(), new UUIDDataType()).toString(), Base64Coder.encodeLines(outputStream.toByteArray()));
                                plugin.getBase64Config().save(plugin.getBase64File());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "encoding contents"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof InventoryCloseEvent e)) return;

                            Inventory inv = e.getInventory();
                            if (!(inv.getHolder() instanceof Player player) || !e.getView().getTitle().equals("Backpack")) return;
                            ItemStack is = player.getInventory().getItemInMainHand();
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();
                            ItemStack[] contents = inv.getContents();
                            if (contents == null) return;

                            for (ItemStack i : contents) {
                                if (i == null) continue;
                                Item item = Item.toItem(i);
                                if (item == null) continue;
                                if (!item.isGlint()) continue;

                                item.setGlint(false, i);
                                ItemMeta iMeta = i.getItemMeta();
                                if (iMeta == null) continue;
                                iMeta.setDisplayName(iMeta.getDisplayName() + "glint");
                                i.setItemMeta(iMeta);
                            }

                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            try {
                                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
                                dataOutput.writeObject(contents);
                                dataOutput.close();
                                plugin.getBase64Config().set(container.get(Item.toItem(is).getKey(), new UUIDDataType()).toString(), Base64Coder.encodeLines(outputStream.toByteArray()));
                                plugin.getBase64Config().save(plugin.getBase64File());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                }
            }, AbilityType.PASSIVE, "_", 0, false, "encoding contents"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ItemDespawnEvent e)) return;

                            ItemStack is = e.getEntity().getItemStack();
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();

                            plugin.getBase64Config().set(container.get(Item.toItem(is).getKey(), new UUIDDataType()).toString(), "");
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "delete content from file"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageEvent e)) return;

                            ItemStack is = ((org.bukkit.entity.Item) e.getEntity()).getItemStack();
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();

                            try {
                                plugin.getBase64Config().set(container.get(Item.toItem(is).getKey(), new UUIDDataType()).toString(), null);
                                plugin.getBase64Config().save(plugin.getBase64File());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "delete content from file"))
            .hasRandomUUID(true)
            .build();

    private final static int BLOCKS_TO_MINE_TOTAL_STONK = 250;
    private static int blocksRemaining = BLOCKS_TO_MINE_TOTAL_STONK;
    public static final Item stonk = new ItemBuilder(new ItemStack(Material.GOLDEN_PICKAXE), "Stonk")
            .subType(SubType.PICKAXE)
            .rarity(Rarity.EPIC)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

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
                    if (Utils.validateItem(is, player, 0, e)) return;
                    NamespacedKey key = new NamespacedKey(plugin, "blocks_mined");

                    if (!container.has(key, PersistentDataType.INTEGER))
                        container.set(key, PersistentDataType.INTEGER, 0);

                    blocksRemaining = BLOCKS_TO_MINE_TOTAL_STONK - container.get(key, PersistentDataType.INTEGER);
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

                    lore.set(index, "§e" + blocksRemaining + " §8blocks remaining");
                    Item.setLore(is, lore);
                }
            }, AbilityType.PASSIVE, "Mine! Mine! Mine!", 0, false, "Every " + blocksRemaining + " blocks mined gain", "haste 5 for 20 seconds"))
            .lore("§e§o" + BLOCKS_TO_MINE_TOTAL_STONK + " §o§8blocks remaining")
            .enchantments(new HashMap<>(){{
                put(Enchantment.DIG_SPEED, 6);
                put(Enchantment.DURABILITY, 10);
            }})
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedGold.getItemStack(16),
                    enchantedGold.getItemStack(16),
                    enchantedGold.getItemStack(16),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item veinPick = new ItemBuilder(new ItemStack(Material.DIAMOND_PICKAXE), "Vein Pick")
            .subType(SubType.PICKAXE)
            .rarity(Rarity.LEGENDARY)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

                    if (!SpecialBlocks.isOre(e.getBlock().getType())) return;
                    Player player = e.getPlayer();
                    ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    Block block = e.getBlock();
                    List<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 1, 1));
                    recCeil = 0;
                    recursiveBreakBlock(block.getType(), blocks, is);
                }
            }, AbilityType.PASSIVE, "VeinMiner v2.0", 0, false, "Automatically mines all the adjacent ores"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedIronBlock.getItemStack(),
                    enchantedIronBlock.getItemStack(),
                    enchantedIronBlock.getItemStack(),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item severedDrill = new ItemBuilder(new ItemStack(Material.STONE_PICKAXE), "Severed Drill")
            .subType(SubType.PICKAXE)
            .rarity(Rarity.UNCOMMON)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

                    if (!SpecialBlocks.isStone(e.getBlock().getType()) && !SpecialBlocks.isOre(e.getBlock().getType())) return;
                    Player player = e.getPlayer();
                    ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

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
            }, AbilityType.PASSIVE, "Hazard Miner", 0, false, "Chance to break up to 2", "additional blocks around", "the mined one"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedCobble.getItemStack(),
                    enchantedCobble.getItemStack(),
                    enchantedCobble.getItemStack(),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item mendedDrill = new ItemBuilder(new ItemStack(Material.IRON_PICKAXE), "Mended Drill")
            .subType(SubType.PICKAXE)
            .rarity(Rarity.RARE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

                    if (!SpecialBlocks.isStone(e.getBlock().getType()) && !SpecialBlocks.isOre(e.getBlock().getType())) return;
                    Player player = e.getPlayer();
                    ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    List<Block> blocks = Utils.getBlocksInRadius(e.getBlock(), new Vector3(1, 1, 1));
                    byte maxBlocks = 4, chanceToBreak = 25;
                    for (Block b : blocks) {
                        if (new Random().nextInt(100) < chanceToBreak) {
                            b.breakNaturally(is);
                            maxBlocks--;
                        }
                        if (maxBlocks == 0) return;
                    }
                }
            }, AbilityType.PASSIVE, "Fortune Driller", 0, false, "High chance to break up", "to 4 additional blocks", "around the mined one"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedIron.getItemStack(16),
                    enchantedIron.getItemStack(16),
                    enchantedIron.getItemStack(16),
                    null,
                    severedDrill.getItemStack(),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item treecapitator = new ItemBuilder(new ItemStack(Material.DIAMOND_AXE), "Treecapitator")
            .subType(SubType.AXE)
            .rarity(Rarity.LEGENDARY)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

                    if (!SpecialBlocks.isLog(e.getBlock().getType())) return;
                    Player player = e.getPlayer();
                    ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    Block block = e.getBlock();
                    List<Block> blocks = Utils.getBlocksInRadius(block, new Vector3(1, 1, 1));
                    recCeil = 0;
                    recursiveBreakBlock(block.getType(), blocks, is);
                }
            }, AbilityType.PASSIVE, "Environmental Threat", 0, false, "Automatically chops all the adjacent logs"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedIronBlock.getItemStack(),
                    enchantedIronBlock.getItemStack(),
                    null,
                    new ItemStack(Material.STICK),
                    enchantedIronBlock.getItemStack(),
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item replenisher = new ItemBuilder(new ItemStack(Material.IRON_HOE), "Replenisher")
            .subType(SubType.HOE)
            .rarity(Rarity.RARE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof BlockBreakEvent e)) return;

                    if (!(e.getBlock().getBlockData() instanceof Ageable age)) return;
                    Player player = e.getPlayer();
                    ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

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

                                new DelayedTask(() -> {
                                    age.setAge(0);
                                    crop.setBlockData(age);

                                    e.setCancelled(true);
                                }, 5);
                                return;
                            }
                        } catch (NullPointerException ignored) {}
                    }
                }
            }, AbilityType.PASSIVE, "Replenish", 0, false, "Automatically replants the harvested crops", "using seeds from your inventory"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedSeed.getItemStack(4),
                    enchantedSeed.getItemStack(4),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item ultimateBread = new ItemBuilder(new ItemStack(Material.BREAD), "Ultimate Bread")
            .type(Type.FOOD)
            .rarity(Rarity.LEGENDARY)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    final int duration = 120 * 20; // effect duration in seconds * ticks
                    int newDuration = player.hasPotionEffect(PotionEffectType.SATURATION) ? player.getPotionEffect(PotionEffectType.SATURATION).getDuration() + duration : duration;

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, newDuration, 0));

                    if (player.getGameMode() != GameMode.CREATIVE)
                        is.setAmount(is.getAmount() - 1);
                }
            }, AbilityType.RIGHT_CLICK, "Saturated", 0, false, "This special food gives you", "saturation for 2 minutes!", "§8§oStackable!"))
            .craftingType(CraftingType.SHAPELESS)
            .crafting(Arrays.asList(
                    new ItemStack(Material.SUSPICIOUS_STEW),
                    new ItemStack(Material.GOLDEN_CARROT),
                    new ItemStack(Material.BREAD),
                    new ItemStack(Material.SWEET_BERRIES),
                    new ItemStack(Material.CHORUS_FRUIT),
                    new ItemStack(Material.CAKE),
                    new ItemStack(Material.COOKIE),
                    new ItemStack(Material.COOKED_COD),
                    new ItemStack(Material.HONEY_BOTTLE)
            ))
            .build();

    private static int cocaineUses = 1;
    private static final HashMap<UUID, Integer> timesUsedInCooldown = new HashMap<>();
    public static final Item meth = new ItemBuilder(new ItemStack(Material.SUGAR), "Meth")
            .type(Type.FOOD)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    final int usesMax = 5;
                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

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

                    if (player.getGameMode() != GameMode.CREATIVE)
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                }
            }, AbilityType.RIGHT_CLICK, "Heisenberg", 30, false, "Say my name..."))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    meltedSugar.getItemStack(8),
                    meltedSugar.getItemStack(8),
                    meltedSugar.getItemStack(8),
                    meltedSugar.getItemStack(8),
                    meltedSugar.getItemStack(8),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item aspectOfTheEnd = new ItemBuilder(new ItemStack(Material.STICK), "Aspect Of The End")
            .subType(SubType.WAND)
            .rarity(Rarity.RARE)
            .damage(-1)
            .abilities(new Ability(EventList.TELEPORT, AbilityType.RIGHT_CLICK, "Insta-Teleport", 0, false, "Teleports you §e8 blocks", "from your current position", "in the direction you're facing"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.ENDER_EYE, 64),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item explosiveStaff = new ItemBuilder(new ItemStack(Material.STICK), "Explosive Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.UNCOMMON)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    player.getWorld().createExplosion(player.getLocation(), 3);
                    if (player.getGameMode() != GameMode.CREATIVE)
                        is.setAmount(is.getAmount() - 1);
                }
            }, AbilityType.RIGHT_CLICK, "Boom!", 0, false, "Creates an explosion that", "destroys the fragile staff"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageEvent e)) return;

                            if (!(e.getEntity() instanceof Player player)) return;
                            if (e.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) return;
                            ItemStack is = player.getInventory().getItemInMainHand();
                            if (Utils.validateItem(is, player, 0, e)) return;

                            e.setDamage(0);
                        }
                    }, AbilityType.PASSIVE, "Explosion Immunity", 0, false, "You're immune to the explosion", "damage of this staff", "§8§oDestroyed on use"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.TNT),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item lightningStaff = new ItemBuilder(new ItemStack(Material.END_ROD), "Lightning Wand")
            .subType(SubType.STAFF)
            .rarity(Rarity.UNCOMMON)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    RayTraceResult ray = player.rayTraceBlocks(192, FluidCollisionMode.SOURCE_ONLY);
                    if (ray == null) return;
                    World world = player.getWorld();
                    world.strikeLightning(ray.getHitPosition().toLocation(world));
                }
            }, AbilityType.CLICK, "Mufulgur", 0, false, "Cast a lighting on the block", "you're looking at"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.LIGHTNING_ROD),
                    null,
                    enchantedCopper.getItemStack(),
                    null,
                    enchantedCopper.getItemStack(),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item fireStaff = new ItemBuilder(new ItemStack(Material.BLAZE_ROD), "Fire Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.RARE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

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
            }, AbilityType.CLICK, "Fire Ball", 0, false, "Shoot an explosive fireball"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.MAGMA_CREAM, 32),
                    null,
                    magmaRod.getItemStack(4),
                    null,
                    magmaRod.getItemStack(4),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item midasStaff = new ItemBuilder(new ItemStack(Material.TOTEM_OF_UNDYING), "Midas' Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.MYTHIC)
            .abilities(new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageByEntityEvent e)) return;

                            if (!(e.getDamager() instanceof Player player)) return;
                            ItemStack is = player.getInventory().getItemInMainHand();
                            if (Utils.validateItem(is, player, 0, e)) return;

                            LivingEntity hit = (LivingEntity) e.getEntity();
                            hit.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, hit.getLocation(), 20, 0, 0, 0, 0.25);
                            Block b = hit.getLocation().getBlock();
                            b.setType(Material.GOLD_BLOCK);
                            hit.remove();
                        }
                    }, AbilityType.LEFT_CLICK, "Midas' Touch", 0, false, "Upon hitting a mob it", "turns into gold"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PlayerInteractEvent e)) return;

                            Player player = e.getPlayer();
                            ItemStack is = e.getItem();
                            if (is == null) return;
                            if (Utils.validateItem(is, player, 1, e)) return;

                            assert is.getItemMeta() != null;
                            if (!player.isSneaking()) return;
                            Objects.requireNonNull(Item.toItem(is)).setGlint(!is.getItemMeta().hasEnchants(), is);
                        }
                    }, AbilityType.SHIFT_RIGHT_CLICK, "Switch", 0, false, "Change the staff mode"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PlayerMoveEvent e)) return;

                            Player player = e.getPlayer();
                            ItemStack is = Utils.findCustomItemInInv(midasStaff, player.getInventory());
                            if (is == null) return;
                            if (Utils.validateItem(is, player, 2, e)) return;

                            if (!Objects.requireNonNull(Item.toItem(is)).isGlint()) return;
                            Block b = player.getLocation().subtract(0, 1, 0).getBlock();
                            Material type = b.getState().getType();

                            if (!b.isPassable())
                                if (b.getType() != Material.GOLD_BLOCK) {
                                    b.setType(Material.GOLD_BLOCK);
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(type), 2 * 20);
                                }
                        }
                    }, AbilityType.PASSIVE, "Blessed Feet", 0, false, "Leave a trail of gold behind you", "if the staff is glowing"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PrepareAnvilEvent e)) return;

                            ItemStack is = e.getInventory().getItem(0);
                            if (is == null) return;
                            if (Utils.validateItem(is, (Player) e.getInventory().getViewers().getFirst(), 3, e)) return;

                            if (Item.toItem(is).equals(midasStaff) && e.getInventory().getItem(1).getItemMeta() instanceof EnchantmentStorageMeta)
                                e.setResult(null);
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "prevent enchanting"))
            .lore("My gold, my kingdom,", "everything for the", "golden touch!")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedGoldBlock.getItemStack(),
                    null,
                    enchantedGoldBlock.getItemStack(),
                    enchantedGoldBlock.getItemStack(),
                    enchantedGoldBlock.getItemStack(),
                    null,
                    enchantedGoldBlock.getItemStack(),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item judger = new ItemBuilder(new ItemStack(Material.WOODEN_SHOVEL), "The Executioner")
            .subType(SubType.MACE)
            .rarity(Rarity.UNCOMMON)
            .damage(3)
            .critChance(5)
            .critDamage(2)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageByEntityEvent e)) return;

                    if (!(e.getDamager() instanceof Player player)) return;
                    if (!(e.getEntity() instanceof LivingEntity hit)) return;
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    if (hit.getHealth() - e.getDamage() < 0.15 * Objects.requireNonNull(hit.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue())
                        hit.setHealth(0);
                }
            }, AbilityType.HIT, "Execute", 0, false, "Finish every enemy under", "15% of it's max health"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedCobble.getItemStack(),
                    null,
                    enchantedCobble.getItemStack(),
                    new ItemStack(Material.STICK),
                    enchantedCobble.getItemStack(),
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .build();

    public static final Item venomousDagger = new ItemBuilder(new ItemStack(Material.STONE_SWORD), "Venomous Dagger")
            .subType(SubType.DAGGER)
            .rarity(Rarity.UNCOMMON)
            .damage(3)
            .critDamage(2)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageByEntityEvent e)) return;

                    if (!(e.getDamager() instanceof Player player)) return;
                    if (!(e.getEntity() instanceof LivingEntity entity)) return;
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 1));
                }
            }, AbilityType.RIGHT_CLICK, "Poison Touch", 3, true, "Poison the enemy on contact"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    null,
                    enchantedSpiderEye.getItemStack(5),
                    null,
                    null,
                    null,
                    enchantedSpiderEye.getItemStack(5)
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item vampiresFang = new ItemBuilder(new ItemStack(Material.GHAST_TEAR), "Vampire's Fang")
            .subType(SubType.DAGGER)
            .rarity(Rarity.RARE)
            .damage(7)
            .attackSpeed(-0.6f)
            .critChance(10)
            .critDamage(1.5f)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageByEntityEvent e)) return;

                    if (!(e.getDamager() instanceof Player player)) return;
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    double amountToHeal = (e.getDamage() * 25) / 100;
                    player.setHealth(Math.min(player.getHealth() + amountToHeal, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
                }
            }, AbilityType.HIT, "Healing Touch", 0, false, "Heals for 25% of the", "dealt damage"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.STICK),
                    null,
                    null,
                    null,
                    enchantedIron.getItemStack(2),
                    null,
                    null,
                    null,
                    enchantedIron.getItemStack(2)
                    ))
            .build();

    public static final Item throwingAxe = new ItemBuilder(new ItemStack(Material.STONE_AXE), "Throwing Axe")
            .subType(SubType.GREATAXE)
            .rarity(Rarity.RARE)
            .damage(5)
            .critChance(15)
            .critDamage(3)
            .abilities(new Ability(EventList.THROW, AbilityType.RIGHT_CLICK, "Throw", 1, false, "Launch the axe"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.COBBLESTONE, 64),
                    new ItemStack(Material.COBBLESTONE, 64),
                    new ItemStack(Material.COBBLESTONE, 64),
                    new ItemStack(Material.COBBLESTONE, 64),
                    new ItemStack(Material.STICK),
                    new ItemStack(Material.COBBLESTONE, 64),
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    private static List<Enemy> enemies = new ArrayList<>();
    private static int defaultCritChance;
    private static DelayedTask shadowFuryTask;
    private static short hits = 0;
    public static final Item shadowFury = new ItemBuilder(new ItemStack(Material.IRON_SWORD), "Shadow Fury")
            .subType(SubType.DAGGER)
            .rarity(Rarity.EPIC)
            .damage(7)
            .critChance(5)
            .critDamage(3)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                        if (!(element instanceof PlayerInteractEvent e)) return;

                        Player player = e.getPlayer();
                        ItemStack is = player.getInventory().getItemInMainHand();
                        ItemMeta meta = is.getItemMeta();
                        if (meta == null) return;
                        if (Utils.validateItem(is, player, 0, e)) return;
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
                            Location enemyLoc = enemy.getLocation();
                            double newX;
                            double newZ;
                            float nang = enemyLoc.getYaw() + 90;
                            if (nang < 0) nang += 360;

                            newX = Math.cos(Math.toRadians(nang));
                            newZ = Math.sin(Math.toRadians(nang));
                            Location newPlayerLoc = new Location(enemyLoc.getWorld(), enemyLoc.getX() - newX, enemyLoc.getY(), enemyLoc.getZ() - newZ, enemyLoc.getYaw(), enemyLoc.getPitch());

                            player.teleport(newPlayerLoc);
                            player.attack(enemy);
                            k[0]++;
                        }, 0, 15);
                }
            }, AbilityType.RIGHT_CLICK, "Back Stab", 15, true, "Rapidly teleports you behind", "the §e5 §7nearest enemies,", "dealing the sword's", "critical damage"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageByEntityEvent e)) return;

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
                    }, AbilityType.PASSIVE, "_", 0, false, ""),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PlayerDropItemEvent e)) return;

                            if (!Item.isCustomItem(e.getItemDrop().getItemStack()) || !Item.toItem(e.getItemDrop().getItemStack()).equals(ItemList.shadowFury)) return;
                            ItemStack is = e.getItemDrop().getItemStack();
                            if (Item.getCritChance(is) != 100) return;

                            shadowFuryTask.cancel();
                            Item.setStats(Item.getDamage(is), defaultCritChance, Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, ""))
            .lore("Teach your enemies what", "fear really means")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    runeShard.getItemStack(32),
                    null,
                    null,
                    runeShard.getItemStack(32),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item blueZenith = new ItemBuilder(new ItemStack(Material.DIAMOND_SWORD), "Blue Zenith")
            .subType(SubType.LONGSWORD)
            .rarity(Rarity.EPIC)
            .damage(10)
            .critChance(10)
            .critDamage(2)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    RayTraceResult ray = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 8.5, (entity -> !entity.equals(player)));
                    if (ray == null) return;
                    if (ray.getHitEntity() == null) return;

                    player.attack(ray.getHitEntity());
                }
            }, AbilityType.LEFT_CLICK, "HACKS!", 0, false, "Hit mobs from §e8.5 §7blocks"))
            .lore("That's a long ass sword!")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedDiamondBlock.getItemStack(1),
                    null,
                    null,
                    enchantedDiamondBlock.getItemStack(1),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item realKnife = new ItemBuilder(new ItemStack(Material.NETHERITE_SWORD), "Real Knife")
            .subType(SubType.DAGGER)
            .rarity(Rarity.EPIC)
            .damage(15)
            .critChance(25)
            .critDamage(5)
            .lore("\"You think you're above consequences.\"")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    enchantedNetherite.getItemStack(4),
                    null,
                    enchantedNetherite.getItemStack(4),
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();


    private static DelayedTask caladbolgTask;
    public static final Item caladbolg = new ItemBuilder(new ItemStack(Material.IRON_SWORD), "Caladbolg")
            .subType(SubType.SWORD)
            .rarity(Rarity.LEGENDARY)
            .damage(10)
            .critChance(10)
            .critDamage(2.5f)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                        if (!(element instanceof PlayerInteractEvent e)) return;

                        Player player = e.getPlayer();
                        ItemStack is = player.getInventory().getItemInMainHand();
                        ItemMeta meta = is.getItemMeta();
                        if (meta == null) return;
                        if (Utils.validateItem(is, player, 0, e)) return;

                        is.setType(Material.NETHERITE_SWORD);
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
                }
            }, AbilityType.RIGHT_CLICK, "Empower!", 30, true, "Does double damage for a", "short period of time"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PlayerDropItemEvent e)) return;

                            if (!Item.isCustomItem(e.getItemDrop().getItemStack()) || !Item.toItem(e.getItemDrop().getItemStack()).equals(ItemList.caladbolg)) return;
                            ItemStack is = e.getItemDrop().getItemStack();
                            if (is.getType() != Material.NETHERITE_SWORD) return;

                            caladbolgTask.cancel();
                            Item.setStats(Item.getDamage(is) / 2, Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
                            is.setType(ItemList.caladbolg.getItemStack().getType());
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, ""))
            .lore("Once branded by the irish", "hero Fergus mac Róich")
            .enchantments(new HashMap<>() {{
                put(Enchantment.DAMAGE_ALL, 6);
                put(Enchantment.SWEEPING_EDGE, 4);
            }})
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedIronBlock.getItemStack(1),
                    null,
                    null,
                    realKnife.getItemStack(1),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    private static DelayedTask mjolnirTask;
    public static final Item mjolnir = new ItemBuilder(new ItemStack(Material.IRON_AXE), "Mjölnir")
            .subType(SubType.HAMMER)
            .rarity(Rarity.DIVINE)
            .damage(10)
            .critChance(10)
            .critDamage(2.5f)
            .abilities(new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageByEntityEvent e)) return;

                            if (!(e.getDamager() instanceof Player player)) return;
                            if (!(e.getEntity() instanceof LivingEntity entity)) return;
                            ItemStack is = player.getInventory().getItemInMainHand();
                            if (Utils.validateItem(is, player, 0, e)) return;
                            ItemMeta meta = is.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();

                            if (!container.has(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER)) return;
                            if (container.get(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER) != 20) return;

                            entity.getWorld().strikeLightning(entity.getLocation());
                        }
                    }, AbilityType.HIT, "Strike", 0, false, "Strike lightning when hitting an enemy", "if the weapon is fully charged"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof PlayerInteractEvent e)) return;

                            Player player = e.getPlayer();
                            ItemStack is = player.getInventory().getItemInMainHand();
                            if (Utils.validateItem(is, player, 1, e)) return;
                            ItemStack i = Utils.findCustomItemInInv(ItemList.mjolnir, player.getInventory());
                            if (i == null) return;
                            ItemMeta meta = i.getItemMeta();
                            assert meta != null;
                            PersistentDataContainer container = meta.getPersistentDataContainer();
                            int charges = 0;
                            if (container.has(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER))
                                charges = container.get(new NamespacedKey(plugin, "charges"), PersistentDataType.INTEGER);

                            if (charges > 2)
                                setChargesMjolnir(i, charges - 2);

                            int finalCharges = charges;
                            // this is awful help me
                            Events.throwItem(player, is, new Instruction() {
                                @Override
                                public <T> void run(T element) {
                                    if (!(element instanceof Entity entity)) return;

                                    if (finalCharges > 2)
                                        player.getWorld().strikeLightning(entity.getLocation());
                                }
                            }, 50);
                        }
                    }, AbilityType.RIGHT_CLICK, "Smite", 1, false, "Throw the weapon at the enemy", "to deal the weapon's damage and", "strike them with lightning, consuming", "2 charges"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDamageByEntityEvent e)) return;

                            if (e.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) return;
                            if (!(e.getEntity() instanceof Player player)) return;
                            ItemStack is = Utils.findCustomItemInInv(ItemList.mjolnir, player.getInventory());
                            if (is == null) return;
                            if (Utils.validateItem(is, player, 2, e)) return;
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
                    }, AbilityType.PASSIVE, "Charge", 1, false, "Charge this weapon by getting", "struck by lightning. The higher the",
                            "energy stored, the higher the", "damage. Energy decays over", "time"))
            .lore("§6[§8:::::§8:::::§6/§8:::::§8:::::§8:::::§8:::::§6/§8:::::§8:::::§8:::::§8:::::§8:::::§8:::::§6/§8:::::§8:::::§8:::::§8:::::§8:::::§8:::::§8:::::§8:::::§6]")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedNetherite.getItemStack(8),
                    enchantedDiamondBlock.getItemStack(2),
                    enchantedNetherite.getItemStack(8),
                    null,
                    new ItemStack(Material.LIGHTNING_ROD),
                    null,
                    null,
                    enchantedCopper.getItemStack(16),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item shortBow = new ItemBuilder(new ItemStack(Material.BOW), "Short Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.UNCOMMON)
            .damage(2)
            .critDamage(2)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    e.setCancelled(true);

                    ItemStack[] contents = player.getInventory().getStorageContents();

                    player.launchProjectile(Arrow.class);

                    for (ItemStack i : contents)
                        if (player.getGameMode() != GameMode.CREATIVE && i != null && i.getType() == Material.ARROW) {
                            i.setAmount(i.getAmount() - 1);
                            break;
                        }
                }
            }, AbilityType.CLICK, "Fast Shot", 0, false, "Instantly shoots an arrow"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    new ItemStack(Material.STICK),
                    enchantedString.getItemStack(4),
                    new ItemStack(Material.STICK),
                    null,
                    enchantedString.getItemStack(4),
                    null,
                    new ItemStack(Material.STICK),
                    enchantedString.getItemStack(4)
            ))
            .build();

    private static ItemStack explosiveBowItem;
    public static final Item explosiveBow = new ItemBuilder(new ItemStack(Material.BOW), "Explosive Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.RARE)
            .damage(3)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof ProjectileHitEvent e)) return;

                            if (!(e.getEntity() instanceof Arrow arrow)) return;
                            if (!(arrow.getShooter() instanceof Player player)) return;
                            if (Utils.validateItem(explosiveBowItem, player, 0, e)) return;

                            player.getWorld().createExplosion(arrow.getLocation(), 3, false);
                }
            }, AbilityType.PASSIVE, "Explosive Threat", 0, false, "The arrows explode on impact"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ProjectileLaunchEvent e)) return;

                            if (!(e.getEntity() instanceof Arrow arrow)) return;
                            if (!(arrow.getShooter() instanceof Player player)) return;
                            explosiveBowItem = player.getInventory().getItemInMainHand();
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, ""))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    explosiveStaff.getItemStack(),
                    new ItemStack(Material.TNT),
                    explosiveStaff.getItemStack(),
                    null,
                    new ItemStack(Material.TNT),
                    null,
                    explosiveStaff.getItemStack(),
                    new ItemStack(Material.TNT)
            ))
            .build();

    private static String soulBowUUID;
    private static ItemStack soulBowItem;
    public static final Item soulBow = new ItemBuilder(new ItemStack(Material.BOW), "Soul Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.EPIC)
            .damage(5)
            .critDamage(1.5f)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof ProjectileHitEvent e)) return;

                            if (!(e.getEntity() instanceof Arrow arrow)) return;
                            if (!(arrow.getShooter() instanceof Player player)) return;
                            LivingEntity hit = (LivingEntity) e.getHitEntity();
                            if (hit == null) return;
                            if (Utils.validateItem(soulBowItem, player, 0, e)) return;
                            soulBowItem = null;

                            if (player.getGameMode() != GameMode.CREATIVE) {
                                if (player.getHealth() > 3)
                                    player.setHealth(player.getHealth() - 3);
                                else {
                                    player.sendMessage("§cYou don't have enough health to do that!");
                                    return;
                                }
                            }

                            Wolf wolf = (Wolf) hit.getWorld().spawnEntity(hit.getLocation(), EntityType.WOLF);
                            wolf.setTamed(true);
                            wolf.setOwner(player);
                            wolf.setAdult();

                            soulBowUUID = player.getUniqueId().toString();
                            wolf.addScoreboardTag("wolf");
                            player.addScoreboardTag(soulBowUUID);
                }
            }, AbilityType.HIT, "Woof Woof!", 0, false, "Spawns a wolf on impact", "that helps you in battle!", "§8§oCost: 1.5 Hearts"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ProjectileLaunchEvent e)) return;

                            if (!(e.getEntity() instanceof Arrow arrow)) return;
                            if (!(arrow.getShooter() instanceof Player player)) return;
                            soulBowItem = player.getInventory().getItemInMainHand();
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, ""),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof EntityDeathEvent e)) return;

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
                    }, AbilityType.PASSIVE, "_", 0, false, ""))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedBone.getItemStack(8),
                    enchantedString.getItemStack(8),
                    enchantedBone.getItemStack(8),
                    null,
                    enchantedString.getItemStack(8),
                    null,
                    enchantedBone.getItemStack(8),
                    enchantedString.getItemStack(8)
            ))
            .build();

    public static final Item grapplingHook = new ItemBuilder(new ItemStack(Material.FISHING_ROD), "Grappling Hook")
            .type(Type.TOOL)
            .rarity(Rarity.UNCOMMON)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerFishEvent e)) return;

                    if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    Location playerLoc = player.getLocation();
                    Location hookLoc = e.getHook().getLocation();
                    Location change = hookLoc.subtract(playerLoc);
                    player.setVelocity(change.toVector().multiply(0.3).setY(1));
                }
            }, AbilityType.RIGHT_CLICK, "Grapple", 2, true, "Makes you fly in the direction", "of the hook"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    new ItemStack(Material.STICK),
                    enchantedString.getItemStack(8),
                    new ItemStack(Material.STICK),
                    null,
                    enchantedString.getItemStack(8)
            ))
            .build();

    public static final Item hookShot = new ItemBuilder(new ItemStack(Material.FISHING_ROD), "Hook Shot")
            .type(Type.TOOL)
            .rarity(Rarity.RARE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerFishEvent e)) return;

                    if (e.getState() != PlayerFishEvent.State.REEL_IN && e.getState() != PlayerFishEvent.State.IN_GROUND) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (Utils.validateItem(is, player, 0, e)) return;

                    Location playerLoc = player.getLocation();
                    Location hookLoc = e.getHook().getLocation();
                    Location change = hookLoc.subtract(playerLoc);
                    player.setVelocity(change.toVector().multiply(0.75).setY(1.25));
                }
            }, AbilityType.RIGHT_CLICK, "Grapple MK2", 1, true, "Makes you fly in the direction", "of the hook...", "but way faster"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null,
                    grapplingHook.getItemStack(),
                    enchantedSilk.getItemStack(8),
                    new ItemStack(Material.STICK),
                    null,
                    enchantedSilk.getItemStack(8)
            ))
            .build();

    public static final Item fireTalisman = new ItemBuilder(new ItemStack(Material.GOLDEN_CARROT, 2), "Fire Talisman")
            .subType(SubType.TALISMAN)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;
                    if (e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.LAVA) return;
                    ItemStack is = Utils.findCustomItemInInv(ItemList.fireTalisman, player.getInventory());
                    if (is == null) return;

                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

                    is.setAmount(is.getAmount() - 1);
                }
            }, AbilityType.PASSIVE, "Lava Protection", 300, true, "On contact with a source of fire", "gives fire protection for 30s", "and regeneration 2 for 10s"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.BLAZE_POWDER),
                    new ItemStack(Material.MAGMA_CREAM),
                    new ItemStack(Material.BLAZE_POWDER),
                    new ItemStack(Material.MAGMA_CREAM),
                    new ItemStack(Material.GOLDEN_CARROT),
                    new ItemStack(Material.MAGMA_CREAM),
                    new ItemStack(Material.BLAZE_POWDER),
                    new ItemStack(Material.MAGMA_CREAM),
                    new ItemStack(Material.BLAZE_POWDER)
            ))
            .build();

    public static final Item reforgeStone = new ItemBuilder(new ItemStack(Material.HEART_OF_THE_SEA), "Reforge Stone")
            .subType(SubType.TALISMAN)
            .rarity(Rarity.RARE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof PrepareAnvilEvent e)) return;

                            ItemStack stone = e.getInventory().getItem(1);
                            if (stone == null) return;
                            if (Utils.validateItem(stone, (Player) e.getInventory().getViewers().getFirst(), 0, e)) return;

                            ItemStack is = e.getInventory().getItem(0);
                            if (is == null) return;
                            Item item = Item.toItem(is);
                            if (item == null) return;
                            ItemStack result = is.clone();
                            e.setResult(result);
                            new DelayedTask(() -> e.getInventory().setRepairCost(3));
                }
            }, AbilityType.PASSIVE, "Re-roll", 0, false, "Combine with a custom item in", "an anvil to change its reforge"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof InventoryClickEvent e)) return;

                            if (e.getInventory().getType() != InventoryType.ANVIL) return;
                            ItemStack stone = e.getInventory().getItem(1);
                            if (stone == null) return;
                            if (Utils.validateItem(stone, (Player) e.getInventory().getViewers().getFirst(), 1, e)) return;
                            ItemStack is = e.getCurrentItem();
                            if (is == null) return;
                            Item item = Item.toItem(is);
                            if (item == null) return;
                            Reforge oldReforge = Reforge.getReforge(e.getInventory().getItem(0));
                            Reforge newReforge = Reforge.randomReforge(item.getType());
                            if (newReforge == null) return;

                            while (true) {
                                assert newReforge != null;
                                if (!newReforge.equals(oldReforge)) break;
                                newReforge = Reforge.randomReforge(item.getType());
                            }

                            Reforge.setReforge(newReforge, is);
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, ""))
            .lore("A dice roll may not always be favorable,", "learn how to change the outcome")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    runeShard.getItemStack(4),
                    null,
                    runeShard.getItemStack(4),
                    enchantedGold.getItemStack(),
                    runeShard.getItemStack(4),
                    null,
                    runeShard.getItemStack(4),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item recombobulator = new ItemBuilder(new ItemStack(Material.RED_DYE), "Recombobulator")
            .subType(SubType.TALISMAN)
            .rarity(Rarity.LEGENDARY)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof PrepareAnvilEvent e)) return;

                            ItemStack recomb = e.getInventory().getItem(1);
                            if (recomb == null) return;
                            if (Utils.validateItem(recomb, (Player) e.getInventory().getViewers().getFirst(), 0, e)) return;

                            ItemStack is = e.getInventory().getItem(0);
                            if (is == null) return;
                            Item item = Item.toItem(is);
                            if (item == null) return;
                            ItemStack result = is.clone();
                            ItemMeta resultMeta = result.getItemMeta();
                            if (resultMeta == null) return;
                            PersistentDataContainer container = resultMeta.getPersistentDataContainer();
                            if (container.has(new NamespacedKey(plugin, "recombed"), PersistentDataType.BOOLEAN)) return;
                            if (Item.getRarity(result).ordinal() + 1 == Rarity.values().length - 2) return;

                            Utils.recombItem(result, is);

                            e.setResult(result);
                            new DelayedTask(() -> e.getInventory().setRepairCost(3));
                }
            }, AbilityType.PASSIVE, "Upgrade", 0, false, "Combine with a custom item in", "an anvil to upgrade it's", "rarity once"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    runeShard.getItemStack(16),
                    null,
                    runeShard.getItemStack(16),
                    enchantedDiamond.getItemStack(16),
                    runeShard.getItemStack(16),
                    null,
                    runeShard.getItemStack(16),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item brain = new ItemBuilder(new ItemStack(Material.FERMENTED_SPIDER_EYE), "Zombie Brain")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityTargetLivingEntityEvent e)) return;

                    if (!(e.getTarget() instanceof Player player)) return;
                    if (!(e.getEntity() instanceof Monster)) return;
                    if (!Utils.hasCustomItemInInv(ItemList.brain, player.getInventory())) return;

                    e.setCancelled(true);
                }
            }, AbilityType.PASSIVE, "Intimidation", 0, false, "Don't go around holding a brain,", "most monsters are now scared", "of you!"))
            .craftingType(CraftingType.DROP)
            .entityDrops(new HashMap<>(){{
                put(1D, Arrays.asList(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN));
                put(0.5, Collections.singletonList(EntityType.ZOMBIE_HORSE));
            }})
            .addInfo("Drop Chance:",
                    "- §2Zombies §8= §e1% (1/100)",
                    "- §2Zombie Horse §8= §e0.5% (1/200)")
            .build();

    public static final Item rabbitFoot = new ItemBuilder(new ItemStack(Material.RABBIT_FOOT), "Lucky Foot")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.EPIC)
            .isGlint(true)
            .abilities(new Ability(EventList.NONE, AbilityType.PASSIVE, "Lucky Rabbit", 0, false, "All random outcomes are rolled 1", "more time for a §bfavorable", "§boutcome"))
            .craftingType(CraftingType.DROP)
            .entityDrops(new HashMap<>(){{
                put(0.1, Collections.singletonList(EntityType.RABBIT));
            }})
            .addInfo("Drop Chance:",
                    "- §6Rabbit §8= §e0.1% (1/1.000)")
            .build();

    public static final Item purity = new ItemBuilder(new ItemStack(Material.LARGE_AMETHYST_BUD), "Purity")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.EPIC)
            .isGlint(true)
            .abilities(new Ability(EventList.NONE, AbilityType.PASSIVE, "Fast Hands", 0, false, "Reduces all the cooldowns by 25%"),
                    new Ability(EventList.NONE, AbilityType.PASSIVE, "Unlucky Fate", 0, false, "All random outcomes are rolled 1", "more time for an §cunfavorable", "§coutcome"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4),
                    null,
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4),
                    runeShard.getItemStack(4)
            ))
            .build();

    public static final Item autoRecomb = new ItemBuilder(new ItemStack(Material.RED_DYE), "Auto-Recombobulator")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.MYTHIC)
            .isGlint(true)
            .abilities(new Ability(EventList.NONE, AbilityType.PASSIVE, "Automatic Upgrade", 0, false, "Chance to automatically", "recombobulate any item", "you craft or drop"))
            .addInfo("Upgrade Chance:",
                    "- §e0.1% (1/1.000)")
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    runeShard.getItemStack(16),
                    reforgeStone.getItemStack(1),
                    runeShard.getItemStack(16),
                    reforgeStone.getItemStack(1),
                    recombobulator.getItemStack(1),
                    reforgeStone.getItemStack(1),
                    runeShard.getItemStack(16),
                    reforgeStone.getItemStack(1),
                    runeShard.getItemStack(16)
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item slimeBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Slime Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .defence(2)
            .color(Color.LIME)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerMoveEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getBoots();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    if (player.getVelocity().getY() > -0.515) return;

                    final short BLOCKS_MIN_TO_CREATE_PAD = 4;
                    for (int i = 0; i < BLOCKS_MIN_TO_CREATE_PAD; i++) {
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
                                    new DelayedTask(() -> blocks.get(finalJ).setType(states.get(finalJ)), 2 * 20);
                                }
                            }

                            break;
                        }
                    }
                }
            }, AbilityType.PASSIVE, "Safe Landing", 0, false, "Creates a pad of slime blocks", "that stops your fall"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    new ItemStack(Material.SLIME_BLOCK, 64),
                    null,
                    new ItemStack(Material.SLIME_BLOCK, 64),
                    new ItemStack(Material.SLIME_BLOCK, 64),
                    null,
                    new ItemStack(Material.SLIME_BLOCK, 64)
            ))
            .build();

    public static final Item springBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Spring Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.UNCOMMON)
            .damage(2)
            .color(Color.GRAY)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerJumpEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = player.getInventory().getBoots();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    if (player.isSneaking())
                        player.setVelocity(player.getVelocity().multiply(0.5).setY(1));
                }
            }, AbilityType.SHIFT, "Jump Up, Super Star!", 0, false, "It's time to jump up in the air"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    new ItemStack(Material.COPPER_INGOT, 32),
                    null,
                    new ItemStack(Material.COPPER_INGOT, 32),
                    new ItemStack(Material.COPPER_INGOT, 32),
                    null,
                    new ItemStack(Material.COPPER_INGOT, 32)
            ))
            .build();

    public static final Item farmerBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Farmer Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.UNCOMMON)
            .health(3)
            .color(Color.OLIVE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof CropTrampleEvent e)) return;

                    if (!(e.getTrampler() instanceof Player player)) return;
                    ItemStack is = player.getInventory().getBoots();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    e.setCancelled(true);
                }
            }, AbilityType.PASSIVE, "Anti-Trample", 0, false, "Your crops are going to be safe now!"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    enchantedSeed.getItemStack(8),
                    null,
                    enchantedSeed.getItemStack(8),
                    enchantedSeed.getItemStack(8),
                    null,
                    enchantedSeed.getItemStack(8)
            ))
            .build();

    public static final Item midasCrown = new ItemBuilder(new ItemStack(Material.GOLDEN_HELMET), "Midas' Crown")
            .subType(SubType.HELMET)
            .rarity(Rarity.RARE)
            .defence(2)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDeathEvent e)) return;

                    Player player = e.getEntity().getKiller();
                    if (player == null) return;
                    ItemStack helmet = player.getInventory().getHelmet();
                    if (helmet == null) return;
                    if (Utils.validateItem(helmet, player, 0, e)) return;

                    ChanceManager.chanceCalculation(50, new Instruction() {
                        @Override
                        public void run() {
                            e.getDrops().add(new ItemStack(Material.GOLD_INGOT));
                        }
                    }, player);
                }
            }, AbilityType.PASSIVE, "Golden Luck", 0, false, "Enemies might drop gold when", "killed!"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedGold.getItemStack(8),
                    enchantedGold.getItemStack(8),
                    enchantedGold.getItemStack(8),
                    enchantedGold.getItemStack(8),
                    null,
                    enchantedGold.getItemStack(8),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item shelmet = new ItemBuilder(new ItemStack(Material.TURTLE_HELMET), "Shelmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.RARE)
            .health(5)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageByEntityEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;

                    ItemStack is = player.getInventory().getHelmet();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    new DelayedTask(() -> player.setVelocity(new Vector()));
                }
            }, AbilityType.PASSIVE, "KnockBack Resistant", 0, false, "Still as a statue"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.BRICK, 64),
                    new ItemStack(Material.BRICK, 64),
                    new ItemStack(Material.BRICK, 64),
                    new ItemStack(Material.BRICK, 64),
                    null,
                    new ItemStack(Material.BRICK, 64),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item speedHelmet = new ItemBuilder(new ItemStack(Material.LEATHER_HELMET), "Speed Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.EPIC)
            .health(2)
            .color(Color.WHITE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getNewArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() + (0.1f * 0.2f));
                            }
                }
            }, AbilityType.PASSIVE, "Light Weight", 0, false, "Gives +10% of base speed while equipped"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getOldArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() - (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "unequip"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item speedChestplate = new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE), "Speed Chestplate")
            .subType(SubType.CHESTPLATE)
            .rarity(Rarity.EPIC)
            .health(3)
            .color(Color.WHITE)
            .abilities(new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getNewArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() + (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "Light Weight", 0, false, "Gives +10% of base speed while equipped"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getOldArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() - (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "unequip"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8)
            ))
            .build();

    public static final Item speedLeggings = new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS), "Speed Leggings")
            .subType(SubType.LEGGINGS)
            .rarity(Rarity.EPIC)
            .health(3)
            .color(Color.WHITE)
            .abilities(new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getNewArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() + (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "Light Weight", 0, false, "Gives +10% of base speed while equipped"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getOldArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() - (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "unequip"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8)
            ))
            .build();

    public static final Item speedBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Speed Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.EPIC)
            .health(2)
            .color(Color.WHITE)
            .abilities(new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getNewArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() + (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "Light Weight", 0, false, "Gives +10% of base speed while equipped"),
                    new Ability(new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            if (!(element instanceof ArmorEquipEvent e)) return;

                            if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR) {
                                Player player = e.getPlayer();
                                ItemStack is = e.getOldArmorPiece();
                                if (Utils.validateArmor(is, targetSpeedArmor)) return;

                                player.setWalkSpeed(player.getWalkSpeed() - (0.1f * 0.2f));
                            }
                        }
                    }, AbilityType.PASSIVE, "_", 0, false, "unequip"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8),
                    meth.getItemStack(8),
                    null,
                    meth.getItemStack(8)
            ))
            .build();
    final static Item[] targetSpeedArmor = { speedHelmet, speedChestplate, speedLeggings, speedBoots };

    public static final Item protectorHelmet = new ItemBuilder(new ItemStack(Material.DIAMOND_HELMET), "Protector Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.EPIC)
            .health(4)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    if (!Utils.hasFullSet(armorContents, targetProtectorArmor))
                        return;
                    if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
                        return;

                    double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    if (e.getDamage() < 0.25 * maxHealth) return;

                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                    player.sendMessage("§aYou have been protected!");

                    Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getAbilities().get(0).cooldown());
                }
            }, AbilityType.FULL_SET, "Hail Mary", 60, true, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack(),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item protectorChestplate = new ItemBuilder(new ItemStack(Material.DIAMOND_CHESTPLATE), "Protector Chestplate")
            .subType(SubType.CHESTPLATE)
            .rarity(Rarity.EPIC)
            .health(5)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    if (!Utils.hasFullSet(armorContents, targetProtectorArmor))
                        return;
                    if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
                        return;

                    double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    if (e.getDamage() < 0.25 * maxHealth) return;

                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                    player.sendMessage("§aYou have been protected!");

                    Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getAbilities().get(0).cooldown());
                }
            }, AbilityType.FULL_SET, "Hail Mary", 60, true, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack()
            ))
            .build();

    public static final Item protectorLeggings = new ItemBuilder(new ItemStack(Material.DIAMOND_LEGGINGS), "Protector Leggings")
            .subType(SubType.LEGGINGS)
            .rarity(Rarity.EPIC)
            .health(5)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    if (!Utils.hasFullSet(armorContents, targetProtectorArmor))
                        return;
                    if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
                        return;

                    double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    if (e.getDamage() < 0.25 * maxHealth) return;

                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                    player.sendMessage("§aYou have been protected!");

                    Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getAbilities().get(0).cooldown());
                }
            }, AbilityType.FULL_SET, "Hail Mary", 60, true, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack()
            ))
            .build();

    public static final Item protectorBoots = new ItemBuilder(new ItemStack(Material.DIAMOND_BOOTS), "Protector Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.EPIC)
            .health(4)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof EntityDamageEvent e)) return;

                    if (!(e.getEntity() instanceof Player player)) return;
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    if (!Utils.hasFullSet(armorContents, targetProtectorArmor))
                        return;
                    if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey()))
                        return;

                    double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    if (e.getDamage() < 0.25 * maxHealth) return;

                    e.setCancelled(true);
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1);
                    player.sendMessage("§aYou have been protected!");

                    Cooldowns.setCooldown(player.getUniqueId(), ItemList.protectorHelmet.getKey(), ItemList.protectorHelmet.getAbilities().get(0).cooldown());
                }
            }, AbilityType.FULL_SET, "Hail Mary", 60, true, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack(),
                    enchantedDiamond.getItemStack(),
                    null,
                    enchantedDiamond.getItemStack()
            ))
            .build();
    final static Item[] targetProtectorArmor = { protectorBoots, protectorLeggings, protectorChestplate, protectorHelmet };

    private static DelayedTask setFire;
    public static final Item fireHelmet = new ItemBuilder(new ItemStack(Material.LEATHER_HELMET), "Fire Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.RARE)
            .damage(2)
            .color(Color.YELLOW)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof ArmorEquipEvent e)) return;

                    if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
                    Player player = e.getPlayer();
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    ItemStack is = e.getNewArmorPiece();
                    if (Utils.validateArmor(is, targetFireArmor)) return;

                    if (!Utils.hasFullSet(armorContents, targetFireArmor, e.getType(), e.getNewArmorPiece()))
                        return;

                    setFire = new DelayedTask(() -> {
                        if (!Utils.hasFullSet(player.getInventory().getArmorContents(), targetFireArmor))
                            setFire.cancel();

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        if (!entities.isEmpty())
                            for (Entity entity : entities)
                                if (entity instanceof LivingEntity livingEntity)
                                    livingEntity.setFireTicks(20);
                    }, 0, 20);
                }
            }, AbilityType.FULL_SET, "TorchMan", 0, false, "Set all near enemies on fire"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item fireChestplate = new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE), "Fire Chestplate")
            .subType(SubType.CHESTPLATE)
            .rarity(Rarity.RARE)
            .damage(2)
            .color(Color.ORANGE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof ArmorEquipEvent e)) return;

                    if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
                    Player player = e.getPlayer();
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    ItemStack is = e.getNewArmorPiece();
                    if (Utils.validateArmor(is, targetFireArmor)) return;

                    if (!Utils.hasFullSet(armorContents, targetFireArmor, e.getType(), e.getNewArmorPiece()))
                        return;

                    setFire = new DelayedTask(() -> {
                        if (!Utils.hasFullSet(player.getInventory().getArmorContents(), targetFireArmor))
                            setFire.cancel();

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        if (!entities.isEmpty())
                            for (Entity entity : entities)
                                if (entity instanceof LivingEntity livingEntity)
                                    livingEntity.setFireTicks(20);
                    }, 0, 20);
                }
            }, AbilityType.FULL_SET, "TorchMan", 0, false, "Set all near enemies on fire"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8)
            ))
            .build();

    public static final Item fireLeggings = new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS), "Fire Leggings")
            .subType(SubType.LEGGINGS)
            .rarity(Rarity.RARE)
            .damage(2)
            .color(Color.ORANGE)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof ArmorEquipEvent e)) return;

                    if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
                    Player player = e.getPlayer();
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    ItemStack is = e.getNewArmorPiece();
                    if (Utils.validateArmor(is, targetFireArmor)) return;

                    if (!Utils.hasFullSet(armorContents, targetFireArmor, e.getType(), e.getNewArmorPiece()))
                        return;

                    setFire = new DelayedTask(() -> {
                        if (!Utils.hasFullSet(player.getInventory().getArmorContents(), targetFireArmor))
                            setFire.cancel();

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        if (!entities.isEmpty())
                            for (Entity entity : entities)
                                if (entity instanceof LivingEntity livingEntity)
                                    livingEntity.setFireTicks(20);
                    }, 0, 20);
                }
            }, AbilityType.FULL_SET, "TorchMan", 0, false, "Set all near enemies on fire"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8)
            ))
            .build();

    public static final Item fireBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Fire Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .damage(2)
            .color(Color.RED)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof ArmorEquipEvent e)) return;

                    if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
                    Player player = e.getPlayer();
                    ItemStack[] armorContents = player.getInventory().getArmorContents();
                    ItemStack is = e.getNewArmorPiece();
                    if (Utils.validateArmor(is, targetFireArmor)) return;

                    if (!Utils.hasFullSet(armorContents, targetFireArmor, e.getType(), e.getNewArmorPiece()))
                        return;

                    setFire = new DelayedTask(() -> {
                        if (!Utils.hasFullSet(player.getInventory().getArmorContents(), targetFireArmor))
                            setFire.cancel();

                        List<Entity> entities = player.getNearbyEntities(5, 5, 5);
                        if (!entities.isEmpty())
                            for (Entity entity : entities)
                                if (entity instanceof LivingEntity livingEntity)
                                    livingEntity.setFireTicks(20);
                    }, 0, 20);
                }
            }, AbilityType.FULL_SET, "TorchMan", 0, false, "Set all near enemies on fire"))
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8),
                    magmaRod.getItemStack(8),
                    null,
                    magmaRod.getItemStack(8)
            ))
            .build();
    final static Item[] targetFireArmor = { fireBoots, fireLeggings, fireChestplate, fireHelmet };

    //Unobtainable
    public final static Item cheatCode = new ItemBuilder(new ItemStack(Material.STONE), "Cheat Code")
            .type(Type.ITEM)
            .rarity(Rarity.SUPREME)
            .isGlint(true)
            .abilities(new Ability(new Instruction() {
                @Override
                public <E> void run(E element) {
                    if (!(element instanceof PlayerInteractEvent e)) return;

                    Player player = e.getPlayer();
                    ItemStack is = e.getItem();
                    if (is == null) return;
                    if (Utils.validateItem(is, player, 0, e)) return;

                    if (player.getGameMode() == GameMode.CREATIVE)
                        player.setGameMode(GameMode.SURVIVAL);
                    else
                        player.setGameMode(GameMode.CREATIVE);
                }
            }, AbilityType.RIGHT_CLICK, "↑ ↑  ↓ ↓  ← →  ← →  B A", 0, false, "Switches between creatvive", "and survival mode"))
            .showInGui(false)
            .hasRandomUUID(true)
            .build();

    //Auxiliary methods
    static short recCeil;
    private static void recursiveBreakBlock(Material type, List<Block> blocks, ItemStack is) {
        for (Block b : blocks)
            if (b.getType() == type) {
                b.breakNaturally(is);
                if (recCeil == 24) return;
                Bukkit.getScheduler().runTaskLater(plugin, () -> recursiveBreakBlock(type, Utils.getBlocksInRadius(b, new Vector3(1, 1, 1)), is), 2);
                recCeil++;
            }
    }

    public static void setChargesMjolnir(ItemStack is, int charges) {
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
                    case 2, 6, 12, 20 ->
                            Item.setStats((int) Math.floor(Item.getDamage(is) * 1.25f), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
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
                    case 1, 5, 11, 19 ->
                            Item.setStats((int) Math.ceil(Item.getDamage(is) / 1.25f), Item.getCritChance(is), Item.getCritDamage(is), Item.getHealth(is), Item.getDefence(is), is, false);
                }
                ItemMeta meta1 = is.getItemMeta();
                List<String> lore = meta1.getLore();
                if (lore == null) return;
                for (String line : lore)
                    if (line.contains("[")) {
                        int index = lore.indexOf(line);
                        int last;
                        try {
                            last = line.lastIndexOf("§b");
                        } catch (StringIndexOutOfBoundsException exception) {
                            last = 0;
                        }
                        String newLine = line.substring(0, last) + "§8" + line.substring(last + 2);
                        lore.set(index, newLine);
                        Item.setLore(is, lore);
                        break;
                    }
            }
    }

    //Utils items
    public static final Item fillerGlass = new UtilsBuilder(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ").build();
    public static final Item abilitiesGlass = new UtilsBuilder(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), " ").build();
    public static final Item craftingGlass = new UtilsBuilder(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), "§aSelect Material").lore("§e§lLEFT CLICK §7- §fselect material type", "§e§lRIGHT CLICK §7- §fselect material amount").build();
    public static final Item nextArrow = new UtilsBuilder(new ItemStack(Material.ARROW), "§rNext").build();
    public static final Item backArrow = new UtilsBuilder(new ItemStack(Material.ARROW), "§rBack").build();
    public static final Item itemSword = new UtilsBuilder(new ItemStack(Material.DIAMOND_SWORD), "§eItems").lore("§aClick to see all the materials!").isGlint(true).build();
    public static final Item matsDiamond = new UtilsBuilder(new ItemStack(Material.DIAMOND), "§eMaterials").lore("§aClick to see all the items!").isGlint(true).build();
    public static final Item closeBarrier = new UtilsBuilder(new ItemStack(Material.BARRIER), "§cClose").build();
    public static final Item shapedCrafting = new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aShaped Recipe").lore("§fThis recipe needs to be replicated", "§fin this exact order").build();
    public static final Item shapelessCrafting = new UtilsBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aShapeless Recipe").lore("§fThis recipe can be done in any order").build();
    public static final Item furnaceCrafting = new UtilsBuilder(new ItemStack(Material.FURNACE), "§aFurnace Recipe").build();
    public static final Item upgradeCrafting = new UtilsBuilder(new ItemStack(Material.ANVIL), "§aUpgrade Recipe").build();
    public static final Item showAddInfo = new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§eShow Additional Information").lore("§cHidden!").build();
}
