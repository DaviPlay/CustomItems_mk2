package davide.customitems.Lists;

import davide.customitems.API.*;
import davide.customitems.Events.StonkEvents;
import davide.customitems.ItemCreation.Item;
import davide.customitems.ItemCreation.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ItemList {

    //Materials
    public static final Item enchantedBone = new ItemBuilder(new ItemStack(Material.BONE), "Enchanted Bone")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.BONE, 32),
                    new ItemStack(Material.BONE, 32),
                    new ItemStack(Material.BONE, 32),
                    new ItemStack(Material.BONE, 32),
                    new ItemStack(Material.BONE, 32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedString = new ItemBuilder(new ItemStack(Material.STRING), "Enchanted String")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.STRING, 32),
                    new ItemStack(Material.STRING, 32),
                    new ItemStack(Material.STRING, 32),
                    new ItemStack(Material.STRING, 32),
                    new ItemStack(Material.STRING, 32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedSilk = new ItemBuilder(new ItemStack(Material.COBWEB), "Enchanted Silk")
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

    public static final Item enchantedGold = new ItemBuilder(new ItemStack(Material.GOLD_INGOT), "Enchanted Gold")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.GOLD_INGOT, 32),
                    new ItemStack(Material.GOLD_INGOT, 32),
                    new ItemStack(Material.GOLD_INGOT, 32),
                    new ItemStack(Material.GOLD_INGOT, 32),
                    new ItemStack(Material.GOLD_INGOT, 32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item enchantedGoldBlock = new ItemBuilder(new ItemStack(Material.GOLD_BLOCK), "Enchanted Gold Block")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
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

    public static final Item enchantedDiamond = new ItemBuilder(new ItemStack(Material.DIAMOND), "Enchanted Diamond")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
            .craftingType(CraftingType.SHAPED)
            .crafting(Arrays.asList(
                    new ItemStack(Material.DIAMOND, 32),
                    new ItemStack(Material.DIAMOND, 32),
                    new ItemStack(Material.DIAMOND, 32),
                    new ItemStack(Material.DIAMOND, 32),
                    new ItemStack(Material.DIAMOND, 32),
                    null,
                    null,
                    null,
                    null
            ))
            .build();

    public static final Item meltedSugar = new ItemBuilder(new ItemStack(Material.WHITE_DYE, 4), "Melted Sugar")
            .type(Type.MATERIAL)
            .rarity(Rarity.UNCOMMON)
            .isGlint(true)
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
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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
            .lore("Every " + StonkEvents.getBlocksMax() + " blocks mined gain", "haste 5 for 20 seconds", "§e" + StonkEvents.getBlocksMax() + " §8blocks remaining")
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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

    public static final Item explosiveWand = new ItemBuilder(new ItemStack(Material.STICK), "Explosive Wand")
            .subType(SubType.STAFF)
            .rarity(Rarity.UNCOMMON)
            .lore("Creates an explosion that", "doesn't damages the player", "§8Destroyed on use")
            .isStackable(false)
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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

    public static final Item ultimateBread = new ItemBuilder(new ItemStack(Material.BREAD), "Ultimate Bread")
            .type(Type.FOOD)
            .rarity(Rarity.LEGENDARY)
            .lore("This special food gives you", "saturation for 5 minutes!", "§8§oStackable!")
            .isGlint(true)
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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

    public static final Item soulBow = new ItemBuilder(new ItemStack(Material.BOW), "Soul Bow")
            .subType(SubType.BOW)
            .rarity(Rarity.EPIC)
            .lore("Spawns a wolf on impact", "that helps you in battle!", "§8§oCost: 1.5 Hearts")
            .abilities(Collections.singletonList(Ability.GENERIC))
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

    public static final Item cocaine = new ItemBuilder(new ItemStack(Material.SUGAR), "Cocaine")
            .type(Type.FOOD)
            .rarity(Rarity.RARE)
            .lore("A helpful tool to escape from", "weird situations, but don't do", "too much of it in a small amount of time!")
            .isGlint(true)
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
            .delay(30)
            .showDelay(false)
            .craftingType(CraftingType.SHAPELESS)
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
            .lore("Teleports you 8 blocks", "from your current position", "in the direction you're facing")
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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
            .build();

    public static final Item caladbolg = new ItemBuilder(new ItemStack(Material.DIAMOND_SWORD), "Caladbolg")
            .subType(SubType.SWORD)
            .rarity(Rarity.LEGENDARY)
            .damage(10)
            .lore("Does double damage for a", "short period of time")
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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

    public static final Item grapplingHook = new ItemBuilder(new ItemStack(Material.FISHING_ROD), "Grappling Hook")
            .type(Type.TOOL)
            .rarity(Rarity.UNCOMMON)
            .lore("Makes you fly in the direction", "of the hook")
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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
            .abilities(Collections.singletonList(Ability.RIGHT_CLICK))
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
            .type(Type.FOOD)
            .rarity(Rarity.RARE)
            .lore("On contact with a source of fire", "gives fire protection for 30s", "and regeneration 2 for 10s")
            .isGlint(true)
            .abilities(Collections.singletonList(Ability.GENERIC))
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

    public static final Item slimeBoots = new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS), "Slime Boots")
            .subType(SubType.BOOTS)
            .rarity(Rarity.RARE)
            .lore("Creates a pad of slime blocks", "that stops your fall")
            .color(Color.LIME)
            .abilities(Collections.singletonList(Ability.GENERIC))
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

    public static final Item midasStaff = new ItemBuilder(new ItemStack(Material.TOTEM_OF_UNDYING), "Midas Staff")
            .subType(SubType.STAFF)
            .rarity(Rarity.LEGENDARY)
            .lore("Upon hitting a mob it", "turns into gold", "/s", "Gold, gold everywhere!")
            .abilities(Arrays.asList(Ability.LEFT_CLICK, Ability.SHIFT_RIGHT_CLICK))
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
            {recipeBook, stonk, explosiveWand, ultimateBread, soulBow, cocaine, aspectOfTheEnd, caladbolg, grapplingHook, hookShot, fireTalisman, slimeBoots, midasStaff},

            //Materials
            {enchantedBone, enchantedString, enchantedSilk, enchantedGold, enchantedGoldBlock, enchantedDiamond, meltedSugar}
    };

}
