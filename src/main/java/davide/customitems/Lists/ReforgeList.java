package davide.customitems.Lists;

import davide.customitems.API.Type;
import davide.customitems.ReforgeCreation.Reforge;

public class ReforgeList {

    public static final Reforge sharp = new Reforge("Sharp", Type.WEAPON, 15, +5);
    public static final Reforge dull = new Reforge("Dull", Type.WEAPON, 5, -5);
    public static final Reforge neat = new Reforge("Neat", Type.WEAPON, 10, +3);

    public static Reforge[] reforges = {sharp, dull, neat};
}
