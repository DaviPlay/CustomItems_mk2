package davide.customitems.commands.specific;

import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GetStats implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (cmd.getName().equalsIgnoreCase("getStats")) {
            ItemStack is = player.getInventory().getItemInMainHand();
            ItemMeta meta = is.getItemMeta();
            if (meta == null) return true;
            Item item = Item.toItem(Objects.requireNonNull(is));

            if (item == null || ItemList.utilsItems.contains(item)) {
                player.sendMessage("§cYou're not holding a custom item!");
                return true;
            }

            Reforge reforge = Reforge.getReforge(is);
            if (reforge != null) {
                player.sendMessage("§r" + meta.getDisplayName() + " Stats {" +
                        "Damage=" + Item.getDamage(is) + " (+" + Reforge.getDamageModifier(is, reforge) + ")" +
                        ", Crit Chance=" + Item.getCritChance(is) + "% (+" + Reforge.getCritChanceModifier(is, reforge) + "%)" +
                        ", Health=" + Item.getHealth(is) + " (+" + Reforge.getHealthModifier(is, reforge) + ")" +
                        ", Defence=" + Item.getDefence(is) + " (+" + Reforge.getDefenceModifier(is, reforge) + ")" +
                        '}');
            } else {
                player.sendMessage("§r" + meta.getDisplayName() + " Stats{" +
                        "Damage=" + Item.getDamage(is) +
                        ", Crit Chance=" + Item.getCritChance(is) + "%" +
                        ", Health=" + Item.getHealth(is) +
                        ", Defence=" + Item.getDefence(is) +
                        '}');
            }
        }

        return false;
    }
}
