package davide.customitems.ItemCreation;

import org.bukkit.ChatColor;

public class Rarity {
    public enum ItemRarity {
        COMMON(ChatColor.WHITE),
        UNCOMMON(ChatColor.GREEN),
        RARE(ChatColor.BLUE),
        EPIC(ChatColor.DARK_PURPLE),
        LEGENDARY(ChatColor.GOLD),
        MYTHIC(ChatColor.LIGHT_PURPLE),
        SUPREME(ChatColor.RED),
        TEST(ChatColor.DARK_RED);

        private final ChatColor color;

        ItemRarity(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
    }
}
