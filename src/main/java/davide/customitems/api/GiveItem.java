package davide.customitems.api;

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
import java.util.Locale;
import java.util.stream.Collectors;

public class GiveItem implements CommandExecutor, TabCompleter {
    List<String> playerNames = new ArrayList<>();
    List<String> arguments = new ArrayList<>();

    public GiveItem() {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            playerNames.add(player.getName());

        for (Item[] items : ItemList.items)
            for (Item item : items)
                arguments.add(item.getKey().getKey());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        Item item = Item.toItem(args[1]);

        if (cmd.getName().equalsIgnoreCase("giveItem")) {
            if (item == null) {
                player.sendMessage("§cThat item doesn't exist!");
                return true;
            }

            ItemStack is = item.getItemStack();

            if (item.hasRandomUUID())
                Item.setRandomUUID(is);

            if (target != null) {
                if (target.getInventory().firstEmpty() == -1)
                    target.getWorld().dropItemNaturally(target.getLocation(), is);
                else
                    target.getInventory().addItem(is);
            } else {
                if (player.getInventory().firstEmpty() == -1)
                    player.getWorld().dropItemNaturally(player.getLocation(), is);
                else
                    player.getInventory().addItem(is);
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 2 ? arguments.stream().filter(item -> item.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList()) : playerNames;
    }
}