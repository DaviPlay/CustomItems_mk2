package davide.customitems.playerStats;

import davide.customitems.CustomItems;
import davide.customitems.api.SpecialBlocks;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.itemCreation.Item;
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
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
        ItemStack is = e.getNewArmorPiece();
        if (!Item.isCustomItem(is)) return;
        int health = Item.getHealth(is);
        if (health == 0) return;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
        player.setHealth(player.getHealth() + health);
    }

    @EventHandler
    private void onUnequipArmor(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null || e.getOldArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack is = e.getOldArmorPiece();
        if (!Item.isCustomItem(is)) return;
        int health = Item.getHealth(is);
        if (health == 0) return;

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);
        player.setHealth(Math.max(player.getHealth() - health, 1));
    }

    @EventHandler
    private void onHotbarSwap(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack newIs = player.getInventory().getItem(e.getNewSlot());
        ItemStack oldIs = player.getInventory().getItem(e.getPreviousSlot());
        if (newIs != null && SpecialBlocks.isArmor(newIs.getType())) return;
        if (oldIs != null && SpecialBlocks.isArmor(oldIs.getType())) return;

        int currentHealth = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int newHealth = Item.getHealth(newIs);
        int oldHealth = Item.getHealth(oldIs);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((currentHealth - oldHealth) + newHealth);
        player.setHealth(Math.max((player.getHealth() - oldHealth) + newHealth, 1));
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != player.getInventory()) return;
        if (e.getSlot() != player.getInventory().getHeldItemSlot()) return;
        ItemStack putDown = e.getCursor();
        ItemStack pickUp = e.getCurrentItem();
        if (putDown != null && SpecialBlocks.isArmor(putDown.getType())) return;
        if (pickUp != null && SpecialBlocks.isArmor(pickUp.getType())) return;

        int currentHealth = (int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        int newHealth = Item.getHealth(putDown);
        int oldHealth = Item.getHealth(pickUp);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((currentHealth - oldHealth) + newHealth);
        player.setHealth(Math.max((player.getHealth() - oldHealth) + newHealth, 1));
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItemDrop().getItemStack();
        if (SpecialBlocks.isArmor(is.getType())) return;
        if (!Item.isCustomItem(is)) return;
        int health = Item.getHealth(is);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);
        player.setHealth(Math.max(player.getHealth() - health, 1));
    }

    @EventHandler
    private void onPickUpItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack is = e.getItem().getItemStack();
        if (SpecialBlocks.isArmor(is.getType())) return;
        if (!Item.isCustomItem(is)) return;
        Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> {
            Player p = (Player) e.getEntity();
            if (!is.equals(p.getInventory().getItemInMainHand())) return;
            int health = Item.getHealth(is);

            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
            player.setHealth(Math.max(player.getHealth() + health, 1));
        }, 1);
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

        if (!player.hasPermission("customitems.stats")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        Player target = player;
        int health = 0;

        if (args.length == 0) {
            player.sendMessage("§cSpecify a number!");
            return true;
        }

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
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        player.setHealth(health);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? playerNames : null;
    }
}
