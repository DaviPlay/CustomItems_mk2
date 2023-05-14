package davide.customitems.playerStats;

import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.itemCreation.Item;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealthManager implements Listener, CommandExecutor, TabCompleter {
    private final List<String> playerNames = new ArrayList<>();

    public HealthManager() {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            playerNames.add(player.getName());
    }

    @EventHandler
    private void onEquipArmor(ArmorEquipEvent e) {
        if (e.getNewArmorPiece() == null || e.getNewArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack item = e.getNewArmorPiece();
        if (Item.getHealth(item) == 0) return;

        int health = Item.getHealth(item);
        Reforge reforge = Reforge.getReforge(item);
        if (reforge != null)
            health += reforge.getHealthModifier();
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
        Reforge reforge = Reforge.getReforge(item);
        if (reforge != null)
            health += reforge.getHealthModifier();
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
        if (!(sender instanceof Player player)) return true;
        Player target = player;
        int health = 0;

        if (cmd.getName().equalsIgnoreCase("setHealthMax")) {
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[0]);
                try {
                    health = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInsert a number!");
                }
            } else {
                try {
                    health = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInsert a number!");
                }
            }

            if (health < 1) {
                player.sendMessage("§cYou can't have less than 1 health!");
                return true;
            }

            assert target != null;
            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
            target.setHealth(health);
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? playerNames : null;
    }
}
