package davide.customitems.commands;

import davide.customitems.CustomItems;
import davide.customitems.commands.specific.GetStats;
import davide.customitems.commands.specific.GiveItem;
import davide.customitems.events.GUIEvents;
import davide.customitems.gui.ItemsGUI;
import davide.customitems.gui.MatsGUI;
import davide.customitems.gui.itemCreationGUIs.CreateItemGUI;
import davide.customitems.gui.itemCreationGUIs.StatsGUI;
import davide.customitems.playerStats.StatsManager;
import davide.customitems.reforgeCreation.ReforgeAssigning;

public class Commands {

    public Commands(CustomItems plugin) {
        plugin.getCommand("customitems").setExecutor(new ItemsGUI());
        plugin.getCommand("custommaterials").setExecutor(new MatsGUI());
        plugin.getCommand("setHealthMax").setExecutor(new StatsManager());
        plugin.getCommand("setSpeed").setExecutor(new StatsManager());
        plugin.getCommand("getSpeed").setExecutor(new StatsManager());
        plugin.getCommand("setReforge").setExecutor(new ReforgeAssigning());
        plugin.getCommand("giveItem").setExecutor(new GiveItem());
        plugin.getCommand("getStats").setExecutor(new GetStats());
        plugin.getCommand("createItem").setExecutor(new CreateItemGUI());
        plugin.getCommand("viewRecipe").setExecutor(new GUIEvents());
    }
}
