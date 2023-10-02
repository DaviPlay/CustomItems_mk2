package davide.customitems.itemCreation;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Rarity {
        COMMON(ChatColor.WHITE, Material.WHITE_DYE),
        UNCOMMON(ChatColor.GREEN, Material.LIME_DYE),
        RARE(ChatColor.BLUE, Material.BLUE_DYE),
        EPIC(ChatColor.DARK_PURPLE, Material.PURPLE_DYE),
        LEGENDARY(ChatColor.GOLD, Material.ORANGE_DYE),
        MYTHIC(ChatColor.LIGHT_PURPLE, Material.MAGENTA_DYE),
        DIVINE(ChatColor.AQUA, Material.LIGHT_BLUE_DYE),
        SUPREME(ChatColor.RED, Material.RED_DYE),
        TEST(ChatColor.DARK_RED, Material.BLACK_DYE);

        private final ChatColor color;
        private final Material dye;

        Rarity(ChatColor color, Material dye) {
            this.color = color;
            this.dye = dye;
        }

        public ChatColor getColor() {
            return color;
        }

        public Material getDye() {
            return dye;
        }
}
