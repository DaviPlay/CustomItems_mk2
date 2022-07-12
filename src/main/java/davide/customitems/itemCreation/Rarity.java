package davide.customitems.itemCreation;

import org.bukkit.ChatColor;

public enum Rarity {
        COMMON(ChatColor.WHITE),
        UNCOMMON(ChatColor.GREEN),
        RARE(ChatColor.BLUE),
        EPIC(ChatColor.DARK_PURPLE),
        LEGENDARY(ChatColor.GOLD),
        MYTHIC(ChatColor.LIGHT_PURPLE),
        SUPREME(ChatColor.RED),
        TEST(ChatColor.DARK_RED);

        private final ChatColor color;

        Rarity(ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return color;
        }
}
