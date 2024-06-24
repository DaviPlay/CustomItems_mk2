package davide.customitems.commands.specific;

import davide.customitems.api.Utils;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GiveItem implements CommandExecutor, TabCompleter {
    private final List<String> arguments = new ArrayList<>();

    public GiveItem() {
        for (List<Item> items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("customitems.give")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        Item item;
        Player target = player;

        if (cmd.getName().equalsIgnoreCase("giveItem")) {
            if (args.length == 1) {
                item = Item.toItem(args[0]);
            } else {
                target = Bukkit.getPlayer(args[0]);
                item = Item.toItem(args[1]);
            }
            
            if (item == null || ItemList.utilsItems.contains(item)) {
                player.sendMessage("§cThat item doesn't exist!");
                return true;
            }

            ItemStack is = item.getItemStack();

            if (item.hasRandomUUID())
                Item.setRandomUUID(is);

            Utils.addToInventory(Objects.requireNonNullElse(target, player), is);
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return arguments.stream().filter(item -> item.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }
}
