package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.CustomItems;
import davide.customitems.api.SignMenuFactory;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StatsGUI extends GUI {
    public static Inventory inv;

    private final IGUI type;

    public StatsGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Stats");
        this.type = type;
        setInv();
    }

    public void setInv() {
        super.setInv(inv);

        ItemCreationGUI i = (ItemCreationGUI) type;

        inv.setItem(4, new UtilsBuilder(new ItemStack(Material.PINK_DYE), "§dAttack Speed", false).lore("§cUse only if you know the effects!", "§f" + i.getAttackSpeed()).build().getItemStack());
        inv.setItem(10, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§cDamage", false).lore("§f" + i.getDamage()).build().getItemStack());
        inv.setItem(11, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§6Critical Chance", false).lore("§f" + i.getCritChance() + "%").build().getItemStack());
        inv.setItem(12, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§eCritical Damage", false).lore("§f" + i.getCritDamage() + "x").build().getItemStack());
        inv.setItem(13, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§f" + i.getHealth()).build().getItemStack());
        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§f" + i.getDefence()).build().getItemStack());
        inv.setItem(15, new UtilsBuilder(new ItemStack(Material.WHITE_DYE), "§fSpeed", false).lore("§f" + i.getSpeed()).build().getItemStack());
        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.PURPLE_DYE), "§5Defence", false).lore("§f" + i.getLuck()).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 4, 10, 11, 12, 13, 14, 15, 16 -> signReadStat(whoClicked, slot);
        }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    private void signReadStat(Player whoClicked, int slot) {
        SignMenuFactory.Menu menu = CustomItems.getSignMenuFactory().newMenu(Arrays.asList("", "^^^^^^^^^^", "Insert a", "number"))
                .reopenIfFail(false)
                .response(((player, strings) -> {
                    ItemCreationGUI i = (ItemCreationGUI) type;

                    switch (slot) {
                        case 4 -> {
                            float stat;
                            try {
                                stat = Float.parseFloat(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setAttackSpeed(stat);
                            inv.setItem(4, new UtilsBuilder(new ItemStack(Material.PINK_DYE), "§dAttack Speed", false).lore("§cUse only if you know the effects!", "§f" + stat).build().getItemStack());
                        }
                        case 10 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setDamage(stat);
                            inv.setItem(10, new UtilsBuilder(new ItemStack(Material.RED_DYE), "§cDamage", false).lore("§f" + stat).build().getItemStack());
                        }
                        case 11 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setCritChance(stat);
                            inv.setItem(11, new UtilsBuilder(new ItemStack(Material.ORANGE_DYE), "§cCritical Chance", false).lore("§f" + stat + "%").build().getItemStack());
                        }
                        case 12 -> {
                            float stat;
                            try {
                                stat = Float.parseFloat(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert a decimal number");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setCritDamage(stat);
                            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§eCritical Damage", false).lore("§f" + stat + "x").build().getItemStack());
                        }
                        case 13 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setHealth(stat);
                            inv.setItem(13, new UtilsBuilder(new ItemStack(Material.GREEN_DYE), "§aHealth", false).lore("§f" + stat).build().getItemStack());
                        }
                        case 14 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setDefence(stat);
                            inv.setItem(14, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§f" + stat).build().getItemStack());
                        }
                        case 15 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setSpeed(stat);
                            inv.setItem(15, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§f" + stat).build().getItemStack());
                        }
                        case 16 -> {
                            int stat;
                            try {
                                stat = Integer.parseInt(strings[0]);
                            } catch (NumberFormatException e) {
                                whoClicked.sendMessage("§cInsert an integer");
                                Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                                return false;
                            }
                            i.setLuck(stat);
                            inv.setItem(16, new UtilsBuilder(new ItemStack(Material.LIGHT_BLUE_DYE), "§bDefence", false).lore("§f" + stat).build().getItemStack());
                        }
                    }

                    Bukkit.getScheduler().runTaskLater(CustomItems.getPlugin(CustomItems.class), () -> whoClicked.openInventory(inv), 1);
                    return true;
                }));

        menu.open(whoClicked);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
