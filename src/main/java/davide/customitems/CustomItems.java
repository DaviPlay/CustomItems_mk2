package davide.customitems;

import davide.customitems.API.*;
import davide.customitems.Crafting.CraftingAmounts;
import davide.customitems.Events.*;
import davide.customitems.GUIs.CraftingInventories;
import davide.customitems.GUIs.GUI;
import davide.customitems.GUIs.GUIEvents;
import davide.customitems.PlayerStats.DamageCalculation;
import davide.customitems.PlayerStats.HealthManager;
import davide.customitems.ReforgeCreation.ReforgeAssigning;
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

        //Commands
        getCommand("customitems").setExecutor(new GUI());
        getCommand("setHealth").setExecutor(new HealthManager());

        //Cooldowns
        Cooldowns.setupCooldown();
        CraftingInventories.setInvs();

        //Listeners
        plugin.registerEvents(new GUIEvents(), this);
        plugin.registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
        plugin.registerEvents(new HealthManager(), this);
        plugin.registerEvents(new CraftingAmounts(), this);
        plugin.registerEvents(new DamageCalculation(), this);
        plugin.registerEvents(new ReforgeAssigning(), this);
        plugin.registerEvents(new GeneralListeners(), this);
        plugin.registerEvents(new RecipeBookEvents(), this);
        plugin.registerEvents(new StonkEvents(), this);
        plugin.registerEvents(new ExplosiveWandEvents(), this);
        plugin.registerEvents(new UltimateBreadEvents(), this);
        plugin.registerEvents(new SoulBowEvents(), this);
        plugin.registerEvents(new CocaineEvents(), this);
        plugin.registerEvents(new AspectOfTheEndEvents(), this);
        plugin.registerEvents(new CaladbolgEvents(), this);
        plugin.registerEvents(new GrapplingHookEvents(), this);
        plugin.registerEvents(new HookShotEvents(), this);
        plugin.registerEvents(new FireTalismanEvents(), this);
        plugin.registerEvents(new SlimeBootsEvents(), this);
        plugin.registerEvents(new MidasStaffEvents(), this);
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
