package davide.customitems.events;

import davide.customitems.CustomItems;
import davide.customitems.api.Instruction;
import davide.customitems.api.Utils;
import davide.customitems.events.customEvents.ArmorEquipEvent;
import davide.customitems.events.customEvents.CropTrampleEvent;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.Item;
import davide.customitems.lists.EventList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneralEvents implements Listener {
    private final CustomItems plugin;

    public GeneralEvents(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);
        if (item == null) return;

        if (item.getAbilities() == null) return;
        for (int i = 0; i < item.getAbilities().size(); i++) {
            Ability ability = item.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null) {
                instruction.run(e);
                continue;
            }

            EventList event = ability.event();
            if (event == null) continue;
            switch (event) {
                case TELEPORT -> Events.teleportPlayer(player, (int) event.getParam()[0]);
                case THROW -> Events.throwItem(player, is, (int) event.getParam()[0]);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void jump(PlayerJumpEvent e) {
        Player player = e.getPlayer();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void trample(CropTrampleEvent e) {
        if (!(e.getTrampler() instanceof Player player)) return;
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void sneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    private void blockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    private void armor(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        ItemStack newIs = null;
        ItemStack oldIs = null;
        if (e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR)
            newIs = e.getNewArmorPiece();
        if (e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR)
            oldIs = e.getOldArmorPiece();

        Item newItem = null;
        if (newIs != null)
            newItem = Item.toItem(newIs);

        if (newItem != null)
            if (newItem.getAbilities() != null)
                for (int i = 0; i < newItem.getAbilities().size(); i++)
                    if (!Utils.validateItem(newIs, player, i, e)) {
                        Ability ability = newItem.getAbilities().get(i);
                        Instruction instruction = ability.instruction();
                        if (instruction != null)
                            instruction.run(e);
                    }

        Item oldItem = null;
        if (oldIs != null)
            oldItem = Item.toItem(oldIs);

        if (oldItem != null)
            if (oldItem.getAbilities() != null)
                for (int i = 0; i < oldItem.getAbilities().size(); i++)
                    if (!Utils.validateItem(oldIs, player, i, e)) {
                        Ability ability = oldItem.getAbilities().get(i);
                        Instruction instruction = ability.instruction();
                        if (instruction != null)
                            instruction.run(e);
                    }

    }

    @EventHandler(priority = EventPriority.HIGH)
    private void hit(EntityDamageEvent e) {
        Player player;

        if (e instanceof EntityDamageByEntityEvent edbe)
            if (edbe.getDamager() instanceof Player)
                player = (Player) edbe.getDamager();
            else
                return;
        else if (e.getEntity() instanceof Player)
            player = (Player) e.getEntity();
        else if (e.getEntity() instanceof org.bukkit.entity.Item is) {
            Item item = Item.toItem(is.getItemStack());
            if (item == null) return;

            if (item.getAbilities() == null) return;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }

            return;
        }
        else return;

        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    ItemStack bufferItem;
    @EventHandler(priority = EventPriority.HIGH)
    private void launch(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player player)) return;
        ItemStack is = player.getInventory().getItemInMainHand();
        bufferItem = is;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (item.getAbilities() == null) return;
        for (int i = 0; i < item.getAbilities().size(); i++) {
            Ability ability = item.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null)
                instruction.run(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void projHit(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) return;
        ItemStack is = bufferItem;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (item.getAbilities() == null) return;
        for (int i = 0; i < item.getAbilities().size(); i++) {
            Ability ability = item.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null)
                instruction.run(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void death(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void fish(PlayerFishEvent e) {
        Player player = e.getPlayer();
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void target(EntityTargetLivingEntityEvent e) {
        if (!(e.getTarget() instanceof Player player)) return;
        List<Item> items = Utils.getCustomItemsInInv(player.getInventory());
        if (items.isEmpty()) return;

        for (Item item : items) {
            if (item.getAbilities() == null) continue;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null)
                    instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void click(InventoryClickEvent e) {
        ItemStack pickUp = e.getCurrentItem();
        ItemStack putDown = e.getCursor();
        ItemStack hand = null;

        if (e.getClickedInventory() instanceof AnvilInventory) {
            ItemStack is = e.getInventory().getItem(1);
            Item item = null;
            if (is != null) item = Item.toItem(is);

            if (item != null) {
                if (item.getAbilities() == null) return;
                for (int i = 0; i < item.getAbilities().size(); i++) {
                    Ability ability = item.getAbilities().get(i);
                    Instruction instruction = ability.instruction();
                    if (instruction != null) {
                        instruction.run(e);
                    }
                }
            }
            return;
        }

        if (e.getInventory().getHolder() instanceof Player player) {
            hand = player.getInventory().getItemInMainHand();
        }

        Item pickUpItem = null;
        Item putDownItem = null;
        Item handItem = null;
        if (pickUp != null) pickUpItem = Item.toItem(pickUp);
        if (putDown != null) putDownItem = Item.toItem(putDown);
        if (hand != null) handItem = Item.toItem(hand);

        if (pickUpItem != null) {
            if (pickUpItem.getAbilities() == null) return;
            for (int i = 0; i < pickUpItem.getAbilities().size(); i++) {
                Ability ability = pickUpItem.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null) {
                    instruction.run(e);
                }
            }
        }

        if (putDownItem != null) {
            if (putDownItem.getAbilities() == null) return;
            for (int i = 0; i < putDownItem.getAbilities().size(); i++) {
                Ability ability = putDownItem.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null) {
                    instruction.run(e);
                }
            }
        }

        if (handItem != null) {
            if (handItem.getAbilities() == null) return;
            for (int i = 0; i < handItem.getAbilities().size(); i++) {
                Ability ability = handItem.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null) {
                    instruction.run(e);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void close(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        Item item = Item.toItem(is);

        if (item != null) {
            if (item.getAbilities() == null) return;
            for (int i = 0; i < item.getAbilities().size(); i++) {
                Ability ability = item.getAbilities().get(i);
                Instruction instruction = ability.instruction();
                if (instruction != null) {
                    instruction.run(e);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void despawn(ItemDespawnEvent e) {
        ItemStack is = e.getEntity().getItemStack();
        if (!Item.isCustomItem(is)) return;

        Item item = Item.toItem(is);
        if (item == null) return;
        if (item.getAbilities() == null) return;
        for (int i = 0; i < item.getAbilities().size(); i++) {
            Ability ability = item.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null) {
                instruction.run(e);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void prepareAnvil(PrepareAnvilEvent e) {
        ItemStack is0 = e.getInventory().getItem(0);
        ItemStack is1 = e.getInventory().getItem(1);
        if (is0 == null) return;
        if (is1 == null) return;

        Item item0 = Item.toItem(is0);
        if (item0 == null) return;
        if (item0.getAbilities() == null) return;
        for (int i = 0; i < item0.getAbilities().size(); i++) {
            Ability ability = item0.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null) {
                instruction.run(e);
            }
        }

        Item item1 = Item.toItem(is1);
        if (item1 == null) return;
        if (item1.getAbilities() == null) return;
        for (int i = 0; i < item1.getAbilities().size(); i++) {
            Ability ability = item1.getAbilities().get(i);
            Instruction instruction = ability.instruction();
            if (instruction != null) {
                instruction.run(e);
            }
        }
    }
}
