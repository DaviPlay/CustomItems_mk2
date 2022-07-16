package davide.customitems.lists;

import davide.customitems.crafting.CraftingType;
import davide.customitems.events.EventListener;
import davide.customitems.itemCreation.*;
import davide.customitems.itemCreation.MaterialBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ItemList {

    //Materials
    public static final Item enchantedBone = new MaterialBuilder(new ItemStack(Material.BONE), "Enchanted Bone")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedString = new MaterialBuilder(new ItemStack(Material.STRING), "Enchanted String")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedSilk = new MaterialBuilder(new ItemStack(Material.COBWEB), "Enchanted Silk")
            .type(Type.MATERIAL)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedString.getItemStack(32),
                    enchantedString.getItemStack(32),
                    enchantedString.getItemStack(32),
                    enchantedString.getItemStack(32),
                    enchantedString.getItemStack(32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedIron = new MaterialBuilder(new ItemStack(Material.IRON_INGOT), "Enchanted Iron")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedIronBlock = new ItemBuilder(new ItemStack(Material.IRON_BLOCK), "Enchanted Iron Block")
            .type(Type.MATERIAL)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedIron.getItemStack(32),
                    enchantedIron.getItemStack(32),
                    enchantedIron.getItemStack(32),
                    enchantedIron.getItemStack(32),
                    enchantedIron.getItemStack(32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedGold = new MaterialBuilder(new ItemStack(Material.GOLD_INGOT), "Enchanted Gold")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedGoldBlock = new ItemBuilder(new ItemStack(Material.GOLD_BLOCK), "Enchanted Gold Block")
            .type(Type.MATERIAL)
            .rarity(Rarity.RARE)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    enchantedGold.getItemStack(32),
                    enchantedGold.getItemStack(32),
                    enchantedGold.getItemStack(32),
                    enchantedGold.getItemStack(32),
                    enchantedGold.getItemStack(32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedDiamond = new MaterialBuilder(new ItemStack(Material.DIAMOND), "Enchanted Diamond")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedSeed = new MaterialBuilder(new ItemStack(Material.WHEAT_SEEDS), "Enchanted Seed")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item enchantedCobble = new MaterialBuilder(new ItemStack(Material.COBBLESTONE), "Enchanted Cobblestone")
            .rarity(Rarity.UNCOMMON)
            .craftingType(CraftingType.SHAPELESS)
            .build();

    public static final Item burtBlazeRod = new MaterialBuilder(new ItemStack(Material.BLAZE_ROD), "Burnt Blaze Rod")
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

    //Cool items
    public static final Item recipeBook = new ItemBuilder(new ItemStack(Material.BOOK), "Knowledge Book")
            .type(Type.ITEM)
            .rarity(Rarity.COMMON)
            .lore("Shows all the custom recipes added", "by the CustomItems plugins")
            .isGlint(true)
            .abilities(Ability.RIGHT_CLICK)
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
            .build();

    public static final Item stonk = new ItemBuilder(new ItemStack(Material.GOLDEN_PICKAXE), "Stonk")
            .type(Type.TOOL)
            .rarity(Rarity.EPIC)
            .lore("Every " + EventListener.getBlocksMaxStonk() + " blocks mined gain", "haste 5 for 20 seconds", "§e§o" + EventListener.getBlocksMaxStonk() + " §8blocks remaining")
            .abilities(Ability.RIGHT_CLICK)
            .enchantments(new HashMap<Enchantment, Integer>() {{
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

    public static final Item ultimateBread = new ItemBuilder(new ItemStack(Material.BREAD), "Ultimate Bread")
            .type(Type.FOOD)
            .rarity(Rarity.LEGENDARY)
            .lore("This special food gives you", "saturation for 5 minutes!", "§8§oStackable!")
            .isGlint(true)
            .abilities(Ability.RIGHT_CLICK)
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

    public static final Item cocaine = new ItemBuilder(new ItemStack(Material.SUGAR), "Cocaine")
            .type(Type.FOOD)
            .rarity(Rarity.RARE)
            .lore("A helpful tool to escape from", "weird situations, but don't do", "too much of it in a small amount of time!")
            .isGlint(true)
            .abilities(Ability.RIGHT_CLICK)
            .delay(30)
            .showDelay(false)
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

    public static final Item explosiveWand = new ItemBuilder(new ItemStack(Material.STICK), "Explosive Wand")
            .subType(SubType.STAFF)
            .rarity(Rarity.UNCOMMON)
            .lore("Creates an explosion that", "doesn't damages the player", "§8§oDestroyed on use")
            .abilities(Ability.RIGHT_CLICK)
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

    public static final Item aspectOfTheEnd = new ItemBuilder(new ItemStack(Material.STICK), "Aspect Of The End")
            .subType(SubType.WAND)
            .rarity(Rarity.RARE)
            .damage(-1)
            .lore("Teleports you 8 blocks", "from your current position", "in the direction you're facing")
            .abilities(Ability.RIGHT_CLICK)
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

    public static final Item lightningStaff = new ItemBuilder(new ItemStack(Material.END_ROD), "Lightning Wand")
            .subType(SubType.STAFF)
            .rarity(Rarity.UNCOMMON)
            .lore("Cast a lighting where", "you're facing")
            .abilities(Ability.PASSIVE)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    new ItemStack(Material.ENDER_EYE, 64),
                    null,
                    new ItemStack(Material.COPPER_INGOT, 32),
                    null,
                    new ItemStack(Material.COPPER_INGOT, 32),
                    null,
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item judger = new ItemBuilder(new ItemStack(Material.WOODEN_SHOVEL), "The Judger")
            .subType(SubType.MACE)
            .rarity(Rarity.UNCOMMON)
            .damage(3)
            .critChance(5)
            .lore("Judge every enemy under", "15% of it's max health")
            .abilities(Ability.PASSIVE)
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
            .lore("Launch the axe")
            .abilities(Ability.RIGHT_CLICK)
            .delay(1)
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

    public static final Item vampiresFang = new ItemBuilder(new ItemStack(Material.GHAST_TEAR), "Vampire's Fang")
            .subType(SubType.DAGGER)
            .rarity(Rarity.UNCOMMON)
            .damage(7)
            .critChance(10)
            .lore("Heals for 25% of the", "dealt damage")
            .isGlint(true)
            .abilities(Ability.PASSIVE)
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

    public static final Item caladbolg = new ItemBuilder(new ItemStack(Material.DIAMOND_SWORD), "Caladbolg")
            .subType(SubType.SWORD)
            .rarity(Rarity.LEGENDARY)
            .damage(15)
            .critChance(10)
            .lore("Does double damage for a", "short period of time")
            .abilities(Ability.RIGHT_CLICK)
            .delay(30)
            .enchantments(new HashMap<Enchantment, Integer>() {{
                put(Enchantment.DAMAGE_ALL, 6);
                put(Enchantment.SWEEPING_EDGE, 4);
            }})
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    enchantedDiamond.getItemStack(16),
                    null,
                    null,
                    enchantedDiamond.getItemStack(16),
                    null,
                    null,
                    new ItemStack(Material.STICK),
                    null
            ))
            .hasRandomUUID(true)
            .build();

    public static final Item soulBow = new ItemBuilder(new ItemStack(Material.BOW), "Soul Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.EPIC)
            .damage(5)
            .lore("Spawns a wolf on impact", "that helps you in battle!", "§8§oCost: 1.5 Hearts")
            .abilities(Ability.PASSIVE)
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

    public static final Item shortBow = new ItemBuilder(new ItemStack(Material.BOW), "Short Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.RARE)
            .damage(2)
            .lore("Instantly shoots an arrow")
            .abilities(Ability.PASSIVE)
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

    public static final Item grapplingHook = new ItemBuilder(new ItemStack(Material.FISHING_ROD), "Grappling Hook")
            .type(Type.TOOL)
            .rarity(Rarity.UNCOMMON)
            .lore("Makes you fly in the direction", "of the hook")
            .abilities(Ability.RIGHT_CLICK)
            .delay(2)
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
            .lore("Makes you fly in the direction", "of the hook...", "but double the fun")
            .abilities(Ability.RIGHT_CLICK)
            .delay(1)
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
            .rarity(Rarity.RARE)
            .lore("On contact with a source of fire", "gives fire protection for 30s", "and regeneration 2 for 10s")
            .isGlint(true)
            .abilities(Ability.PASSIVE)
            .delay(300)
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

    public static final Item midasStaff = new ItemBuilder(new ItemStack(Material.TOTEM_OF_UNDYING), "Midas Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.LEGENDARY)
            .lore("Upon hitting a mob it", "turns into gold", "/s", "Gold, gold everywhere!")
            .abilities(Ability.LEFT_CLICK, Ability.SHIFT_RIGHT_CLICK)
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

    public static final Item slimeBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Slime Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .health(2)
            .lore("Creates a pad of slime blocks", "that stops your fall")
            .color(Color.LIME)
            .abilities(Ability.PASSIVE)
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
            .lore("Shift to jump higher")
            .color(Color.GRAY)
            .abilities(Ability.SHIFT)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    new ItemStack(Material.IRON_INGOT, 32),
                    null,
                    new ItemStack(Material.IRON_INGOT, 32),
                    new ItemStack(Material.IRON_INGOT, 32),
                    null,
                    new ItemStack(Material.IRON_INGOT, 32)
            ))
            .build();

    public static final Item farmerBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Farmer Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.UNCOMMON)
            .health(3)
            .lore("Your crops are going to be safe now!")
            .color(Color.OLIVE)
            .abilities(Ability.PASSIVE)
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

    public static final Item speedHelmet = new ItemBuilder(new ItemStack(Material.LEATHER_HELMET), "Speed Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.EPIC)
            .health(2)
            .lore("Gives +10% speed while equipped")
            .color(Color.WHITE)
            .abilities(Ability.PASSIVE)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item speedChestplate = new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE), "Speed Chestplate")
            .subType(SubType.CHESTPLATE)
            .rarity(Rarity.EPIC)
            .health(3)
            .lore("Gives +10% speed while equipped")
            .color(Color.WHITE)
            .abilities(Ability.PASSIVE)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8)
            ))
            .build();

    public static final Item speedLeggings = new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS), "Speed Leggings")
            .subType(SubType.LEGGINGS)
            .rarity(Rarity.EPIC)
            .health(3)
            .lore("Gives +10% speed while equipped")
            .color(Color.WHITE)
            .abilities(Ability.PASSIVE)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8)
            ))
            .build();

    public static final Item speedBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Speed Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.EPIC)
            .health(2)
            .lore("Gives +10% speed while equipped")
            .color(Color.WHITE)
            .abilities(Ability.PASSIVE)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8),
                    cocaine.getItemStack(8),
                    null,
                    cocaine.getItemStack(8)
            ))
            .build();

    public static final Item protectorHelmet = new ItemBuilder(new ItemStack(Material.DIAMOND_HELMET), "Protector Helmet")
            .subType(SubType.HELMET)
            .rarity(Rarity.EPIC)
            .health(4)
            .lore("If you take damage for more then", "25% of your max health prevent", "the damage")
            .abilities(Ability.FULL_SET)
            .delay(60)
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
            .lore("If you take damage for more then", "25% of your max health prevent", "the damage")
            .abilities(Ability.FULL_SET)
            .delay(60)
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
            .lore("If you take damage for more then", "25% of your max health prevent", "the damage")
            .abilities(Ability.FULL_SET)
            .delay(60)
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
            .lore("If you take damage for more then", "25% of your max health prevent", "the damage")
            .abilities(Ability.FULL_SET)
            .delay(60)
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
            .lore("Set all near enemies on fire")
            .color(Color.YELLOW)
            .abilities(Ability.FULL_SET)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8),
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item fireChestplate = new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE), "Fire Chestplate")
            .subType(SubType.CHESTPLATE)
            .rarity(Rarity.RARE)
            .damage(2)
            .lore("Set all near enemies on fire")
            .color(Color.ORANGE)
            .abilities(Ability.FULL_SET)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8)
            ))
            .build();

    public static final Item fireLeggings = new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS), "Fire Leggings")
            .subType(SubType.LEGGINGS)
            .rarity(Rarity.RARE)
            .damage(2)
            .lore("Set all near enemies on fire")
            .color(Color.ORANGE)
            .abilities(Ability.FULL_SET)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8)
            ))
            .build();

    public static final Item fireBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Fire Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .damage(2)
            .lore("Set all near enemies on fire")
            .color(Color.RED)
            .abilities(Ability.FULL_SET)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    null,
                    null,
                    null,
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8),
                    burtBlazeRod.getItemStack(8),
                    null,
                    burtBlazeRod.getItemStack(8)
            ))
            .build();

    //Utils items
    public static final Item fillerGlass = new ItemBuilder(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ").build();
    public static final Item itemArrow = new ItemBuilder(new ItemStack(Material.ARROW), "Items").build();
    public static final Item matsArrow = new ItemBuilder(new ItemStack(Material.ARROW), "Materials").build();
    public static final Item shapedCrafting = new ItemBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aShaped Recipe").lore("§fThis recipe needs to be replicated", "§fin this exact order").build();
    public static final Item shapelessCrafting = new ItemBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aShapeless Recipe").lore("§fThis recipe can be done in any order").build();
    public static final Item furnaceCrafting = new ItemBuilder(new ItemStack(Material.CRAFTING_TABLE), "§aFurnace Recipe").build();

    //Items array
    public static Item[][] items = {
            //Items
            {
              recipeBook, stonk, ultimateBread, cocaine, explosiveWand, aspectOfTheEnd, lightningStaff, judger, throwingAxe, vampiresFang, caladbolg, soulBow, shortBow, grapplingHook, hookShot, fireTalisman, midasStaff, springBoots,
              slimeBoots, farmerBoots, speedHelmet, speedChestplate, speedLeggings, speedBoots, protectorHelmet, protectorChestplate, protectorLeggings, protectorBoots, fireHelmet, fireChestplate, fireLeggings, fireBoots
            },

            //Materials
            { enchantedBone, enchantedString, enchantedSilk, enchantedIron, enchantedIronBlock, enchantedGold, enchantedGoldBlock, enchantedDiamond, enchantedSeed, enchantedCobble, burtBlazeRod, meltedSugar }
    };

}
