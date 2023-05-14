package davide.customitems.api;

import org.bukkit.Material;
import org.bukkit.block.Block;

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

    public static boolean isDye(Material type) {
        return switch (type) {
            case BLACK_DYE, BLUE_DYE, BROWN_DYE, CYAN_DYE, GRAY_DYE, GREEN_DYE, LIME_DYE, LIGHT_BLUE_DYE, MAGENTA_DYE, ORANGE_DYE, PINK_DYE, LIGHT_GRAY_DYE, PURPLE_DYE, RED_DYE, WHITE_DYE, YELLOW_DYE ->
                    true;
            default -> false;
        };
    }

    public static boolean isOre(Material type) {
        return switch (type) {
            case COAL_ORE, COPPER_ORE, DEEPSLATE_COAL_ORE, DEEPSLATE_COPPER_ORE, DEEPSLATE_DIAMOND_ORE, DEEPSLATE_EMERALD_ORE, DEEPSLATE_IRON_ORE, DEEPSLATE_GOLD_ORE, DIAMOND_ORE, EMERALD_ORE, GOLD_ORE,
                    IRON_ORE, LAPIS_ORE, DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE, REDSTONE_ORE, NETHER_GOLD_ORE, NETHER_QUARTZ_ORE ->
                    true;
            default -> false;
        };
    }
}
