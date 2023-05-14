package davide.customitems.commands.specific;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetSpeed implements CommandExecutor, TabCompleter {
    private final List<String> playerNames = new ArrayList<>();

    public SetSpeed() {
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            playerNames.add(player.getName());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        Player target = player;
        float speed = 0;

        if (cmd.getName().equalsIgnoreCase("setSpeed")) {
            if (args.length > 1) {
                target = Bukkit.getPlayer(args[0]);
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
                assert target != null;
                target.setWalkSpeed(speed);
            } catch (Exception e) {
                target.sendMessage("§cChoose a number between 0 and 1");
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return playerNames;
    }
}
