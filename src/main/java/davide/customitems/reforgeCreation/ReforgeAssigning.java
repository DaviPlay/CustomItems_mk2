package davide.customitems.reforgeCreation;

import davide.customitems.api.Instruction;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.ReforgeList;
import davide.customitems.playerStats.ChanceManager;
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
    private final List<String> arguments = new ArrayList<>();

    public ReforgeAssigning() {
        for (Reforge reforge : ReforgeList.reforges)
            arguments.add(reforge.getName().toLowerCase(Locale.ROOT));

        arguments.add("random");
    }

    @EventHandler
    private void assignReforgeOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        if (Reforge.isReforged(is)) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        Reforge reforge = Reforge.randomReforge(item.getType());

        ChanceManager.chanceCalculation(10, new Instruction() {
            @Override
            public void run() {
                Reforge.setReforge(reforge, is);
            }
        }, (Player) e.getWhoClicked());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (!args[0].equals("random") && getReforge(args[0]) == null) {
            player.sendMessage("§cThat reforge doesn't exist!");
            return true;
        }
        if (args[0].equals("random"))
            Reforge.setReforge(Reforge.randomReforge(), player.getInventory().getItemInMainHand());
        else
            Reforge.setReforge(getReforge(args[0]), player.getInventory().getItemInMainHand());

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
