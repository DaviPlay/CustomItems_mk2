package davide.customitems.lists;

import davide.customitems.itemCreation.Type;
import davide.customitems.reforgeCreation.Reforge;

import java.util.ArrayList;
import java.util.List;

public class ReforgeList {

    public static List<Reforge> reforges = new ArrayList<>();

    //MELEE
    public static final Reforge sharp = new Reforge("Sharp", Type.MELEE, 10, 5, 0, 5);
    public static final Reforge dull = new Reforge("Dull", Type.MELEE, 5, -5, 0, 0);
    public static final Reforge neat = new Reforge("Neat", Type.MELEE, 15, 3, 0 ,0);

    //RANGED
    public static final Reforge fast = new Reforge("Fast", Type.RANGED, 15, 3, 0, 0);
    public static final Reforge unreal = new Reforge("Unreal", Type.RANGED, 5, 5, 0, 10);

    //Armor
    public static final Reforge plated = new Reforge("Plated", Type.ARMOR, 10, 0, 2, 0);
    public static final Reforge spikey = new Reforge("Spikey", Type.ARMOR, 5, 3, 0, 3);
}
