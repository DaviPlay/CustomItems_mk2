package davide.customitems;

import davide.customitems.API.Cooldowns;
import davide.customitems.Events.ExplosiveWandEvents;
import davide.customitems.Events.StonkEvents;
import davide.customitems.GUIs.GUI;
import davide.customitems.GUIs.GUIEvents;
import davide.customitems.API.Glow;
import davide.customitems.ItemCreation.Item;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class CustomItems extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager plugin = getServer().getPluginManager();

        //Commands
        getCommand("givestick").setExecutor(this);
        getCommand("customitems").setExecutor(new GUI());
        registerGlow();

        //Cooldowns
        Cooldowns.setupCooldown();

        //Listeners
        plugin.registerEvents(new GUIEvents(), this);
        plugin.registerEvents(new StonkEvents(), this);
        plugin.registerEvents(new ExplosiveWandEvents(), this);
    }

    @Override
    public void onDisable() { }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey key = new NamespacedKey(this, getDescription().getName());

            Glow glow = new Glow(key);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (label.equalsIgnoreCase("givestick")) {
            Player player = (Player) sender;
            player.getInventory().addItem(Item.damageSword.getItemStack());
        }

        return false;
    }
}
