package davide.customitems.events;

import davide.customitems.CustomItems;
import davide.customitems.api.Instruction;
import davide.customitems.api.SpecialBlocks;
import davide.customitems.api.UUIDDataType;
import davide.customitems.api.Utils;
import davide.customitems.crafting.CraftingType;
import davide.customitems.gui.ViewRecipesFromMat;
import davide.customitems.itemCreation.Item;
import davide.customitems.itemCreation.Type;
import davide.customitems.lists.ItemList;
import davide.customitems.playerStats.ChanceManager;
import davide.customitems.reforgeCreation.Reforge;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeneralListeners implements Listener {
    private static CustomItems plugin;

    public GeneralListeners(CustomItems plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        GeneralListeners.plugin = plugin;
    }

    @EventHandler
    private void disableBlockPlace(BlockPlaceEvent e) {
        if (Item.isCustomItem(e.getItemInHand()))
            e.setCancelled(true);
    }

    @EventHandler
    private void disableEating(PlayerItemConsumeEvent e) {
        if (Item.isCustomItem(e.getItem()))
            e.setCancelled(true);
    }

    @EventHandler
    private void dropCustomItemsOnKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();

        for (List<Item> items : ItemList.items)
            for (Item it : items) {
                if (it.getCraftingType() != CraftingType.DROP) continue;
                if (it.getEntityDropChances() == null) continue;

                for (Map.Entry<Double, List<EntityType>> entry : it.getEntityDropChances().entrySet()) {
                    for (EntityType et : entry.getValue()) {
                        if (e.getEntity().getType() != et) continue;
                        Entity entity = e.getEntity();

                        ChanceManager.chanceCalculation(entry.getKey(), new Instruction() {
                            @Override
                            public void run() {
                                entity.getWorld().dropItemNaturally(entity.getLocation(), it.getItemStack());
                                Utils.autoRecombUpgrade(it.getItemStack(), player);
                            }
                        }, player);
                    }
                }
            }
    }

    @EventHandler
    private void dropCustomItemsOnBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        for (List<Item> items : ItemList.items)
            for (Item it : items) {
                if (it.getCraftingType() != CraftingType.DROP) continue;
                if (it.getBlockDropChances() == null) continue;

                for (Map.Entry<Double, List<Material>> entry : it.getBlockDropChances().entrySet()) {
                    for (Material bt : entry.getValue()) {
                        if (e.getBlock().getType() != bt) continue;
                        Block b = e.getBlock();

                        ChanceManager.chanceCalculation(entry.getKey(), new Instruction() {
                            @Override
                            public void run() {
                                b.getWorld().dropItemNaturally(b.getLocation(), it.getItemStack());
                                Utils.autoRecombUpgrade(it.getItemStack(), player);
                            }
                        }, player);
                    }
                }
            }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPlayedBefore()) return;

        Utils.addToInventory(player, ItemList.recipeBook.getItemStack());
    }

    @EventHandler
    private void inheritPropertiesOnCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getType() != InventoryType.WORKBENCH) return;
        ItemStack is = e.getInventory().getResult();
        if (is == null || !Item.isCustomItem(is)) return;

        Inventory craftingInv = e.getInventory();
        for (int i = 1; i < 10; i++) {
            ItemStack item = craftingInv.getItem(i);
            if (item == null) continue;

            if (Item.isCustomItem(item) && Item.toItem(item).getType() != Type.MATERIAL) {
                Reforge reforge = Reforge.getReforge(item);
                boolean isRecombed = item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "recombed"), PersistentDataType.BOOLEAN);

                if (reforge != null) Reforge.setReforge(reforge, is);
                if (isRecombed) Utils.recombItem(is, item);

                break;
            }
        }
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

        Item.addEnchantsToLore(enchants, is, enchants.size() > 1);
    }

    @EventHandler
    private void addEnchantsOnAnvilCombine(PrepareAnvilEvent e) {
        ItemStack es = e.getInventory().getItem(1);
        ItemStack is = e.getInventory().getItem(0);
        if (es == null) return;
        ItemMeta esMeta = es.getItemMeta();
        if (is == null) return;
        ItemMeta isMeta = is.getItemMeta();
        if (isMeta == null) return;
        Map<Enchantment, Integer> enchants = new HashMap<>();
        if (esMeta instanceof EnchantmentStorageMeta meta) {
            enchants.putAll(meta.getStoredEnchants());
            enchants.putAll(isMeta.getEnchants());

            ItemStack finalItem = e.getResult();
            if (finalItem == null) return;

            Item.removeEnchantsFromLore(finalItem);
            Item.addEnchantsToLore(enchants, finalItem, isMeta.getEnchants().isEmpty() && meta.getStoredEnchants().size() > 1);
        } else {
            enchants.putAll(esMeta.getEnchants());
            enchants.putAll(isMeta.getEnchants());

            ItemStack finalItem = e.getResult();
            if (finalItem == null) return;

            Item.removeEnchantsFromLore(finalItem);
            Item.addEnchantsToLore(enchants, finalItem, false);
        }
    }

    @EventHandler
    private void removeEnchantsOnGrindstoneUse(PrepareGrindstoneEvent e) {
        ItemStack is;
        try {
            is = e.getInventory().getItem(0) != null ? e.getInventory().getItem(0).clone() : e.getInventory().getItem(1).clone();
        } catch (NullPointerException exc) {
            return;
        }
        if (Item.isCustomItem(is))
            e.setResult(is);
        else
            return;

        ItemStack finalItem = e.getResult();
        if (finalItem == null) return;
        Item.removeEnchantsFromLore(finalItem);

        ItemMeta finalMeta = finalItem.getItemMeta();
        if (finalMeta == null) return;
        if (finalMeta.getEnchants().isEmpty()) return;

        for (Enchantment ench : finalMeta.getEnchants().keySet())
            finalMeta.removeEnchant(ench);

        finalItem.setItemMeta(finalMeta);
    }
}
