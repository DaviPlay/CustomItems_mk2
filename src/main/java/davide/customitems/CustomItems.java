package davide.customitems;

import davide.customitems.api.*;
import davide.customitems.commands.Commands;
import davide.customitems.crafting.CraftingAmounts;
import davide.customitems.events.*;
import davide.customitems.events.customEvents.ArmorListener;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.events.customEvents.TrampleListener;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.events.GUIEvents;
import davide.customitems.playerStats.DamageManager;
import davide.customitems.playerStats.HealthManager;
import davide.customitems.reforgeCreation.ReforgeAssigning;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class CustomItems extends JavaPlugin {
    private static SignMenuFactory signMenuFactory;

    @Override
    public void onEnable() {
        PluginManager plugin = getServer().getPluginManager();

        //Others
        new DelayedTask(this);
        new CraftingAmounts(this);
        registerGlow();
        signMenuFactory = new SignMenuFactory(this);

        //Inventories
        new CraftingInventories();

        //Commands
        new Commands(this);

        //Cooldowns
        Cooldowns.setupCooldown();

        //Listeners
        new EventListener(this);
        new GeneralListeners(this);
        PlayerJumpEvent.register(this);
        plugin.registerEvents(new ArmorListener(), this);
        plugin.registerEvents(new TrampleListener(), this);
        plugin.registerEvents(new DamageManager(), this);
        plugin.registerEvents(new HealthManager(), this);
        plugin.registerEvents(new ReforgeAssigning(), this);
        plugin.registerEvents(new GUIEvents(), this);
    }

    @Override
    public void onDisable() {

    }

    public static SignMenuFactory getSignMenuFactory() {
        return signMenuFactory;
    }

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
