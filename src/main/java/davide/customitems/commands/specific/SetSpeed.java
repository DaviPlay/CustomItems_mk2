package davide.customitems.commands.specific;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpeed implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setSpeed"))
            try {
                player.setWalkSpeed(Float.parseFloat(args[0]));
            } catch (Exception e) {
                player.sendMessage("§cChoose a number between 0 and 1");
            }

        return false;
    }
}
