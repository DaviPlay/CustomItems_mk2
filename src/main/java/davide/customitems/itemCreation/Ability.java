package davide.customitems.itemCreation;

import davide.customitems.CustomItems;
import davide.customitems.api.Instruction;
import davide.customitems.api.UUIDDataType;
import davide.customitems.api.Utils;
import davide.customitems.lists.EventList;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Ability(EventList event, Instruction instruction, AbilityType type, String name, int cooldown, boolean showDelay, NamespacedKey key, String... description) {
        private static final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);

        public Ability {
                key = new NamespacedKey(plugin, Utils.normalizeKey(UUID.randomUUID().toString()));
        }

        public Ability(EventList event, AbilityType type, String name, int cooldown, boolean showDelay, String... description) {
                this(event, null, type, name, cooldown, showDelay, new NamespacedKey(plugin, UUID.randomUUID().toString()), description);
        }

        public Ability(Instruction instruction, AbilityType type, String name, int cooldown, boolean showDelay, String... description) {
                this(null, instruction, type, name, cooldown, showDelay, new NamespacedKey(plugin, UUID.randomUUID().toString()), description);
        }

        // for loading user items
        public Ability(EventList event, AbilityType type, String name, int cooldown, List<String> description) {
                this(event, null, type, name, cooldown, true, new NamespacedKey(plugin, UUID.randomUUID().toString()), description.toArray(new String[]{}));
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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Ability ability)) return false;
            return Objects.equals(key, ability.key);
        }

        @Override
        public int hashCode() {
                return Objects.hashCode(key);
        }
}
