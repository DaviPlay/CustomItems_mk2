package davide.customitems.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class DelayedTask implements Listener {
    private static Plugin plugin;
    private int id = -1;

    public DelayedTask(Plugin plugin) {
        DelayedTask.plugin = plugin;
    }

    public DelayedTask(Runnable runnable) {
        this(runnable, 0);
    }

    public DelayedTask(Runnable runnable, long delay) {
        if (plugin.isEnabled())
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
        else
            runnable.run();
    }

    public DelayedTask(Runnable runnable, long delay, long period) {
        if (plugin.isEnabled())
            id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, period);
        else
            runnable.run();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    public int getId() {
        return id;
    }
}
