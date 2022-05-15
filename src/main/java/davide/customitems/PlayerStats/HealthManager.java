package davide.customitems.PlayerStats;

import davide.customitems.API.ArmorEquipEvent;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthManager implements Listener {

    @EventHandler
    private void onWearArmor(ArmorEquipEvent e) {
        if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR) {
            System.out.println(e.getEquipType());
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPlayedBefore()) return;

        player.setHealthScale(10);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }
}
