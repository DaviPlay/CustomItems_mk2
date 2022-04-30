package davide.customitems.Events;

import davide.customitems.API.ItemList;
import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.UUIDDataType;
import davide.customitems.ItemCreation.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StonkEvents implements Listener {
    private static int b = 0;
    private static final HashMap<UUID, Integer> blocksMined = new HashMap<>();
    private static final int blocksRemaining = 250;

    @EventHandler
    private void onRightClick(BlockBreakEvent e) {
        if (e.getBlock().isPassable()) return;
        if (SpecialBlocks.isClickableBlock(e.getBlock().getType())) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.stonk.getKey(), new UUIDDataType())) return;
        assert item != null;
        List<String> lore = item.getLore();

        b++;
        blocksMined.put(container.get(item.getKey(), new UUIDDataType()), b);

        if (blocksMined.get(container.get(item.getKey(), new UUIDDataType())) == blocksRemaining) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 4));

            b = 0;
        }

        int index = -1;
        for (String line : lore)
            if (line.contains("§e"))
                index = lore.indexOf(line);

        System.out.println(blocksMined.keySet());
        System.out.println(Item.getRandomUUID(is));
        lore.set(index, "§e" + StonkEvents.getBlocksRemaining(is) + " §8blocks remaining");
        item.setLore(lore, is);
    }

    public static int getBlocksRemaining(ItemStack is) {
        Item item = Item.toItem(is);
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        assert item != null;

        return blocksRemaining - blocksMined.get(container.get(item.getKey(), new UUIDDataType()));
    }

    public static int getBlocksMax() {
        return blocksRemaining;
    }
}
