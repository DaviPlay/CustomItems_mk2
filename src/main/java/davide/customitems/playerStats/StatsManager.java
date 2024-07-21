package davide.customitems.playerStats;

import davide.customitems.CustomItems;
import davide.customitems.api.DelayedTask;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class StatsManager implements Listener, CommandExecutor, TabCompleter {
    private final List<String> playerNames = new ArrayList<>();

    private final CustomItems plugin = CustomItems.getPlugin(CustomItems.class);
    public StatsManager() {
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
        float speed = Item.getSpeed(is);

        if (health != 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
            player.setHealth(player.getHealth() + health);
        }
        if (speed != 0)
            player.setWalkSpeed(BigDecimal.valueOf(player.getWalkSpeed() + speed).setScale(3, RoundingMode.HALF_UP).floatValue());
    }

    @EventHandler
    private void onUnequipArmor(ArmorEquipEvent e) {
        if (e.getOldArmorPiece() == null || e.getOldArmorPiece().getType() == Material.AIR) return;
        Player player = e.getPlayer();
        ItemStack is = e.getOldArmorPiece();
        if (!Item.isCustomItem(is)) return;
        int health = Item.getHealth(is);
        float speed = Item.getSpeed(is);

        if (health != 0) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);
            player.setHealth(Math.max(player.getHealth() - health, 1));
        }
        if (speed != 0)
            player.setWalkSpeed(BigDecimal.valueOf(player.getWalkSpeed() - speed).setScale(3, RoundingMode.HALF_EVEN).floatValue());
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
        float currentSpeed = player.getWalkSpeed();
        float newSpeed = Item.getSpeed(newIs);
        float oldSpeed = Item.getSpeed(oldIs);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((currentHealth - oldHealth) + newHealth);
        player.setHealth(Math.max((player.getHealth() - oldHealth) + newHealth, 1));

        player.setWalkSpeed(Math.min(BigDecimal.valueOf((currentSpeed - oldSpeed) + newSpeed).setScale(3, RoundingMode.HALF_EVEN).floatValue(), 1));
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
        float currentSpeed = player.getWalkSpeed();
        float newSpeed = Item.getSpeed(putDown);
        float oldSpeed = Item.getSpeed(pickUp);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((currentHealth - oldHealth) + newHealth);
        player.setHealth(Math.max((player.getHealth() - oldHealth) + newHealth, 1));

        player.setWalkSpeed(Math.min(BigDecimal.valueOf((currentSpeed - oldSpeed) + newSpeed).setScale(3, RoundingMode.HALF_EVEN).floatValue(), 1));
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        ItemStack is = e.getItemDrop().getItemStack();
        if (SpecialBlocks.isArmor(is.getType())) return;
        if (!Item.isCustomItem(is)) return;
        int health = Item.getHealth(is);
        float speed = Item.getSpeed(is);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);
        player.setHealth(Math.max(player.getHealth() - health, 1));

        player.setWalkSpeed(Math.max(BigDecimal.valueOf(player.getWalkSpeed() - speed).setScale(3, RoundingMode.HALF_EVEN).floatValue(), 0));
    }

    @EventHandler
    private void onPickUpItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        ItemStack is = e.getItem().getItemStack();
        if (SpecialBlocks.isArmor(is.getType())) return;
        if (!Item.isCustomItem(is)) return;
        new DelayedTask(() -> {
            Player p = (Player) e.getEntity();
            if (!is.equals(p.getInventory().getItemInMainHand())) return;
            int health = Item.getHealth(is);
            float speed = Item.getSpeed(is);

            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
            player.setHealth(Math.max(player.getHealth() + health, 1));

            player.setWalkSpeed(Math.min(BigDecimal.valueOf(player.getWalkSpeed() + speed).setScale(3, RoundingMode.HALF_EVEN).floatValue(), 1));
        });
    }

    @EventHandler
    private void onFirstJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPlayedBefore()) return;

        int healthOnJoin;
        try {
            healthOnJoin = plugin.getConfig().getInt("health_on_join");
        } catch (NullPointerException ex) {
            healthOnJoin = 20;
        }

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(healthOnJoin);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        player.setWalkSpeed(0.2f);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        Player target = player;

        if (cmd.getName().equalsIgnoreCase("setHealthMax")) {
            int health = 0;

            if (args.length == 0) {
                player.sendMessage("§cSpecify a number!");
                return true;
            }
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    player.sendMessage("§cInsert a valid player");
                    return true;
                }
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

            target.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
            target.setHealth(health);
        }
        if (cmd.getName().equalsIgnoreCase("setSpeed")) {
            float speed = 0;
            if (args.length == 0) {
                player.sendMessage("§cSpecify a number!");
                return true;
            }

            if (args.length > 1) {
                target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    player.sendMessage("§cInsert a valid player");
                    return true;
                }
                try {
                    speed = Float.parseFloat(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInsert a number!");
                }
            } else {
                try {
                    speed = Float.parseFloat(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInsert a number!");
                }
            }

            try {
                target.setWalkSpeed(speed);
            } catch (Exception e) {
                target.sendMessage("§cChoose a number between 0 and 1");
            }
        }
        if (cmd.getName().equalsIgnoreCase("getSpeed")) {
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§cInsert a valid player");
                    return true;
                }
            }

            target.sendMessage(String.valueOf(target.getWalkSpeed()));
        }


        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? playerNames : null;
    }
}
