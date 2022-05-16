package davide.customitems.Events;

import davide.customitems.API.Cooldowns;
import davide.customitems.API.UUIDDataType;
import davide.customitems.Lists.ItemList;
import davide.customitems.API.SpecialBlocks;
import davide.customitems.CustomItems;
import davide.customitems.ItemCreation.Item;
import davide.customitems.ReforgeCreation.Reforge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Objects;
import java.util.UUID;

public class CaladbolgEvents implements Listener {

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.caladbolg.getKey(), new UUIDDataType())) return;

        UUID uuid = container.get(ItemList.caladbolg.getKey(), new UUIDDataType());

        if (Cooldowns.checkCooldown(uuid, ItemList.caladbolg.getKey())) {
            player.sendMessage(Cooldowns.inCooldownMessage(uuid, ItemList.caladbolg.getKey()));
            return;
        }

        Reforge reforge = Reforge.getReforge(is);

        is.setType(Material.NETHERITE_SWORD);

        if (reforge != null && reforge.getDamageModifier() > 0)
            Item.setDamageWithReforge((Item.getDamage(is) - reforge.getDamageModifier()) * 2, is, reforge.getDamageModifier() * 2);
        else
            Item.setDamage(Item.getDamage(is) * 2, is);

        Cooldowns.setCooldown(uuid, ItemList.caladbolg.getKey(), ItemList.caladbolg.getDelay());

        final short duration = 10;
        int delay = (ItemList.caladbolg.getDelay() - (ItemList.caladbolg.getDelay() - duration)) * 20;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CustomItems.getPlugin(CustomItems.class), () -> {
            Inventory inv = player.getInventory();
            ItemStack[] items = inv.getContents();

            for (ItemStack i : items)
                if (i != null) {
                    ItemMeta meta1 = i.getItemMeta();

                    if (meta1 != null) {
                        PersistentDataContainer container1 = meta1.getPersistentDataContainer();

                        if (Objects.equals(container1.get(ItemList.caladbolg.getKey(), new UUIDDataType()), uuid)) {
                            Reforge r = Reforge.getReforge(i);

                            if (r != null && r.getDamageModifier() > 0)
                                Item.setDamageWithReforge(Item.getTemporaryDamage(i) / 2, i, r.getDamageModifier());
                            else
                                Item.setDamage(Item.getTemporaryDamage(i) / 2, i);

                            i.setType(Material.DIAMOND_SWORD);
                            break;
                        }
                    }
                }
        }, delay);
    }
}
