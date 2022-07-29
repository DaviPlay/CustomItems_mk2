package davide.customitems.reforgeCreation;

import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ItemList;
import davide.customitems.lists.ReforgeList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReforgeAssigning implements Listener, CommandExecutor, TabCompleter {
    List<String> arguments = new ArrayList<>();

    public ReforgeAssigning() {
        for (Reforge reforge : ReforgeList.reforges)
            arguments.add(reforge.getName().toLowerCase(Locale.ROOT));
    }

    @EventHandler
    private void assignReforgeOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        Reforge reforge = Reforge.randomReforge();

        if (reforge.getType() != null && item.getSubType() != null) {
            if (reforge.getType() == item.getType() || reforge.getType() == item.getSubType().getType()) {
                Reforge.setReforge(reforge, is);
            }
        }
        else if (reforge.getSubType() != null) {
            if (reforge.getSubType() == item.getSubType()) {
                Reforge.setReforge(reforge, is);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setReforge")) {
            if (getReforge(args[0]) == null) {
                player.sendMessage("§cThat reforge doesn't exist!");
                return true;
            }

            Reforge.setReforge(getReforge(args[0]), player.getInventory().getItemInMainHand());
        }

        return false;
    }

    private Reforge getReforge(String name) {
        Reforge reforge = null;

        for (Reforge r : ReforgeList.reforges)
            if (r.getName().equalsIgnoreCase(name))
                reforge = r;

        return reforge;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return args.length == 1 ? arguments.stream().filter(reforge -> reforge.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()) : arguments;
    }
}
