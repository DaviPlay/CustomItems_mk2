package davide.customitems.itemCreation;

import davide.customitems.CustomItems;
import davide.customitems.gui.itemCreationGUIs.Events;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;

public record Ability(Events event, AbilityType type, String name, int cooldown, boolean showDelay, NamespacedKey key, String... description) {
        private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

        public Ability {
                key = new NamespacedKey(plugin, normalizeKey(name));
        }

        public Ability(Events event, AbilityType type, String name, int cooldown, String... description) {
                this(event, type, name, cooldown, true, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        public Ability(AbilityType type, String name, int cooldown, boolean showDelay, String... description) {
                this(null, type, name, cooldown, showDelay, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        public Ability(AbilityType type, String name, int cooldown, String... description) {
                this(null, type, name, cooldown, true, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        public Ability(AbilityType type, String name, String... description) {
                this(null, type, name, 0, false, new NamespacedKey(plugin, normalizeKey(name)), description);
        }

        @NotNull
        private static String normalizeKey(@NotNull String key) {
                return key.toLowerCase(Locale.ROOT)
                        .replace("!", "")
                        .replace("\'", "")
                        .replace(",", "")
                        .replace(" ", "_");
        }

        @Override
        public String toString() {
                return "Ability{" +
                        "type=" + type +
                        ", name='" + name + '\'' +
                        ", cooldown=" + cooldown +
                        ", showDelay=" + showDelay +
                        ", key=" + key +
                        ", description=" + Arrays.toString(description) +
                        '}';
        }
}
