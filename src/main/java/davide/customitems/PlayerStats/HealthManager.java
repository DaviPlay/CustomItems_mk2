package davide.customitems.PlayerStats;

import davide.customitems.API.ArmorEquipEvent;
import davide.customitems.ItemCreation.Item;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class HealthManager implements Listener, CommandExecutor {

    @EventHandler
    private void onEquipArmor(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack item = e.getNewArmorPiece();
        if (Item.getHealth(item) == 0) return;

        int health = Item.getHealth(item);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
        player.setHealth(player.getHealth() + health);
    }

    @EventHandler
    private void onUnequipArmor(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null || e.getOldArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack item = e.getOldArmorPiece();
        if (Item.getHealth(item) == 0) return;

        int health = Item.getHealth(item);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);

        double healthToSub = Math.max(player.getHealth() - health, 1);
        player.setHealth(healthToSub);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPlayedBefore()) return;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setHealthMax")) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Integer.parseInt(args[0]));
            player.setHealth(Integer.parseInt(args[0]));
        }

        return false;
    }
}
