package davide.customitems.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public abstract class Instruction implements IInstruction {

    @Override
    public void run() {

    }

    public void run(ItemStack is) {

    }

    public void run(Entity entity) {

    }

    public void run(Block block ) {

    }
}
