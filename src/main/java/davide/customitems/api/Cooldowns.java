package davide.customitems.api;

import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.UUID;

public class Cooldowns {
    private static HashMap<UUID, HashMap<NamespacedKey, Double>> mapCooldowns;

    public static void setupCooldown() {
        mapCooldowns = new HashMap<>();
    }

    public static void setCooldown(UUID uuid, NamespacedKey key, int seconds) {
        if (!mapCooldowns.containsKey(uuid))
            mapCooldowns.put(uuid, new HashMap<>());

        double delay = System.currentTimeMillis() + (seconds * 1000L);
        mapCooldowns.get(uuid).put(key, delay);
    }

    public static boolean checkCooldown(UUID uuid, NamespacedKey key) {
        if (!mapCooldowns.containsKey(uuid)) return false;
        HashMap<NamespacedKey, Double> cools = mapCooldowns.get(uuid);

        return cools.containsKey(key) && cools.get(key) >= System.currentTimeMillis();
    }

    public static short timeLeft(UUID uuid, NamespacedKey key) {
        if (!mapCooldowns.containsKey(uuid)) return 0;
        HashMap<NamespacedKey, Double> cools = mapCooldowns.get(uuid);

        return (short) Math.ceil((cools.get(key) - System.currentTimeMillis()) / 1000);
    }

    public static String inCooldownMessage(UUID uuid, NamespacedKey key) {
        if (timeLeft(uuid, key) < 60)
            return "§cThis ability is on cooldown for " + timeLeft(uuid, key) + " sec";
        else
            return "§cThis ability is on cooldown for " + timeLeft(uuid, key) + " min";
    }
}
