package davide.customitems.gui;

import davide.customitems.api.Utils;
import davide.customitems.lists.ItemList;
import davide.customitems.itemCreation.Item;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CraftingInventories implements IGUI {
    private static HashMap<NamespacedKey, Inventory> invs;

    public CraftingInventories() {
        setInvs();
    }

    public CraftingInventories(Item item) {
        createInv(item);
    }

    public void setInvs() {
        invs = new HashMap<>();

        for (List<Item> items : ItemList.items)
            for (Item item : items)
                if (item.getCrafting() != null)
                    switch (item.getCraftingType()) {
                        case SHAPED -> createShapedInvs(item);
                        case SHAPELESS -> createShapelessInvs(item);
                        case FURNACE -> createFurnaceInvs(item);
                    }
    }

    public void createInv(Item item) {
        if (item.getCrafting() != null)
            switch (item.getCraftingType()) {
                case SHAPED -> createShapedInvs(item);
                case SHAPELESS -> createShapelessInvs(item);
                case FURNACE -> createFurnaceInvs(item);
            }
    }

    private void createShapedInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(this, 54, itemResult.getName());
        int i = 0;
        int j;

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapedRecipe sr) {
                String[] shape = sr.getShape();
                Map<Character, RecipeChoice> map = sr.getChoiceMap();

                if (item != null && sr.getKey().equals(item.getKey()))
                    for (String sItem : shape) {
                        char[] chars = sItem.toCharArray();
                        i++;
                        j = 0;

                        for (char ingredient : chars) {
                            j++;
                            RecipeChoice choice = map.get(ingredient);
                            ItemStack is = null;

                            if (choice != null)
                                if (choice instanceof RecipeChoice.ExactChoice)
                                    is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                                else
                                    is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                            if (i > 3) break;

                            inv.setItem((i * 9) + j, is);
                        }
                    }
            }

        inv.setItem(23, ItemList.shapedCrafting.getItemStack());
        inv.setItem(25, result);
        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private void createShapelessInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        if (item == null) return;
        List<ItemStack> crafting = item.getCrafting();
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(this, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            if (x != 10 && x != 11 && x != 12 && x != 19 && x != 20 && x != 21 && x != 28 && x != 29 && x != 30)
                inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof ShapelessRecipe sr) {
                List<RecipeChoice> choices = sr.getChoiceList();
                ItemStack is = null;
                int i = -1;

                if (sr.getKey().equals(item.getKey()))
                    for (RecipeChoice choice : choices) {
                        i++;
                        if (choice != null)
                            if (choice instanceof RecipeChoice.ExactChoice)
                                is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                            else
                                is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                        if (i > crafting.size()) break;

                        if (i < 3)
                            inv.setItem(10 + i, is);
                        else if (i >= 3 && i < 6)
                            inv.setItem(19 + (i - 3), is);
                        else if (i >= 6 && i < 9)
                            inv.setItem(28 + (i - 6), is);
                    }
                break;
            }

        inv.setItem(23, ItemList.shapelessCrafting.getItemStack());
        inv.setItem(25, result);
        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    private void createFurnaceInvs(Item itemResult) {
        ItemStack result = itemResult.getItemStack();
        Item item = Item.toItem(result);
        List<Recipe> recipes = Bukkit.getRecipesFor(result);
        assert result.getItemMeta() != null;
        Inventory inv = Bukkit.createInventory(this, 54, itemResult.getName());

        for (int x = 0; x < 54; x++)
            inv.setItem(x, ItemList.fillerGlass.getItemStack());

        for (Recipe recipe : recipes)
            if (recipe instanceof FurnaceRecipe fr) {
                RecipeChoice choice = fr.getInputChoice();
                ItemStack is = null;

                if (item != null && fr.getKey().equals(item.getKey()))
                    if (choice instanceof RecipeChoice.ExactChoice)
                        is = ((RecipeChoice.ExactChoice) choice).getItemStack();
                    else
                        is = ((RecipeChoice.MaterialChoice) choice).getItemStack();

                inv.setItem(11, is);
            }

        inv.setItem(29, new ItemStack(Material.COAL));
        inv.setItem(22,ItemList.furnaceCrafting.getItemStack());
        inv.setItem(24, result);
        inv.setItem(45, ItemList.backArrow.getItemStack());
        inv.setItem(49, ItemList.closeBarrier.getItemStack());
        invs.put(itemResult.getKey(), inv);
    }

    public static Inventory getInv(NamespacedKey key) {
        return invs.get(key);
    }
    public static HashMap<NamespacedKey, Inventory> getInvs() {
        return invs;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        ItemMeta meta = clickedItem.getItemMeta();
        PersistentDataContainer container = null;
        if (meta != null)
            container = meta.getPersistentDataContainer();
        Item item = Item.toItem(clickedItem);

        if (item != null && !ItemList.utilsItems.contains(item)) {
            if (container != null)
                if (container.getKeys().contains(item.getKey()))
                    if (whoClicked.getGameMode() == GameMode.CREATIVE) {
                        if (clickType.isLeftClick()) {
                            if (item.hasRandomUUID())
                                Item.setRandomUUID(clickedItem);

                            Utils.addToInventory(whoClicked, clickedItem);
                        } else if (clickType.isRightClick()) {
                            if (CraftingInventories.getInv(item.getKey()) != null)
                                whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                        }
                    } else {
                        if (CraftingInventories.getInv(item.getKey()) != null)
                            whoClicked.openInventory(CraftingInventories.getInv(item.getKey()));
                    }
        }

        switch (slot) {
            case 45 -> {
                for (Inventory inv : ItemsGUI.itemInv)
                    if (inventory.equals(inv))
                        whoClicked.openInventory(ItemsGUI.itemInv.get(0));

                if (inventory != null)
                    whoClicked.openInventory(ItemsGUI.itemInv.get(Utils.findItemInv(inventory)));
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return ItemsGUI.itemInv.get(0);
    }
}
