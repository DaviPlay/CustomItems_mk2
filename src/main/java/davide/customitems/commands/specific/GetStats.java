package davide.customitems.commands.specific;

import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GetStats implements CommandExecutor, TabCompleter {
    private final List<String> arguments = new ArrayList<>();

    public GetStats() {
        for (List<Item> items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (cmd.getName().equalsIgnoreCase("getStats")) {
            ItemStack is = player.getInventory().getItemInMainHand();
            ItemMeta meta = is.getItemMeta();
            if (meta == null) return true;
            Item item = Item.toItem(Objects.requireNonNull(is));

            if (item == null || ItemList.utilsItems.contains(item)) {
                player.sendMessage("§cThat item doesn't exist!");
                return true;
            }

            Reforge reforge = Reforge.getReforge(is);
            if (reforge != null) {
                player.sendMessage("§r" + meta.getDisplayName() + " Stats {" +
                        "Damage=" + Item.getDamage(is) + " (+" + (int) (reforge.getDamageModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2)) + ")" +
                        ", Crit Chance=" + Item.getCritChance(is) + "% (+" + (int) (reforge.getCritChanceModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2)) + "%)" +
                        ", Health=" + Item.getHealth(is) + " (+" + (int) (reforge.getHealthModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2)) + ")" +
                        ", Defence=" + Item.getDefence(is) + " (+" + (int) (reforge.getDefenceModifier() * (((float) Item.getRarity(is).ordinal() + 1) / 2)) + ")" +
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return arguments.stream().filter(item -> item.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }
}
