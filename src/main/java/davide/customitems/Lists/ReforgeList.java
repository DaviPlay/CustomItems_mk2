package davide.customitems.Lists;

import davide.customitems.ItemCreation.Type;
import davide.customitems.ReforgeCreation.Reforge;

public class ReforgeList {

    //Weapon
    public static final Reforge sharp = new Reforge("Sharp", Type.WEAPON, 15, 5, 0, 5);
    public static final Reforge dull = new Reforge("Dull", Type.WEAPON, 5, -5, 0, 0);
    public static final Reforge neat = new Reforge("Neat", Type.WEAPON, 10, 3, 0 ,0);

    //Armor
    public static final Reforge plated = new Reforge("Plated", Type.ARMOR, 10, 0, 2, 0);
    public static final Reforge spikey = new Reforge("Spikey", Type.ARMOR, 20, 3, 0, 3);

    public static Reforge[] reforges = {sharp, dull, neat, plated, spikey};
}
