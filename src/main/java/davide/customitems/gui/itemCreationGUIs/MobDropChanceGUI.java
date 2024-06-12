package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.Instruction;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobDropChanceGUI extends GUI {
    public static Inventory inv;
    private double chance;
    private final List<EntityType> entities;
    private final List<Material> blocks;

    private final IGUI type;

    public MobDropChanceGUI(IGUI type) {
        entities = new ArrayList<>();
        blocks = new ArrayList<>();
        inv = Bukkit.createInventory(this, 54, "Mob & Drop Chance");
        this.type = type;
        setInv();
    }

    private void setInv() {
        super.setInv(inv);

        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§aAdd a mob", false).build().getItemStack());
        inv.setItem(23, new UtilsBuilder(new ItemStack(Material.COMPASS), "§aDrop Chance", false).build().getItemStack());
        inv.setItem(30, new UtilsBuilder(new ItemStack(Material.STONE), "§aAdd a block", false).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 12 -> signReadMobs(whoClicked, this, inv);
            case 23 -> signReadChance(whoClicked);
            case 30 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Crafting Material"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    new CraftingMaterialGUI((strings[0] + " " + strings[1]).trim(), true, slot, inv, getGUIType());
                }
            }, whoClicked, CraftingMaterialGUI.invs.get(0));
            case 45 -> {
                if (type instanceof MaterialCreationGUI m) {
                    if (!entities.isEmpty())
                        m.getEntityDrops().put(chance, entities);
                    if (!blocks.isEmpty())
                        m.getBlockDrops().put(chance, blocks);
                    MaterialCreationGUI.inv.setItem(32, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§aMob, Blocks & Drop Chance", false).lore("§f" + m.getEntityDrops().toString(), "§f" + m.getBlockDrops().toString()).build().getItemStack());
                    whoClicked.openInventory(MaterialCreationGUI.inv);
                }
                else if (type instanceof ItemCreationGUI i) {
                    if (!entities.isEmpty())
                        i.getEntityDrops().put(chance, entities);
                    if (!blocks.isEmpty())
                        i.getBlockDrops().put(chance, blocks);
                    ItemCreationGUI.inv.setItem(32, new UtilsBuilder(new ItemStack(Material.ZOMBIE_HEAD), "§aMob, Blocks & Drop Chance", false).lore("§f" + i.getEntityDrops().toString(), "§f" + i.getBlockDrops().toString()).build().getItemStack());
                    whoClicked.openInventory(ItemCreationGUI.inv);
                }
            }
            case 49 -> whoClicked.closeInventory();
        }
    }

    public static void signReadMobs(Player whoClicked, IGUI type, Inventory inv) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "", "^^^^^^^^^^", "Mob"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    new CraftingEntityGUI((strings[0] + " " + strings[1]).trim(), inv, type);
                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(CraftingEntityGUI.invs.get(0)), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    private void signReadChance(Player whoClicked) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Drop Chance", "as a decimal"))
                .reopenIfFail(true)
                .response(((player, strings) -> {
                    try {
                        if (Double.parseDouble(strings[0]) <= 0) {
                            player.sendMessage("§cCan't input a number less or equal to 0");
                            return false;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cInsert a decimal");
                        return false;
                    }

                    double chance = Double.parseDouble(strings[0]);
                    this.chance = chance;

                    inv.setItem(23, new UtilsBuilder(new ItemStack(Material.COMPASS), "§aDrop Chance", false).lore("§f" + chance + "%").build().getItemStack());

                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    public List<EntityType> getEntities() {
        return entities;
    }

    public List<Material> getBlocks() {
        return blocks;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
