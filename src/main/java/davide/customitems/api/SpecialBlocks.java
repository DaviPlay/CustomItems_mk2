package davide.customitems.api;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class SpecialBlocks {

    public static boolean isClickableBlock(Block b) {
        return b.getType().isInteractable();

        /*switch (type) {
            case ANVIL:
            //Beds
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case WHITE_BED:
            case PURPLE_BED:
            case RED_BED:
            case YELLOW_BED:
            case BEACON:
            case BELL:
            case BLAST_FURNACE:
            //Boats
            case ACACIA_BOAT:
            case BIRCH_BOAT:
            case DARK_OAK_BOAT:
            case JUNGLE_BOAT:
            case OAK_BOAT:
            case SPRUCE_BOAT:
            case BREWING_STAND:
            //Buttons
            case ACACIA_BUTTON:
            case BIRCH_BUTTON:
            case CRIMSON_BUTTON:
            case DARK_OAK_BUTTON:
            case JUNGLE_BUTTON:
            case OAK_BUTTON:
            case POLISHED_BLACKSTONE_BUTTON:
            case SPRUCE_BUTTON:
            case WARPED_BUTTON:
            case STONE_BUTTON:
            case CAKE:
            case CARTOGRAPHY_TABLE:
            case CHAIN_COMMAND_BLOCK:
            case CHEST:
            case CHEST_MINECART:
            case COMMAND_BLOCK:
            case COMMAND_BLOCK_MINECART:
            case COMPARATOR:
            case CRAFTING_TABLE:
            case DISPENSER:
            //Doors
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case CRIMSON_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case WARPED_DOOR:
            case IRON_DOOR:
            case DROPPER:
            case ENCHANTING_TABLE:
            case ENDER_CHEST:
            case MINECART:
            case NOTE_BLOCK:
            case FURNACE:
            case FURNACE_MINECART:
            case GRINDSTONE:
            case HOPPER:
            case HOPPER_MINECART:
            case ITEM_FRAME:
            case LECTERN:
            case LEVER:
            case LODESTONE:
            case LOOM:
            case TNT_MINECART:
            case REPEATER:
            case REPEATING_COMMAND_BLOCK:
            case RESPAWN_ANCHOR:
            case SMITHING_TABLE:
            case SMOKER:
            case STONECUTTER:
            //Trapdoors
            case ACACIA_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case WARPED_TRAPDOOR:
            case IRON_TRAPDOOR:
            case TRAPPED_CHEST:
            case LAVA_CAULDRON:
            case POWDER_SNOW_CAULDRON:
            case WATER_CAULDRON:
            case JUKEBOX:
            case GRASS:
            case TALL_GRASS:
                return true;
            default:
                return false;
        }*/
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

    public static boolean isArmor(Material type) {
        return getArmor().contains(type);
    }
}
