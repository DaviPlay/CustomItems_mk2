package davide.customitems.commands.specific;

import davide.customitems.CustomItems;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GetStats implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return true;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey scrollKey = new NamespacedKey(CustomItems.getPlugin(CustomItems.class), "undead_scroll_dmg");
        Item item = Item.toItem(Objects.requireNonNull(is));

        if (item == null || ItemList.utilsItems.contains(item)) {
            player.sendMessage("§cYou're not holding a custom item!");
            return true;
        }

        Reforge reforge = Reforge.getReforge(is);
        int scrollDmg;
        try {
            scrollDmg = container.get(scrollKey, PersistentDataType.INTEGER);
        } catch (NullPointerException ex) {
            scrollDmg = 0;
        }
        if (reforge == null) {
            player.sendMessage("§r" + meta.getDisplayName() + " Stats{" +
                    "Damage=" + Item.getDamage(is) + (scrollDmg > 0 ? " [+" + scrollDmg + "]" : "") +
                    ", Crit Chance=" + Item.getCritChance(is) + "%" +
                    ", Health=" + Item.getHealth(is) +
                    ", Defence=" + Item.getDefence(is) +
                    ", Speed=" + Item.getSpeed(is) +
                    ", Luck=" + Item.getLuck(is) +
                    '}');
        } else {
            player.sendMessage("§r" + meta.getDisplayName() + " Stats {" +
                    "Damage=" + Item.getDamage(is) + (scrollDmg > 0 ? " [+" + scrollDmg + "]" : "") + " (+" + Reforge.getDamageModifier(is, reforge) + ")" +
                    ", Crit Chance=" + Item.getCritChance(is) + "% (+" + Reforge.getCritChanceModifier(is, reforge) + "%)" +
                    ", Health=" + Item.getHealth(is) + " (+" + Reforge.getHealthModifier(is, reforge) + ")" +
                    ", Defence=" + Item.getDefence(is) + " (+" + Reforge.getDefenceModifier(is, reforge) + ")" +
                    ", Speed=" + Item.getSpeed(is) + " (+" + Reforge.getSpeedModifier(is, reforge) + ")" +
                    ", Luck=" + Item.getLuck(is) + " (+" + Reforge.getLuckModifier(is, reforge) + ")" +
                    '}');
        }

        return false;
    }
}
