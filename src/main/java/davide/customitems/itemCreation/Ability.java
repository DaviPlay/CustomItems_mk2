package davide.customitems.itemCreation;

import davide.customitems.CustomItems;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record Ability(AbilityType type, String name, int cooldown, boolean showDelay, NamespacedKey key, String... description) {
        private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

        public Ability {
                key = new NamespacedKey(plugin, normalizeKey(name));
        }

        public Ability(AbilityType type, String name, int cooldown, boolean showDelay, String... description) {
                this(type, name, cooldown, showDelay, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        public Ability(AbilityType type, String name, int cooldown, String... description) {
                this(type, name, cooldown, true, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        public Ability(AbilityType type, String name, String... description) {
                this(type, name, 0, false, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        @NotNull
        private static String normalizeKey(@NotNull String key) {
                return key.toLowerCase(Locale.ROOT)
                        .replace("!", "")
                        .replace("\'", "")
                        .replace(",", "")
                        .replace(" ", "_");
        }
}
