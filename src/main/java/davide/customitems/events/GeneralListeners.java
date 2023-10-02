package davide.customitems.events;

import davide.customitems.api.SpecialBlocks;
import davide.customitems.api.UUIDDataType;
import davide.customitems.api.Utils;
import davide.customitems.gui.ViewRecipesFromMat;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.SubType;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class GeneralListeners implements Listener {

    @EventHandler
    private void disableBlockPlace(BlockPlaceEvent e) {
        if (Item.toItem(e.getItemInHand()) != null)
            e.setCancelled(true);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPlayedBefore()) return;

        Utils.addToInventory(player, ItemList.recipeBook.getItemStack());
    }

    @EventHandler
    private void viewRecipeOfMatOnRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() != null)
            if (SpecialBlocks.isClickableBlock(e.getClickedBlock())) return;

        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack is = e.getItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        if (item.getType() != Type.MATERIAL) return;

        new ViewRecipesFromMat(item);
        if (!ViewRecipesFromMat.getInvs().isEmpty())
            player.openInventory(ViewRecipesFromMat.getInvs().get(0));
    }

    @EventHandler
    private void generateUUIDOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Item item = Item.toItem(is);
        if (item == null) return;

        if (item.hasRandomUUID())
            container.set(item.getKey(), new UUIDDataType(), UUID.randomUUID());
    }

    @EventHandler
    private void disableShiftOnCraft(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;

        if (e.getClick().isShiftClick())
            e.setCancelled(true);
    }

    @EventHandler
    private void onDyeReset(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack is = e.getItem();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        Block b = e.getClickedBlock();
        if (b == null) return;

        if (b.getType() == Material.WATER_CAULDRON)
            e.setCancelled(true);

    }

    @EventHandler
    private void onDyeCraft(PrepareItemCraftEvent e) {
        ItemStack is = e.getInventory().getResult();
        if (is == null) return;
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (!(meta instanceof LeatherArmorMeta)) return;
        Inventory inv = e.getInventory();

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inv.getItem(i + 1);

            if (itemStack != null) {
                if (Item.toItem(itemStack) == null && SpecialBlocks.isDye(itemStack.getType()))
                    e.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    private void preventEnchantOnGlint(PrepareItemEnchantEvent e) {
        Item item = Item.toItem(e.getItem());
        if (item == null) return;

        if (item.isGlint())
            e.setCancelled(true);
    }

    @EventHandler
    private void disableCustomItemRenaming(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack is = e.getCurrentItem();
        if (is == null || !Item.isCustomItem(is)) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        String name = meta.getDisplayName();
        Reforge reforge = Reforge.getReforge(is);
        Item item = Item.toItem(is);
        String colorCode = name.charAt(0) + "" + name.charAt(1);

        if (reforge != null) {
            if (item != null && !name.replace(reforge.getName() + " ", "").replace(colorCode, "").equals(Item.getName(is)))
                e.setCancelled(true);
        } else {
            if (item != null && !name.replace(colorCode, "").equals(Item.getName(is)))
                e.setCancelled(true);
        }
    }

    @EventHandler
    private void addEnchantsOnEnchantingTableUse(EnchantItemEvent e) {
        ItemStack is = e.getItem();
        Item item = Item.toItem(is);
        if (item == null) return;
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return;
        Map<Enchantment, Integer> enchants = e.getEnchantsToAdd();

        Item.addEnchantsToLore(enchants, is);
    }

    @EventHandler
    private void addEnchantsOnAnvilCombine(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;
        ItemStack es = e.getInventory().getItem(1);
        if (es == null) return;

        ItemStack finalItem = e.getCurrentItem();
        if (finalItem == null) return;
        Item.removeEnchantsFromLore(finalItem);

        ItemMeta finalMeta1 = finalItem.getItemMeta();
        if (finalMeta1 == null) return;
        Map<Enchantment, Integer> enchants = finalMeta1.getEnchants();
        Item.addEnchantsToLore(enchants, finalItem);
    }

    @EventHandler
    private void removeEnchantsOnGrindstoneUse(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.GRINDSTONE) return;
        if (e.getSlotType() != InventoryType.SlotType.RESULT) return;

        ItemStack finalItem = e.getCurrentItem();
        if (finalItem == null) return;
        Item.removeEnchantsFromLore(finalItem);
    }
}
