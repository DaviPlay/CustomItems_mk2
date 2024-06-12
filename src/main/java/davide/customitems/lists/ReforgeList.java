package davide.customitems.lists;

import davide.customitems.itemCreation.Type;
import davide.customitems.reforgeCreation.Reforge;

import java.util.ArrayList;
import java.util.List;

public class ReforgeList {

    public static List<Reforge> reforges = new ArrayList<>();

    //MELEE
    public static final Reforge sharp = new Reforge("Sharp", Type.MELEE, 5, 3, 5, 0, 0, 0);
    public static final Reforge serrated = new Reforge("Serrated", Type.MELEE, 1, 5, 0, 0.5f, 0, 0);
    public static final Reforge dull = new Reforge("Dull", Type.MELEE, 2, -2, 0, 0, 0, 0);
    public static final Reforge neat = new Reforge("Neat", Type.MELEE, 6, 3, 0, 0,0, 0);

    //RANGED
    public static final Reforge fast = new Reforge("Fast", Type.RANGED, 5, 3, 0, 0, 0, 0);
    public static final Reforge unreal = new Reforge("Unreal", Type.RANGED, 2, 3, 0, 0.5f, 0, 0);
    public static final Reforge lucky = new Reforge("Lucky", Type.RANGED, 3, 0, 10, 0, 0, 0);

    //ARMOR
    public static final Reforge refreshing = new Reforge("Refreshing", Type.ARMOR, 3, 0, 0, 0, 2, 0);
    public static final Reforge Healthy = new Reforge("Healthy", Type.ARMOR, 1, 0, 0, 0, 4, 0);
    public static final Reforge spikey = new Reforge("Spikey", Type.ARMOR, 3, 3, 3, 0, 0, 0);
    public static final Reforge plated = new Reforge("Plated", Type.ARMOR, 5, 0, 0, 0, 0, 2);
}
