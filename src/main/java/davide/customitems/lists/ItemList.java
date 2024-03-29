package davide.customitems.lists;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.EventListener;
import davide.customitems.itemCreation.*;
import davide.customitems.itemCreation.ItemBuilder;
import davide.customitems.itemCreation.MaterialBuilder;
import davide.customitems.itemCreation.UtilsBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemList {

    //Items 2d List
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
            .craftingType(CraftingType.NONE)
            .addInfo("§7Drop Chance:",
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

    //Cool items
    public static final Item recipeBook = new ItemBuilder(new ItemStack(Material.BOOK), "Knowledge Book")
            .type(Type.ITEM)
            .rarity(Rarity.COMMON)
            .isGlint(true)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Omniscience", "Shows all the custom recipes added", "by the CustomItems plugins"))
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

    public static final Item stonk = new ItemBuilder(new ItemStack(Material.GOLDEN_PICKAXE), "Stonk")
            .subType(SubType.PICKAXE)
            .rarity(Rarity.EPIC)
            .abilities(new Ability(AbilityType.PASSIVE, "Mine! Mine! Mine!", "Every " + EventListener.getBlocksMaxStonk() + " blocks mined gain", "haste 5 for 20 seconds"))
            .lore("§e§o" + EventListener.getBlocksMaxStonk() + " §o§8blocks remaining")
            .enchantments(new HashMap<>() {{
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
            .abilities(new Ability(AbilityType.PASSIVE, "VeinMiner v2.0", "Automatically mines all the adjacent ores"))
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
            .rarity(Rarity.RARE)
            .abilities(new Ability(AbilityType.PASSIVE, "Hazard Miner", "Chance to break up to 2", "additional blocks around", "the mined one"))
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
            .rarity(Rarity.EPIC)
            .abilities(new Ability(AbilityType.PASSIVE, "Fortune Driller", "Higher chance to break up", "to 5 additional blocks", "around the mined one"))
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
            .abilities(new Ability(AbilityType.PASSIVE, "Environmental Threat", "Automatically chops all the adjacent logs"))
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
            .abilities(new Ability(AbilityType.PASSIVE, "Replenish", "Automatically replants the harvested crops", "using seeds from your inventory"))
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
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Saturated", "This special food gives you", "saturation for 5 minutes!", "§8§oStackable!"))
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

    public static final Item meth = new ItemBuilder(new ItemStack(Material.SUGAR), "Meth")
            .type(Type.FOOD)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Heisenberg", 30, false, "Say my name..."))
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
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Insta-Teleport", "Teleports you 8 blocks", "from your current position", "in the direction you're facing"))
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
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "BoOoM!", "Creates an explosion that", "doesn't damage the player", "§8§oDestroyed on use"))
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
            .abilities(new Ability(AbilityType.CLICK, "Mufulgur", "Cast a lighting on the block", "you're looking at"))
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
            .abilities(new Ability(AbilityType.CLICK, "Fire Ball", "Shoot an explosive fireball"))
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

    public static final Item midasStaff = new ItemBuilder(new ItemStack(Material.TOTEM_OF_UNDYING), "Midas Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.MYTHIC)
            .abilities(
                    new Ability(AbilityType.LEFT_CLICK, "Midas' Touch", "Upon hitting a mob it", "turns into gold"),
                    new Ability(AbilityType.SHIFT_RIGHT_CLICK, "Blessed Feet", "Gold, gold everywhere!"))
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
            .build();

    public static final Item judger = new ItemBuilder(new ItemStack(Material.WOODEN_SHOVEL), "The Executioner")
            .subType(SubType.MACE)
            .rarity(Rarity.UNCOMMON)
            .damage(3)
            .critChance(5)
            .critDamage(2)
            .abilities(new Ability(AbilityType.HIT, "Execute", "Finish every enemy under", "15% of it's max health"))
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

    public static final Item throwingAxe = new ItemBuilder(new ItemStack(Material.STONE_AXE), "Throwing Axe")
            .subType(SubType.GREATAXE)
            .rarity(Rarity.RARE)
            .damage(5)
            .critChance(15)
            .critDamage(3)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Throw", 1, false, "Launch the axe"))
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

    public static final Item venomousDagger = new ItemBuilder(new ItemStack(Material.STONE_SWORD), "Venomous Dagger")
            .subType(SubType.DAGGER)
            .rarity(Rarity.UNCOMMON)
            .damage(3)
            .critDamage(2)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Poison Touch", 1, false, "Poison the enemy on contact"))
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

    public static final Item vampiresFang = new ItemBuilder(new ItemStack(Material.IRON_SWORD), "Vampire's Fang")
            .subType(SubType.DAGGER)
            .rarity(Rarity.UNCOMMON)
            .damage(7)
            .critChance(10)
            .critDamage(1.5f)
            .abilities(new Ability(AbilityType.HIT, "Healing Touch", "Heals for 25% of the", "dealt damage"))
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

    public static final Item shadowFury = new ItemBuilder(new ItemStack(Material.IRON_SWORD), "Shadow Fury")
            .subType(SubType.DAGGER)
            .rarity(Rarity.EPIC)
            .damage(7)
            .critChance(5)
            .critDamage(3)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Back Stab", 15, "Rapidly teleports you behind", "the §e5 §7nearest enemies,", "dealing the sword's", "critical damage"))
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
            .abilities(new Ability(AbilityType.LEFT_CLICK, "Reach", "Hit mobs from §e8.5 §7blocks"))
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

    public static final Item caladbolg = new ItemBuilder(new ItemStack(Material.IRON_SWORD), "Caladbolg")
            .subType(SubType.SWORD)
            .rarity(Rarity.LEGENDARY)
            .damage(15)
            .critChance(10)
            .critDamage(2.5f)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Empower!",  30, "Does double damage for a", "short period of time"))
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
                    enchantedIronBlock.getItemStack(1),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item realKnife = new ItemBuilder(new ItemStack(Material.NETHERITE_SWORD), "Real Knife")
            .subType(SubType.DAGGER)
            .rarity(Rarity.MYTHIC)
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

    public static final Item mjolnir = new ItemBuilder(new ItemStack(Material.IRON_AXE), "Mjölnir")
            .subType(SubType.HAMMER)
            .rarity(Rarity.DIVINE)
            .damage(10)
            .critChance(10)
            .critDamage(2.5f)
            .abilities(new Ability(AbilityType.HIT, "Strike", "Strike lightning when hitting an enemy", "if the weapon is fully charged"),
                    new Ability(AbilityType.RIGHT_CLICK, "Smite", "Throw the weapon at the enemy", "to deal the weapon's damage and", "strike them with lightning, consuming", "2 charges"),
                    new Ability(AbilityType.PASSIVE, "Charge", 1, false, "Charge this weapon by getting", "struck by lightning. The higher the",
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
            .abilities(new Ability(AbilityType.CLICK, "Fast Shoot", "Instantly shoots an arrow"))
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

    public static final Item explosiveBow = new ItemBuilder(new ItemStack(Material.BOW), "Explosive Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.RARE)
            .damage(3)
            .abilities(new Ability(AbilityType.PASSIVE, "Far BoOoM!", "The arrows explode on impact"))
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

    public static final Item soulBow = new ItemBuilder(new ItemStack(Material.BOW), "Soul Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.EPIC)
            .damage(5)
            .critDamage(1.5f)
            .abilities(new Ability(AbilityType.HIT, "Woof Woof!", "Spawns a wolf on impact", "that helps you in battle!", "§8§oCost: 1.5 Hearts"))
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
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Grapple", 2, "Makes you fly in the direction", "of the hook"))
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
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Grapple MK2", 1, "Makes you fly in the direction", "of the hook...", "but double the fun"))
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

    public static final Item rabbitFoot = new ItemBuilder(new ItemStack(Material.RABBIT_FOOT), "Lucky Foot")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.EPIC)
            .isGlint(true)
            .abilities(new Ability(AbilityType.PASSIVE, "Lucky Rabbit", "All random outcomes are rolled 1", "more time for a §bfavorable", "§boutcome"))
            .craftingType(CraftingType.NONE)
            .addInfo("§7Drop Chance:",
                    "§8- §6Rabbit §8= §e0.0001% (1/1.000.000)")
            .build();

    public static final Item purity = new ItemBuilder(new ItemStack(Material.LARGE_AMETHYST_BUD), "Purity")
            .subType(SubType.ACCESSORY)
            .rarity(Rarity.EPIC)
            .isGlint(true)
            .abilities(new Ability(AbilityType.PASSIVE, "Fast Hands", "Reduces all the cooldowns by 25%"),
                    new Ability(AbilityType.PASSIVE, "Unlucky Fate", "All random outcomes are rolled 1", "more time for an §cunfavorable", "§coutcome"))
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

    public static final Item fireTalisman = new ItemBuilder(new ItemStack(Material.GOLDEN_CARROT, 2), "Fire Talisman")
            .subType(SubType.TALISMAN)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .abilities(new Ability(AbilityType.PASSIVE, "Lava Protection", 300, "On contact with a source of fire", "gives fire protection for 30s", "and regeneration 2 for 10s"))
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
            .isGlint(true)
            .abilities(new Ability(AbilityType.PASSIVE, "Re-roll", "Combine with a custom item in", "an anvil to change its reforge"))
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

    public static final Item recombobulator = new ItemBuilder(new ItemStack(Material.FERMENTED_SPIDER_EYE), "Recombobulator")
            .subType(SubType.TALISMAN)
            .rarity(Rarity.LEGENDARY)
            .isGlint(true)
            .abilities(new Ability(AbilityType.PASSIVE, "Upgrade", "Combine with a custom item in", "an anvil to upgrade it's", "rarity once"))
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

    public static final Item slimeBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Slime Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .defence(2)
            .color(Color.LIME)
            .abilities(new Ability(AbilityType.PASSIVE, "Safe Landing", "Creates a pad of slime blocks", "that stops your fall"))
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
            .abilities(new Ability(AbilityType.SHIFT, "Jump Up, Super Star!", "It's time to jump up in the air"))
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
            .abilities(new Ability(AbilityType.PASSIVE, "Anti-Trample", "Your crops are going to be safe now!"))
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

    public static final Item shelmet = new ItemBuilder(new ItemStack(Material.TURTLE_HELMET), "Shelmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.RARE)
            .health(5)
            .abilities(new Ability(AbilityType.PASSIVE, "KnockBack Resistant", "Still as a statue"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Light Weight", "Gives +10% of base speed while equipped"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Light Weight", "Gives +10% of base speed while equipped"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Light Weight", "Gives +10% of base speed while equipped"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Light Weight", "Gives +10% of base speed while equipped"))
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

    public static final Item protectorHelmet = new ItemBuilder(new ItemStack(Material.DIAMOND_HELMET), "Protector Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.EPIC)
            .health(4)
            .abilities(new Ability(AbilityType.FULL_SET, "Hail Mary", 60, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Hail Mary", 60, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Hail Mary", 60, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Hail Mary", 60, "If you take damage for more then", "25% of your max health, prevent", "the damage"))
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

    public static final Item fireHelmet = new ItemBuilder(new ItemStack(Material.LEATHER_HELMET), "Fire Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.RARE)
            .damage(2)
            .color(Color.YELLOW)
            .abilities(new Ability(AbilityType.FULL_SET, "Flame Thrower", "Set all near enemies on fire"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Flame Thrower", "Set all near enemies on fire"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Flame Thrower", "Set all near enemies on fire"))
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
            .abilities(new Ability(AbilityType.FULL_SET, "Flame Thrower", "Set all near enemies on fire"))
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

    //Unobtainable
    public final static Item cheatCode = new ItemBuilder(new ItemStack(Material.STONE), "Cheat Code")
            .type(Type.ITEM)
            .rarity(Rarity.SUPREME)
            .isGlint(true)
            .abilities(new Ability(AbilityType.RIGHT_CLICK, "Cheater!", "↑ ↑  ↓ ↓  ← →  ← →  B A"))
            .showInGui(false)
            .hasRandomUUID(true)
            .build();

    //Utils items
    public static final Item fillerGlass = new UtilsBuilder(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ").build();
    public static final Item craftingGlass = new UtilsBuilder(new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE), "§aSelect Material").lore("§e§lLEFT CLICK §f- select material type", "§e§lRIGHT CLICK §f- select material amount").build();
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
