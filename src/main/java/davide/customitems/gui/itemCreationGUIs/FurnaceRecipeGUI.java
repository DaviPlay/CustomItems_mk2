package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.api.Instruction;
import davide.customitems.crafting.CraftingType;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.ItemBuilder;
import davide.customitems.itemCreation.MaterialBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FurnaceRecipeGUI extends GUI {
    public static Inventory inv;

    private final IGUI type;

    public FurnaceRecipeGUI(List<ItemStack> recipe, IGUI type) {
        inv = Bukkit.createInventory(this, 54, "Furnace Recipe");
        this.type = type;
        setInv(recipe, type);
    }

    private void setInv(List<ItemStack> recipe, IGUI type) {
        super.setInv(inv);

        if (recipe.isEmpty())
            inv.setItem(11, ItemList.craftingGlass.getItemStack());
        else
            inv.setItem(11, recipe.get(0));

        inv.setItem(29, new ItemStack(Material.COAL));
        inv.setItem(22,ItemList.furnaceCrafting.getItemStack());

        if (type instanceof MaterialCreationGUI m)
            inv.setItem(24, new MaterialBuilder(m.getItemStack(), m.getName(), false)
                            .rarity(m.getRarity())
                            .build()
                            .getItemStack());
        else if (type instanceof ItemCreationGUI i)
            if (i.getSubType() == null)
                inv.setItem(24, new ItemBuilder(i.getItemStack(), i.getName(), false)
                            .type(i.getType())
                            .rarity(i.getRarity())
                            .damage(i.getDamage())
                            .critChance(i.getCritChance())
                            .critDamage(i.getCritDamage())
                            .health(i.getHealth())
                            .defence(i.getDefence())
                            .abilities(i.getAbilities().toArray(new Ability[]{}))
                            .enchantments(i.getEnchantments())
                            .lore(i.getLore().toArray(new String[]{}))
                            .craftingType(CraftingType.NONE)
                            .build()
                            .getItemStack());
            else
                inv.setItem(24, new ItemBuilder(i.getItemStack(), i.getName(), false)
                            .subType(i.getSubType())
                            .rarity(i.getRarity())
                            .damage(i.getDamage())
                            .critChance(i.getCritChance())
                            .critDamage(i.getCritDamage())
                            .health(i.getHealth())
                            .defence(i.getDefence())
                            .abilities(i.getAbilities().toArray(new Ability[]{}))
                            .enchantments(i.getEnchantments())
                            .lore(i.getLore().toArray(new String[]{}))
                            .craftingType(CraftingType.NONE)
                            .build()
                            .getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 11 -> {
                if (clickType.isLeftClick())
                    GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Crafting Material"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), false, slot, inv, getGUIType());
                        }
                    }, whoClicked, CraftingMaterialGUI.invs);
                else if (clickType.isRightClick())
                    GUIUtils.signRead(Arrays.asList("", "^^^^^^^^^^", "Amount of", "items required"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            try {
                                if (Integer.parseInt(strings[0]) < 1 || Integer.parseInt(strings[0]) > clickedItem.getMaxStackSize()) {
                                    whoClicked.sendMessage("§cThe amount of items required can't be less then 1 or more then it's max stack size");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert a number");
                                return;
                            }

                            clickedItem.setAmount(Integer.parseInt(strings[0]));
                            inv.setItem(slot, clickedItem);
                        }
                    }, whoClicked, inv);
            }
            case 24, 45 -> {
                if (this.type instanceof MaterialCreationGUI m) {
                    m.getFurnace().clear();
                    m.getFurnace().add(inv.getItem(11));
                    whoClicked.openInventory(MaterialCreationGUI.inv);
                } else if (this.type instanceof ItemCreationGUI i) {
                    i.getFurnace().clear();
                    i.getFurnace().add(inv.getItem(11));
                    whoClicked.openInventory(ItemCreationGUI.inv);
                }
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
