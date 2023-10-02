package davide.customitems.playerStats;

import davide.customitems.api.IInstruction;
import davide.customitems.api.Utils;
import davide.customitems.lists.ItemList;
import org.bukkit.entity.Player;

import java.util.Random;

public class ChanceManager {

    public static double chanceCalculation(double dropChance, IInstruction instruction, Player player) {
        double r = new Random().nextDouble(100);
        boolean purity = Utils.hasCustomItemInInv(ItemList.purity, player.getInventory());
        boolean foot = Utils.hasCustomItemInInv(ItemList.rabbitFoot, player.getInventory());
        //System.out.println(dropChance);

        //System.out.println(r);
        if (r <= dropChance) {
            if ((purity && !foot) && !tryAgainBitch(dropChance)) return r;
            instruction.run();
        }
        else if ((!purity && foot) && tryAgainBitch(dropChance)) {
            instruction.run();
        }

        return r;
    }

    private static boolean tryAgainBitch(double dropChance) {
        double r = new Random().nextDouble(100);

        //System.out.println(r);

        return r <= dropChance;
    }
}
