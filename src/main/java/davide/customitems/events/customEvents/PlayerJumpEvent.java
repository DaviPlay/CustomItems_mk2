package davide.customitems.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Credits: <p>
 * <a href="https://www.spigotmc.org/threads/accurate-playerjumpevent-do-actions-when-players-jump.138946/">Accurate Player Jump Event</a>
 */
public class PlayerJumpEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static final PlayerJumpEventListener listener = new PlayerJumpEventListener();

    private final Player player;

    public PlayerJumpEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private static class PlayerJumpEventListener implements Listener {

        private final Map<UUID, Integer> jumps = new HashMap<>();

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerJoin(PlayerJoinEvent e) {
            jumps.put(e.getPlayer().getUniqueId(), e.getPlayer().getStatistic(Statistic.JUMP));
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent e) {
            jumps.remove(e.getPlayer().getUniqueId());
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerMove(PlayerMoveEvent e) {
            Player player = e.getPlayer();

            if(e.getFrom().getY() < Objects.requireNonNull(e.getTo()).getY()) {
                int current = player.getStatistic(Statistic.JUMP);
                int last = jumps.getOrDefault(player.getUniqueId(), -1);

                if(last != current) {
                    jumps.put(player.getUniqueId(), current);

                    double yDif = (long) ((e.getTo().getY() - e.getFrom().getY()) * 1000) / 1000d;

                    if((yDif < 0.035 || yDif > 0.037) && (yDif < 0.116 || yDif > 0.118)) {
                        Bukkit.getPluginManager().callEvent(new PlayerJumpEvent(player));
                    }
                }
            }
        }
    }

    public static void register(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
