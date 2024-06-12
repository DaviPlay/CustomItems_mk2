package davide.customitems.gui.itemCreationGUIs;

import davide.customitems.api.Instruction;
import davide.customitems.gui.GUI;
import davide.customitems.gui.IGUI;
import davide.customitems.itemCreation.Ability;
import davide.customitems.itemCreation.AbilityType;
import davide.customitems.itemCreation.UtilsBuilder;
import davide.customitems.lists.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AbilityCreationGUI extends GUI {

    public static Inventory inv;

    private String name;
    private AbilityType abilityType;
    private int cooldown;
    private final List<String> description;
    private Events event;
    private int slot;
    private int abilityIdx;
    private boolean modifying;
    private final IGUI gui;

    public AbilityCreationGUI(IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Creating a new Ability...");
        name = "";
        abilityType = null;
        cooldown = 0;
        description = new ArrayList<>();
        event = null;
        this.gui = type;
        setInv();
        new AbilityTypeGUI(this);
    }

    public AbilityCreationGUI(String name, AbilityType abilityType, int cooldown, List<String> description, Events event, int slot, int abilityIdx, IGUI type) {
        inv = Bukkit.createInventory(this, 36, "Creating a new Ability...");
        this.name = name;
        this.abilityType = abilityType;
        this.cooldown = cooldown;
        this.description = new ArrayList<>(description);
        this.event = event;
        this.gui = type;
        this.slot = slot;
        this.abilityIdx = abilityIdx;
        modifying = true;
        setInv();
        new AbilityTypeGUI(this);
    }

    private void setInv() {
        super.setInv(inv);

        inv.setItem(10, new UtilsBuilder(new ItemStack(Material.STICK), "§aAbility Type", false).build().getItemStack());

        if (!name.isBlank())
            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore("§f" + name).build().getItemStack());
        else
            inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).build().getItemStack());

        inv.setItem(14, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooldown", false).lore("§f" + cooldown).build().getItemStack());

        if (!description.isEmpty())
            inv.setItem(16, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aDescription", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + description).build().getItemStack());
        else
            inv.setItem(16, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aDescription", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());

        if (event == null)
            inv.setItem(21, new UtilsBuilder(new ItemStack(Material.ENDER_EYE), "§aEvents", false).build().getItemStack());
        else
            inv.setItem(21, new UtilsBuilder(new ItemStack(Material.ENDER_EYE), "§aEvents", false).lore("§f" + event.name()).build().getItemStack());

        inv.setItem(23, new UtilsBuilder(new ItemStack(Material.ENCHANTING_TABLE), "§aCreate", false).build().getItemStack());
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem, ClickType clickType, Inventory inventory) {
        switch (slot) {
            case 10 -> whoClicked.openInventory(AbilityTypeGUI.inv);
            case 12 -> GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", "Name"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    String name = (strings[0] + " " + strings[1]).trim();
                    setName(name);
                    inv.setItem(12, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aName", false).lore("§f" + name).build().getItemStack());
                }
            }, whoClicked, inv);
            case 14 -> GUIUtils.signRead(Arrays.asList("", "^^^^^^^^^^", "Cooldown", "in seconds"), new Instruction() {
                @Override
                public <E> void run(E element) {
                    String[] strings = (String[]) element;
                    try {
                        if (Integer.parseInt(strings[0]) < 0) {
                            whoClicked.sendMessage("§cCan't input a number less than 0");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        whoClicked.sendMessage("§cInsert an integer");
                        return;
                    }

                    int i = Integer.parseInt(strings[0]);
                    setCooldown(i);
                    inv.setItem(14, new UtilsBuilder(new ItemStack(Material.CLOCK), "§aCooldown", false).lore("§f" + i).build().getItemStack());
                }
            }, whoClicked, inv);
            case 16 -> {
                if (clickType.isLeftClick())
                    GUIUtils.signRead(Arrays.asList("", "", "^^^^^^^^^^", " 1 line of description"), new Instruction() {
                        @Override
                        public <E> void run(E element) {
                            String[] strings = (String[]) element;
                            String line = (strings[0] + " " + strings[1]).trim();
                            description.add(line);
                            inv.setItem(16, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aDescription", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + description).build().getItemStack());
                        }
                    }, whoClicked, inv);
                else if (clickType.isRightClick()) {
                    if (!description.isEmpty()) {
                        description.remove(description.size() - 1);
                        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aDescription", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line", "§f" + description).build().getItemStack());
                    } else
                        inv.setItem(16, new UtilsBuilder(new ItemStack(Material.OAK_SIGN), "§aDescription", false).lore("§eLeft click to add a line to the item lore", "§eRight click to remove the last line").build().getItemStack());
                }
            }
            case 21 -> {
                new EventsGUI("", slot, inv, this);
                whoClicked.openInventory(EventsGUI.eventsInv.get(0));
            }
            case 23 -> {
                if (name.isBlank()) {
                    whoClicked.sendMessage("§cThe ability must have a name");
                    return;
                }
                if (abilityType == null) {
                    whoClicked.sendMessage("§cThe ability must have a type");
                    return;
                }
                if (event == null) {
                    whoClicked.sendMessage("§cThe ability must have an event tied to it");
                    return;
                }

                if (modifying) {
                    ((ItemCreationGUI)((AbilitiesGUI) gui).getType()).getAbilities().set(abilityIdx, new Ability(event, abilityType, name, cooldown, description.toArray(String[]::new)));

                    if (abilityType == AbilityType.PASSIVE)
                        gui.getInventory().setItem(this.slot, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§6§l" + name + " §r§7" + cooldown + "s").lore("§a" + event.name(), "§e§l" + abilityType.name(), "§f" + description, "", "§aLeft click to modify", "§cRight click to remove").build().getItemStack());
                    else
                        gui.getInventory().setItem(this.slot, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§6§l" + name + " §r§7" + cooldown + "s").lore("§a" + event.name(), "§e§l" + abilityType.getPrefix(), "§f" + description, "", "§aLeft click to modify", "§cRight click to remove").build().getItemStack());
                } else {
                    ((ItemCreationGUI)((AbilitiesGUI) gui).getType()).getAbilities().add(new Ability(event, abilityType, name, cooldown, description.toArray(String[]::new)));

                    for (int i = 0; i < gui.getInventory().getSize(); i++)
                        if (gui.getInventory().getItem(i).equals(ItemList.abilitiesGlass.getItemStack())) {
                            if (abilityType == AbilityType.PASSIVE)
                                gui.getInventory().setItem(i, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§6§l" + name + " §r§7" + cooldown + "s").lore("§a" + event.name(), "§e§l" + abilityType.name(), "§f" + description, "", "§aLeft click to modify", "§cRight click to remove").build().getItemStack());
                            else
                                gui.getInventory().setItem(i, new UtilsBuilder(new ItemStack(Material.YELLOW_DYE), "§6§l" + name + " §r§7" + cooldown + "s").lore("§a" + event.name(), "§e§l" + abilityType.getPrefix(), "§f" + description, "", "§aLeft click to modify", "§cRight click to remove").build().getItemStack());

                            break;
                        }
                }
                whoClicked.openInventory(AbilitiesGUI.inv);
            }
            case 27 -> whoClicked.openInventory(AbilitiesGUI.inv);
        }

        super.onGUIClick(whoClicked, slot, clickedItem ,clickType, inventory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public void setAbilityType(AbilityType abilityType) {
        this.abilityType = abilityType;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }
}
