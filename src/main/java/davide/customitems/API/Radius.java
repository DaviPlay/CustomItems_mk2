package davide.customitems.API;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class Radius {

    public static ArrayList<Block> getBlocksInRadius(Block start, Vector offset){
        ArrayList<Block> blocks = new ArrayList<>();

        for(double x = start.getLocation().getX() - offset.getX(); x <= start.getLocation().getX() + offset.getX(); x++)
            for(double y = start.getLocation().getY() - offset.getY(); y <= start.getLocation().getY() + offset.getY(); y++)
                for(double z = start.getLocation().getZ() - offset.getZ(); z <= start.getLocation().getZ() + offset.getZ(); z++) {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }

        return blocks;
    }
}
