package davide.customitems.api;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class SpecialBlocks {

    public static boolean isClickableBlock(Block b) {
        return b.getType().isInteractable();
    }

    public static List<Material> getDyes() {
        return Arrays.asList(
                Material.BLACK_DYE, Material.BLUE_DYE, Material.BROWN_DYE, Material.CYAN_DYE, Material.GRAY_DYE, Material.GREEN_DYE, Material.LIME_DYE, Material.LIGHT_BLUE_DYE,
                Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.PINK_DYE, Material.LIGHT_GRAY_DYE, Material.PURPLE_DYE, Material.RED_DYE, Material.WHITE_DYE, Material.YELLOW_DYE
        );
    }

    public static List<Material> getOres() {
        return Arrays.asList(
                Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.LAPIS_ORE,
                Material.DEEPSLATE_LAPIS_ORE, Material.NETHER_QUARTZ_ORE, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE,
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.ANCIENT_DEBRIS
        );
    }

    public static List<Material> getStones() {
        return Arrays.asList(
                Material.STONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE, Material.DEEPSLATE, Material.DRIPSTONE_BLOCK, Material.NETHERRACK, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM,
                Material.TUFF, Material.CALCITE, Material.BLACKSTONE, Material.BASALT, Material.SMOOTH_BASALT, Material.END_STONE
        );
    }

    public static List<Material> getLogs() {
         return Arrays.asList(
                Material.ACACIA_LOG, Material.BIRCH_LOG, Material.CHERRY_LOG, Material.MANGROVE_LOG, Material.SPRUCE_LOG, Material.OAK_LOG, Material.JUNGLE_LOG, Material.DARK_OAK_LOG, Material.CRIMSON_STEM,
                Material.WARPED_STEM
         );
    }

    public static List<Material> getShovelBlocks() {
         return Arrays.asList(
                Material.DIRT, Material.DIRT_PATH, Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.PODZOL, Material.MUD, Material.GRASS_BLOCK, Material.FARMLAND, Material.MYCELIUM, Material.CLAY,
                Material.SAND, Material.RED_SAND, Material.SUSPICIOUS_SAND, Material.GRAVEL, Material.SUSPICIOUS_GRAVEL, Material.BLACK_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER, Material.BROWN_CONCRETE_POWDER,
                Material.CYAN_CONCRETE_POWDER, Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.YELLOW_CONCRETE_POWDER,
                Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER,
                Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.MUDDY_MANGROVE_ROOTS, Material.SNOW, Material.SNOW_BLOCK, Material.SOUL_SAND, Material.SOUL_SOIL, Material.POWDER_SNOW
         );
    }

    public static List<Material> getPickaxeBlocks() {
         return Arrays.asList(
                Material.OBSIDIAN, Material.CRYING_OBSIDIAN, Material.RESPAWN_ANCHOR, Material.NETHERITE_BLOCK, Material.ANCIENT_DEBRIS, Material.ENDER_CHEST, Material.ANVIL, Material.BELL, Material.COAL_BLOCK,
                Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.IRON_BLOCK, Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK, Material.RAW_IRON_BLOCK, Material.REDSTONE_BLOCK, Material.CHAIN, Material.ENCHANTING_TABLE,
                Material.IRON_BARS, Material.IRON_DOOR, Material.IRON_TRAPDOOR, Material.SPAWNER, Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE,
                Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.BLAST_FURNACE, Material.COBBLED_DEEPSLATE, Material.CHISELED_DEEPSLATE,
                Material.DEEPSLATE_BRICKS, Material.DEEPSLATE_TILES, Material.POLISHED_DEEPSLATE, Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.LANTERN, Material.LODESTONE, Material.SMOKER, Material.STONECUTTER,
                Material.CONDUIT, Material.GOLD_BLOCK, Material.LAPIS_BLOCK, Material.COAL_ORE, Material.COPPER_ORE, Material.COPPER_BLOCK, Material.CUT_COPPER, Material.EXPOSED_CUT_COPPER, Material.EXPOSED_COPPER, Material.OXIDIZED_COPPER,
                Material.OXIDIZED_CUT_COPPER, Material.WAXED_CUT_COPPER, Material.WAXED_EXPOSED_COPPER, Material.WAXED_OXIDIZED_COPPER, Material.WAXED_WEATHERED_COPPER, Material.WAXED_WEATHERED_CUT_COPPER, Material.WEATHERED_COPPER,
                Material.WEATHERED_CUT_COPPER, Material.WAXED_COPPER_BLOCK, Material.WAXED_EXPOSED_CUT_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER, Material.CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB,
                Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB, Material.POWDER_SNOW_CAULDRON,
                Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS, Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS,
                Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Material.DEEPSLATE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.END_STONE, Material.GOLD_ORE, Material.HOPPER,
                Material.IRON_ORE, Material.LIGHTNING_ROD, Material.LAPIS_ORE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE, Material.OBSERVER, Material.REDSTONE_ORE, Material.BLUE_ICE, Material.GRINDSTONE,
                Material.BONE_BLOCK, Material.BRICK_STAIRS, Material.BRICKS, Material.BRICK_SLAB, Material.CAULDRON, Material.LAVA_CAULDRON, Material.COBBLESTONE, Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE,
                Material.MOSSY_COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_WALL, Material.MOSSY_STONE_BRICKS, Material.MOSSY_STONE_BRICK_SLAB, Material.MOSSY_STONE_BRICK_STAIRS, Material.MOSSY_STONE_BRICK_WALL,
                Material.INFESTED_MOSSY_STONE_BRICKS, Material.NETHER_BRICKS, Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_SLAB, Material.NETHER_BRICK_WALL, Material.NETHER_BRICK_STAIRS, Material.RED_NETHER_BRICK_SLAB,
                Material.RED_NETHER_BRICKS, Material.RED_NETHER_BRICK_WALL, Material.RED_NETHER_BRICK_STAIRS, Material.POLISHED_BLACKSTONE, Material.POLISHED_BLACKSTONE_BRICKS, Material.POLISHED_BLACKSTONE_BUTTON, Material.WATER_CAULDRON,
                Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, Material.POLISHED_BLACKSTONE_BRICKS, Material.POLISHED_BLACKSTONE_SLAB, Material.POLISHED_BLACKSTONE_STAIRS, Material.POLISHED_BLACKSTONE_WALL, Material.POLISHED_BLACKSTONE_BRICKS,
                Material.POLISHED_BLACKSTONE_BRICK_SLAB, Material.POLISHED_BLACKSTONE_BRICK_STAIRS, Material.POLISHED_BLACKSTONE_BRICK_WALL, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, Material.CHISELED_POLISHED_BLACKSTONE,
                Material.BLACKSTONE, Material.BLACKSTONE_SLAB, Material.BLACKSTONE_STAIRS, Material.BLACKSTONE_WALL, Material.GILDED_BLACKSTONE, Material.DEEPSLATE_TILES, Material.DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_SLAB,
                Material.DEEPSLATE_BRICK_STAIRS, Material.DEEPSLATE_BRICK_WALL, Material.DEEPSLATE_TILE_SLAB, Material.DEEPSLATE_TILE_STAIRS, Material.DEEPSLATE_TILE_WALL, Material.CRACKED_DEEPSLATE_BRICKS, Material.CRACKED_DEEPSLATE_TILES,
                Material.DEEPSLATE_BRICKS, Material.DEEPSLATE_BRICK_WALL, Material.DEEPSLATE_BRICK_STAIRS, Material.DEEPSLATE_BRICK_SLAB, Material.COBBLED_DEEPSLATE, Material.COBBLED_DEEPSLATE_SLAB, Material.COBBLED_DEEPSLATE_STAIRS,
                Material.COBBLED_DEEPSLATE_WALL, Material.INFESTED_DEEPSLATE, Material.REINFORCED_DEEPSLATE, Material.POLISHED_DEEPSLATE_WALL, Material.POLISHED_DEEPSLATE_SLAB, Material.POLISHED_DEEPSLATE_STAIRS, Material.SMOOTH_STONE,
                Material.END_STONE_BRICK_SLAB, Material.END_STONE_BRICKS, Material.END_STONE_BRICK_STAIRS, Material.END_STONE_BRICK_WALL, Material.STONE, Material.STONE_SLAB, Material.STONE_BUTTON, Material.STONE_STAIRS,
                Material.STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.END_STONE_BRICK_WALL, Material.STONE_BRICK_STAIRS, Material.CHISELED_STONE_BRICKS, Material.CHISELED_BOOKSHELF, Material.STONE_PRESSURE_PLATE, Material.INFESTED_STONE,
                Material.INFESTED_CHISELED_STONE_BRICKS, Material.PURPUR_BLOCK, Material.PURPUR_PILLAR, Material.PURPUR_SLAB, Material.PURPUR_STAIRS, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.QUARTZ_PILLAR, Material.QUARTZ_SLAB,
                Material.QUARTZ_STAIRS, Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_SLAB, Material.CHISELED_QUARTZ_BLOCK, Material.SANDSTONE, Material.SANDSTONE_SLAB, Material.SANDSTONE_STAIRS, Material.SANDSTONE_WALL,
                Material.CHISELED_SANDSTONE, Material.SMOOTH_SANDSTONE, Material.SMOOTH_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE_STAIRS, Material.CUT_SANDSTONE, Material.CUT_SANDSTONE_SLAB, Material.RED_SANDSTONE, Material.RED_SANDSTONE_SLAB,
                Material.RED_SANDSTONE_STAIRS, Material.RED_SANDSTONE_WALL, Material.CHISELED_RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE, Material.SMOOTH_RED_SANDSTONE_SLAB, Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.CUT_RED_SANDSTONE,
                Material.CUT_RED_SANDSTONE_SLAB, Material.ANDESITE, Material.ANDESITE_SLAB, Material.ANDESITE_STAIRS, Material.ANDESITE_WALL, Material.POLISHED_ANDESITE, Material.POLISHED_ANDESITE_SLAB, Material.POLISHED_ANDESITE_STAIRS,
                Material.DIORITE, Material.DIORITE_SLAB, Material.DIORITE_STAIRS, Material.DIORITE_WALL, Material.POLISHED_DIORITE, Material.POLISHED_DIORITE_SLAB, Material.POLISHED_DIORITE_STAIRS, Material.GRANITE, Material.GRANITE_SLAB,
                Material.GRANITE_STAIRS, Material.GRANITE_WALL, Material.POLISHED_GRANITE, Material.POLISHED_GRANITE_SLAB, Material.POLISHED_GRANITE_STAIRS, Material.PRISMARINE, Material.PRISMARINE_SLAB, Material.PRISMARINE_STAIRS,
                Material.PRISMARINE_WALL, Material.PRISMARINE_BRICKS, Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_BRICK_STAIRS, Material.DARK_PRISMARINE, Material.DARK_PRISMARINE_SLAB, Material.DARK_PRISMARINE_STAIRS, Material.MUD_BRICKS,
                Material.MUD_BRICK_SLAB, Material.MUD_BRICK_STAIRS, Material.MUD_BRICK_WALL, Material.SHULKER_BOX, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.YELLOW_CONCRETE,
                Material.LIME_CONCRETE, Material.PINK_CONCRETE, Material.GRAY_CONCRETE, Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE, Material.BROWN_CONCRETE, Material.GREEN_CONCRETE,
                Material.RED_CONCRETE, Material.BLACK_CONCRETE, Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA,
                Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA, Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA,
                Material.RED_TERRACOTTA, Material.BLACK_TERRACOTTA, Material.WHITE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA, Material.MAGENTA_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.YELLOW_GLAZED_TERRACOTTA,
                Material.LIME_GLAZED_TERRACOTTA, Material.PINK_GLAZED_TERRACOTTA, Material.GRAY_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.CYAN_GLAZED_TERRACOTTA, Material.PURPLE_GLAZED_TERRACOTTA,
                Material.BLUE_GLAZED_TERRACOTTA, Material.BROWN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.RED_GLAZED_TERRACOTTA, Material.BLACK_GLAZED_TERRACOTTA, Material.DRIPSTONE_BLOCK, Material.POINTED_DRIPSTONE,
                Material.TUFF, Material.SMALL_AMETHYST_BUD, Material.MEDIUM_AMETHYST_BUD, Material.LARGE_AMETHYST_BUD, Material.BUDDING_AMETHYST, Material.AMETHYST_BLOCK, Material.AMETHYST_CLUSTER, Material.BASALT, Material.SMOOTH_BASALT,
                Material.POLISHED_BASALT, Material.PACKED_MUD, Material.CALCITE, Material.RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL, Material.POWERED_RAIL, Material.BREWING_STAND, Material.ICE, Material.MAGMA_BLOCK, Material.PACKED_ICE,
                Material.FROSTED_ICE, Material.NETHERRACK, Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.PETRIFIED_OAK_SLAB
         );
    }

    public static List<Material> getAxeBlocks() {
        return Arrays.asList(
                Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.MANGROVE_TRAPDOOR, Material.CHERRY_TRAPDOOR,
                Material.BAMBOO_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.WARPED_TRAPDOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR,
                Material.MANGROVE_DOOR, Material.CHERRY_DOOR, Material.BAMBOO_DOOR, Material.CRIMSON_DOOR, Material.WARPED_DOOR, Material.BARREL, Material.CARTOGRAPHY_TABLE, Material.CHEST, Material.TRAPPED_CHEST, Material.CRAFTING_TABLE,
                Material.FLETCHING_TABLE, Material.LECTERN, Material.LOOM, Material.SMITHING_TABLE, Material.BAMBOO_MOSAIC, Material.BAMBOO_MOSAIC_SLAB, Material.BAMBOO_MOSAIC_STAIRS, Material.BAMBOO_BLOCK, Material.CAMPFIRE,
                Material.OAK_FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE, Material.MANGROVE_FENCE, Material.CHERRY_FENCE, Material.BAMBOO_FENCE,
                Material.CRIMSON_FENCE, Material.WARPED_FENCE, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.DARK_OAK_FENCE_GATE,
                Material.MANGROVE_FENCE_GATE, Material.CHERRY_FENCE_GATE, Material.BAMBOO_FENCE_GATE, Material.CRIMSON_FENCE_GATE, Material.WARPED_FENCE_GATE, Material.JUKEBOX, Material.NOTE_BLOCK, Material.OAK_LOG, Material.SPRUCE_LOG,
                Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.CRIMSON_STEM, Material.WARPED_STEM, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG,
                Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_CHERRY_LOG, Material.STRIPPED_CRIMSON_STEM,
                Material.STRIPPED_WARPED_STEM, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.BIRCH_PLANKS, Material.JUNGLE_PLANKS, Material.ACACIA_PLANKS, Material.DARK_OAK_PLANKS, Material.MANGROVE_PLANKS, Material.CHERRY_PLANKS,
                Material.BAMBOO_PLANKS, Material.CRIMSON_PLANKS, Material.WARPED_PLANKS, Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.BIRCH_SLAB, Material.JUNGLE_SLAB, Material.ACACIA_SLAB, Material.DARK_OAK_SLAB,
                Material.MANGROVE_SLAB, Material.CHERRY_SLAB, Material.BAMBOO_SLAB, Material.CRIMSON_SLAB, Material.WARPED_SLAB, Material.OAK_STAIRS, Material.SPRUCE_STAIRS, Material.BIRCH_STAIRS, Material.JUNGLE_STAIRS,
                Material.ACACIA_STAIRS, Material.DARK_OAK_STAIRS, Material.MANGROVE_STAIRS, Material.CHERRY_STAIRS, Material.BAMBOO_STAIRS, Material.CRIMSON_STAIRS, Material.WARPED_STAIRS, Material.BOOKSHELF, Material.CHISELED_BOOKSHELF,
                Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER, Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER, Material.LIGHT_GRAY_BANNER,
                Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER, Material.BLACK_BANNER, Material.WHITE_WALL_BANNER, Material.ORANGE_WALL_BANNER,
                Material.MAGENTA_WALL_BANNER, Material.LIGHT_BLUE_WALL_BANNER, Material.YELLOW_WALL_BANNER, Material.LIME_WALL_BANNER, Material.PINK_WALL_BANNER, Material.GRAY_WALL_BANNER, Material.LIGHT_GRAY_WALL_BANNER,
                Material.CYAN_WALL_BANNER, Material.PURPLE_WALL_BANNER, Material.BLUE_WALL_BANNER, Material.BROWN_WALL_BANNER, Material.GREEN_WALL_BANNER, Material.RED_WALL_BANNER, Material.BLACK_WALL_BANNER, Material.JACK_O_LANTERN,
                Material.PUMPKIN, Material.MELON, Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_SIGN, Material.DARK_OAK_SIGN, Material.MANGROVE_SIGN, Material.CHERRY_SIGN,
                Material.BAMBOO_SIGN, Material.CRIMSON_SIGN, Material.WARPED_SIGN, Material.OAK_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.BIRCH_WALL_SIGN, Material.JUNGLE_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.DARK_OAK_WALL_SIGN,
                Material.MANGROVE_WALL_SIGN, Material.CHERRY_WALL_SIGN, Material.BAMBOO_WALL_SIGN, Material.CRIMSON_WALL_SIGN, Material.WARPED_WALL_SIGN, Material.OAK_HANGING_SIGN, Material.SPRUCE_HANGING_SIGN, Material.BIRCH_HANGING_SIGN,
                Material.JUNGLE_HANGING_SIGN, Material.ACACIA_HANGING_SIGN, Material.DARK_OAK_HANGING_SIGN, Material.MANGROVE_HANGING_SIGN, Material.CHERRY_HANGING_SIGN, Material.BAMBOO_HANGING_SIGN, Material.CRIMSON_HANGING_SIGN,
                Material.WARPED_HANGING_SIGN, Material.MUDDY_MANGROVE_ROOTS, Material.MANGROVE_ROOTS, Material.OAK_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE,
                Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.MANGROVE_PRESSURE_PLATE, Material.CHERRY_PRESSURE_PLATE, Material.BAMBOO_PRESSURE_PLATE, Material.CRIMSON_PRESSURE_PLATE, Material.WARPED_PRESSURE_PLATE,
                Material.BEEHIVE, Material.LADDER, Material.BEE_NEST, Material.COMPOSTER, Material.BAMBOO, Material.STRIPPED_BAMBOO_BLOCK, Material.WHITE_BED, Material.ORANGE_BED, Material.MAGENTA_BED, Material.LIGHT_BLUE_BED, Material.YELLOW_BED,
                Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.CYAN_BED, Material.PURPLE_BED, Material.BLUE_BED, Material.BROWN_BED, Material.GREEN_BED, Material.RED_BED, Material.BLACK_BED, Material.COCOA,
                Material.DAYLIGHT_DETECTOR, Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK, Material.CAVE_VINES, Material.TWISTING_VINES, Material.WEEPING_VINES, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON,
                Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.DARK_OAK_BUTTON, Material.MANGROVE_BUTTON, Material.CHERRY_BUTTON, Material.BAMBOO_BUTTON, Material.CRIMSON_BUTTON, Material.WARPED_BUTTON
        );
    }

    public static List<Material> getHoeBlocks() {
        return Arrays.asList(
                Material.SCULK, Material.SCULK_VEIN, Material.SCULK_CATALYST, Material.SCULK_SHRIEKER, Material.SCULK_SENSOR, Material.CALIBRATED_SCULK_SENSOR, Material.NETHER_WART_BLOCK, Material.WARPED_WART_BLOCK, Material.SHROOMLIGHT,
                Material.HAY_BLOCK, Material.TARGET, Material.DRIED_KELP_BLOCK, Material.SPONGE, Material.WET_SPONGE, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES,
                Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES, Material.AZALEA_LEAVES, Material.FLOWERING_AZALEA_LEAVES, Material.MOSS_BLOCK
        );
    }

    public static List<Material> getArmor() {
        return Arrays.asList(
                Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
                Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
                Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
                Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
        );
    }

    public static boolean isDye(Material type) {
        return getDyes().contains(type);
    }

    public static boolean isOre(Material type) {
        return getOres().contains(type);
    }

    public static boolean isStone(Material type) {
        return getStones().contains(type);
    }

    public static boolean isLog(Material type) {
        return getLogs().contains(type);
    }

    public static boolean isShovelBlock(Material type) {
        return getShovelBlocks().contains(type);
    }

    public static boolean isPickaxeBlock(Material type) {
        return getPickaxeBlocks().contains(type);
    }

    public static boolean isAxeBlock(Material type) {
        return getAxeBlocks().contains(type);
    }

    public static boolean isHoeBlock(Material type) {
        return getHoeBlocks().contains(type);
    }

    public static boolean isArmor(Material type) {
        return getArmor().contains(type);
    }
}
