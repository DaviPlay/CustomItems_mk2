package davide.customitems;

import davide.customitems.api.*;
import davide.customitems.crafting.CraftingAmounts;
import davide.customitems.events.*;
import davide.customitems.events.customEvents.ArmorListener;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.events.customEvents.TrampleListener;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.events.GUIEvents;
import davide.customitems.playerStats.DamageCalculation;
import davide.customitems.playerStats.HealthManager;
import davide.customitems.reforgeCreation.ReforgeAssigning;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class CustomItems extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager plugin = getServer().getPluginManager();

        registerGlow();
        new SignMenuFactory(this);

        //GiveItem
        getCommand("customitems").setExecutor(new ItemsGUI());
        getCommand("setHealthMax").setExecutor(new HealthManager());
        getCommand("setReforge").setExecutor(new ReforgeAssigning());
        getCommand("giveItem").setExecutor(new GiveItem());
        getCommand("viewRecipe").setExecutor(new GUIEvents());

        //Cooldowns
        Cooldowns.setupCooldown();
        CraftingInventories.setInvs();

        //Listeners
        new EventListener(this);
        PlayerJumpEvent.register(this);
        plugin.registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
        plugin.registerEvents(new TrampleListener(), this);
        plugin.registerEvents(new DamageCalculation(), this);
        plugin.registerEvents(new HealthManager(), this);
        plugin.registerEvents(new GeneralListeners(), this);
        plugin.registerEvents(new ReforgeAssigning(), this);
        plugin.registerEvents(new CraftingAmounts(), this);
        plugin.registerEvents(new GUIEvents(), this);
    }

    @Override
    public void onDisable() {

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
