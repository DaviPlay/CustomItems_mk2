package davide.customitems.events.customEvents;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Credits:
 * https://github.com/KettleMC-Network/SimpleNoCropTrample/blob/master/src/net/kettlemc/nocroptrample/listener/TrampleListener.java
 */
public class TrampleListener implements Listener {

    public TrampleListener() {

    }

    @EventHandler
    public void onMobTrample(EntityInteractEvent event) {
        if (event.getEntity() instanceof Player)
            return;
        if (event.getBlock().getType() == Material.FARMLAND) {
            CropTrampleEvent cropTrampleEvent = new CropTrampleEvent(event.getEntity(), CropTrampleEvent.TrampleCause.MOB, event.getBlock());
            Bukkit.getPluginManager().callEvent(cropTrampleEvent);
            if (cropTrampleEvent.isCancelled())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTrample(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND) {
            CropTrampleEvent cropTrampleEvent = new CropTrampleEvent(event.getPlayer(), CropTrampleEvent.TrampleCause.PLAYER, event.getClickedBlock());
            Bukkit.getPluginManager().callEvent(cropTrampleEvent);
            if (cropTrampleEvent.isCancelled())
                event.setCancelled(true);
        }
    }
}
