package davide.customitems.Events;

import davide.customitems.API.SpecialBlocks;
import davide.customitems.API.Cooldowns;
import davide.customitems.Lists.ItemList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class CocaineEvents implements Listener {
    private int t = 1;
    private final HashMap<UUID, Integer> timesUsedInCooldown = new HashMap<>();

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        final int usesMax = 5;
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock().getType())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemList.cocaine.getKey(), PersistentDataType.INTEGER)) return;

        if (Cooldowns.checkCooldown(player.getUniqueId(), ItemList.cocaine.getKey()))
            t++;
        else {
            Cooldowns.setCooldown(player.getUniqueId(), ItemList.cocaine.getKey(), ItemList.cocaine.getDelay());
            t = 1;
        }

        timesUsedInCooldown.put(player.getUniqueId(), t);

        switch (timesUsedInCooldown.get(player.getUniqueId())) {
            case usesMax:
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    PotionEffectType type = effect.getType();

                    player.removePotionEffect(type);
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 1));
                break;

            case usesMax + 1:
                player.sendMessage("Congratulations, you OD'd!");
                player.setHealth(0);
                e.setCancelled(true);
                break;

            default:
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 1));
                break;
        }

        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
    }
}
