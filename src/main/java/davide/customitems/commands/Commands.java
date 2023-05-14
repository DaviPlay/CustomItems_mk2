package davide.customitems.commands;

import davide.customitems.CustomItems;
import davide.customitems.commands.specific.GetStats;
import davide.customitems.commands.specific.GiveItem;
import davide.customitems.commands.specific.SetSpeed;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.gui.MatsGUI;
import davide.customitems.gui.itemCreation.CreateItemGUI;
import davide.customitems.playerStats.HealthManager;
import davide.customitems.reforgeCreation.ReforgeAssigning;

public class Commands {

    public Commands(CustomItems plugin) {
        plugin.getCommand("customitems").setExecutor(new ItemsGUI());
        plugin.getCommand("custommaterials").setExecutor(new MatsGUI());
        plugin.getCommand("setHealthMax").setExecutor(new HealthManager());
        plugin.getCommand("setSpeed").setExecutor(new SetSpeed());
        plugin.getCommand("setReforge").setExecutor(new ReforgeAssigning());
        plugin.getCommand("giveItem").setExecutor(new GiveItem());
        plugin.getCommand("getStats").setExecutor(new GetStats());
        plugin.getCommand("createItem").setExecutor(new CreateItemGUI());
        plugin.getCommand("viewRecipe").setExecutor(new GUIEvents());
    }
}
