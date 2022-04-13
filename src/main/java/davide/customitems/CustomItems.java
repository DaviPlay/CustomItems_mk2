package davide.customitems;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.CraftingAmounts;
import davide.customitems.Events.*;
import davide.customitems.GUIs.CraftingInventories;
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

        registerGlow();

        //Commands
        getCommand("customitems").setExecutor(new GUI());

        //Cooldowns
        Cooldowns.setupCooldown();
        CraftingInventories.setInvs();

        //Listeners
        plugin.registerEvents(new GUIEvents(), this);
        plugin.registerEvents(new CraftingAmounts(), this);
        plugin.registerEvents(new StonkEvents(), this);
        plugin.registerEvents(new ExplosiveWandEvents(), this);
        plugin.registerEvents(new UltimateBreadEvents(), this);
        plugin.registerEvents(new SoulBowEvents(), this);
        plugin.registerEvents(new CocaineEvents(), this);
        plugin.registerEvents(new AspectOfTheEndEvents(), this);
        plugin.registerEvents(new GrapplingHookEvents(), this);
        plugin.registerEvents(new FireTalismanEvents(), this);
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
}
