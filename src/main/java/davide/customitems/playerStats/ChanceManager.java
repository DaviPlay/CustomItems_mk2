package davide.customitems.playerStats;

import davide.customitems.api.IInstruction;
import davide.customitems.api.Instruction;
import davide.customitems.api.Utils;
import davide.customitems.lists.ItemList;
import org.bukkit.entity.Player;

import java.util.Random;

public class ChanceManager {

    public static double chanceCalculation(double chance, Instruction instruction, Player player) {
        double r = new Random().nextDouble(100);
        chance = Math.min(chance, 100);
        chance = Math.max(0, chance);
        boolean purity = Utils.hasCustomItemInInv(ItemList.purity, player.getInventory());
        boolean foot = Utils.hasCustomItemInInv(ItemList.rabbitFoot, player.getInventory());

        //System.out.println(chance);
        //System.out.println(r);

        if (r <= chance) {
            if ((purity && !foot))
                if (!tryAgainBitch(chance))
                    return r;

            instruction.run();
        }
        else if ((!purity && foot)) {
            if (tryAgainBitch(chance))
                instruction.run();
        }

        return r;
    }

    private static boolean tryAgainBitch(double chance) {
        double r = new Random().nextDouble(100);
        //System.out.println(r);

        return r <= chance;
    }
}
