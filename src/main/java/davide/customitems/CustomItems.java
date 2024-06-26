package davide.customitems;

import davide.customitems.api.*;
import davide.customitems.commands.Commands;
import davide.customitems.crafting.CraftingAmounts;
import davide.customitems.crafting.CraftingType;
import davide.customitems.events.*;
import davide.customitems.events.customEvents.ArmorListener;
import davide.customitems.events.customEvents.PlayerJumpEvent;
import davide.customitems.events.customEvents.TrampleListener;
import davide.customitems.gui.CraftingInventories;
import davide.customitems.events.GUIEvents;
import davide.customitems.itemCreation.*;
import davide.customitems.lists.EventList;
import davide.customitems.playerStats.DamageManager;
import davide.customitems.playerStats.HealthManager;
import davide.customitems.reforgeCreation.ReforgeAssigning;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public final class CustomItems extends JavaPlugin {
    private static SignMenuFactory signMenuFactory;

    private File userItemsFile;
    private File base64File;
    private FileConfiguration userItemsConfig;
    private FileConfiguration base64Config;

    @Override
    public void onEnable() {
        PluginManager plugin = getServer().getPluginManager();

        //Configs
        createConfigs();
        buildUserItems();
        buildUserMaterials();

        //Others
        new DelayedTask(this);
        new CraftingAmounts(this);
        registerGlow();
        signMenuFactory = new SignMenuFactory(this);

        //Inventories
        new CraftingInventories();

        //Commands
        new Commands(this);

        //Cooldowns
        Cooldowns.setupCooldown();

        //Listeners
        new GeneralEvents(this);
        new GeneralListeners(this);
        PlayerJumpEvent.register(this);
        plugin.registerEvents(new ArmorListener(), this);
        plugin.registerEvents(new TrampleListener(), this);
        plugin.registerEvents(new DamageManager(), this);
        plugin.registerEvents(new HealthManager(), this);
        plugin.registerEvents(new ReforgeAssigning(), this);
        plugin.registerEvents(new GUIEvents(), this);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers())
            p.closeInventory();
    }

    private void buildUserItems() {
        if (userItemsConfig.get("items") == null || userItemsConfig.getConfigurationSection("items") == null) return;

        final List<ItemStack> is = new ArrayList<>();
        final List<Ability> abilities = new ArrayList<>();
        final HashMap<Enchantment, Integer> enchs = new HashMap<>();
        final HashMap<Double, List<EntityType>> mobsMap = new HashMap<>();
        final HashMap<Double, List<Material>> blocksMap = new HashMap<>();

        for (String key : userItemsConfig.getConfigurationSection("items").getKeys(false))
            if (userItemsConfig.getConfigurationSection("items." + key + ".ability") != null)
                for (String ability : userItemsConfig.getConfigurationSection("items." + key + ".ability").getKeys(false)) {
                    abilities.add(new Ability(EventList.valueOf(userItemsConfig.getString("items." + key + ".ability." + ability + ".event")),
                            AbilityType.valueOf(userItemsConfig.getString("items." + key + ".ability." + ability + ".ability_type")),
                            userItemsConfig.getString("items." + key + ".ability." + ability + ".name"),
                            userItemsConfig.getInt("items." + key + ".ability." + ability + ".cooldown"),
                            (List<String>) userItemsConfig.getConfigurationSection("items." + key + ".ability." + ability + ".description").getKeys(false)));

            for (String item : userItemsConfig.getList("items." + key + ".crafting_recipe").toArray(new String[]{})) {
                int amount;
                try {
                    amount = Integer.parseInt(item.substring(item.toCharArray().length - 1));
                } catch (NumberFormatException ignored) {
                    amount = 1;
                }

                if (!item.equals("null")) {
                    String itemKey = item.substring(0, item.toCharArray().length - 3);
                    if (Item.isCustomItem(itemKey)) {
                        ItemStack i = Item.toItem(itemKey).getItemStack();
                        i.setAmount(amount);
                        is.add(i);
                    } else {
                        Material material = Material.valueOf(itemKey);
                        ItemStack i = new ItemStack(material, amount);
                        is.add(i);
                    }
                } else
                    is.add(null);
            }

            if (userItemsConfig.getConfigurationSection("items." + key + ".enchantments") != null)
                for (String e : userItemsConfig.getList("items." + key + ".enchantments").toArray(new String[]{})) {
                    String enchKey = e.substring(0, e.toCharArray().length - 2);
                    int lvl;
                    try {
                        lvl = Integer.parseInt(e.substring(e.toCharArray().length - 1));
                    } catch (NumberFormatException ignored) {
                        continue;
                    }
                    enchs.put(Enchantment.getByKey(NamespacedKey.minecraft(enchKey)), lvl);
                }

            if (userItemsConfig.getConfigurationSection("items." + key + ".entity_drops") != null)
                for (String chance : userItemsConfig.getConfigurationSection("items." + key + ".entity_drops").getKeys(false)) {
                    List<EntityType> mobsList = new ArrayList<>();

                    for (String mob : userItemsConfig.getList("items." + key + ".entity_drops." + chance).toArray(new String[]{})) {
                        mobsList.add(EntityType.valueOf(mob));
                    }
                    mobsMap.put(Double.parseDouble(chance), mobsList);
                }

            if (userItemsConfig.getConfigurationSection("items." + key + ".block_drops") != null)
                for (String chance : userItemsConfig.getConfigurationSection("items." + key + ".block_drops").getKeys(false)) {
                    List<EntityType> blocksList = new ArrayList<>();

                    for (String block : userItemsConfig.getList("items." + key + ".block_drops." + chance).toArray(new String[]{})) {
                        blocksList.add(EntityType.valueOf(block));
                    }
                    mobsMap.put(Double.parseDouble(chance), blocksList);
                }

            if (SubType.valueOf(userItemsConfig.getString("items." + key + ".sub_type")) != null)
                new ItemBuilder(new ItemStack(Material.valueOf(userItemsConfig.getString("items." + key + ".material")), userItemsConfig.getInt("items." + key + ".amount")),
                        userItemsConfig.getString("items." + key + ".name"))
                        .subType(SubType.valueOf(userItemsConfig.getString("items." + key + ".sub_type")))
                        .hasRandomUUID(true)
                        .rarity(Rarity.valueOf(userItemsConfig.getString("items." + key + ".rarity")))
                        .attackSpeed(userItemsConfig.getInt("items." + key + ".attack_speed"))
                        .damage(userItemsConfig.getInt("items." + key + ".damage"))
                        .critChance(userItemsConfig.getInt("items." + key + ".critChance"))
                        .critDamage(userItemsConfig.getInt("items." + key + ".critDamage"))
                        .health(userItemsConfig.getInt("items." + key + ".health"))
                        .defence(userItemsConfig.getInt("items." + key + ".defence"))
                        .abilities(abilities.toArray(new Ability[abilities.size()]))
                        .enchantments(enchs)
                        .lore(userItemsConfig.getList("items." + key + ".lore").toArray(new String[]{}))
                        .craftingType(CraftingType.valueOf(userItemsConfig.getString("items." + key + ".crafting_type")))
                        .crafting(is)
                        .entityDrops(mobsMap)
                        .blockDrops(blocksMap)
                        .build();
            else
                new ItemBuilder(new ItemStack(Material.valueOf(userItemsConfig.getString("items." + key + ".material")), userItemsConfig.getInt("items." + key + ".amount")),
                        userItemsConfig.getString("items." + key + ".name"))
                        .type(Type.valueOf(userItemsConfig.getString("items." + key + ".type")))
                        .hasRandomUUID(true)
                        .rarity(Rarity.valueOf(userItemsConfig.getString("items." + key + ".rarity")))
                        .attackSpeed(userItemsConfig.getInt("items." + key + ".attack_speed"))
                        .damage(userItemsConfig.getInt("items." + key + ".damage"))
                        .critChance(userItemsConfig.getInt("items." + key + ".critChance"))
                        .critDamage(userItemsConfig.getInt("items." + key + ".critDamage"))
                        .health(userItemsConfig.getInt("items." + key + ".health"))
                        .defence(userItemsConfig.getInt("items." + key + ".defence"))
                        .abilities(abilities.toArray(new Ability[abilities.size()]))
                        .enchantments(enchs)
                        .lore(userItemsConfig.getList("items." + key + ".lore").toArray(new String[]{}))
                        .craftingType(CraftingType.valueOf(userItemsConfig.getString("items." + key + ".crafting_type")))
                        .crafting(is)
                        .entityDrops(mobsMap)
                        .blockDrops(blocksMap)
                        .build();
        }
    }

    private void buildUserMaterials() {
        if (userItemsConfig.get("materials") == null || userItemsConfig.getConfigurationSection("materials") == null) return;

        final List<ItemStack> is = new ArrayList<>();
        final HashMap<Double, List<EntityType>> mobsMap = new HashMap<>();
        final HashMap<Double, List<Material>> blocksMap = new HashMap<>();

        for (String key : userItemsConfig.getConfigurationSection("materials").getKeys(false)) {

            for (String item : userItemsConfig.getList("materials." + key + ".crafting_recipe").toArray(new String[]{})) {
                int amount;
                try {
                    amount = Integer.parseInt(item.substring(item.toCharArray().length - 1));
                } catch (NumberFormatException ignored) {
                    amount = 1;
                }

                if (!item.equals("null")) {
                    String itemKey = item.substring(0, item.toCharArray().length - 3);
                    if (Item.isCustomItem(itemKey)) {
                        ItemStack i = Item.toItem(itemKey).getItemStack();
                        i.setAmount(amount);
                        is.add(i);
                    } else {
                        Material material = Material.valueOf(itemKey);
                        ItemStack i = new ItemStack(material, amount);
                        is.add(i);
                    }
                } else
                    is.add(null);
            }

            if (userItemsConfig.getConfigurationSection("materials." + key + ".entity_drops") != null)
                for (String chance : userItemsConfig.getConfigurationSection("materials." + key + ".entity_drops").getKeys(false)) {
                    List<EntityType> mobsList = new ArrayList<>();

                    for (String mob : userItemsConfig.getList("materials." + key + ".entity_drops." + chance).toArray(new String[]{})) {
                        mobsList.add(EntityType.valueOf(mob));
                    }
                    mobsMap.put(Double.parseDouble(chance), mobsList);
                }

            if (userItemsConfig.getConfigurationSection("materials." + key + ".block_drops") != null)
                for (String chance : userItemsConfig.getConfigurationSection("materials." + key + ".block_drops").getKeys(false)) {
                    List<EntityType> blocksList = new ArrayList<>();

                    for (String block : userItemsConfig.getList("materials." + key + ".block_drops." + chance).toArray(new String[]{})) {
                        blocksList.add(EntityType.valueOf(block));
                    }
                    mobsMap.put(Double.parseDouble(chance), blocksList);
                }

            new MaterialBuilder(new ItemStack(Material.valueOf(userItemsConfig.getString("items." + key + ".material")), userItemsConfig.getInt("items." + key + ".amount")),
                    userItemsConfig.getString("items." + key + ".name"))
                    .lore(userItemsConfig.getList("items." + key + ".lore").toArray(new String[]{}))
                    .craftingType(CraftingType.valueOf(userItemsConfig.getString("items." + key + ".crafting_type")))
                    .crafting(is)
                    .entityDrops(mobsMap)
                    .blockDrops(blocksMap)
                    .isGlint(true)
                    .hasRandomUUID(true)
                    .build();
        }
    }

    public static SignMenuFactory getSignMenuFactory() {
        return signMenuFactory;
    }

    public FileConfiguration getUserItemsConfig() {
        return this.userItemsConfig;
    }
    public File getUserItemsFile() {
        return this.userItemsFile;
    }

    public FileConfiguration getBase64Config() {
        return this.base64Config;
    }
    public File getBase64File() {
        return this.base64File;
    }

    private void createConfigs() {
        userItemsFile = new File(getDataFolder(), "userItems.yml");
        if (!userItemsFile.exists()) {
            userItemsFile.getParentFile().mkdirs();
            saveResource("userItems.yml", false);
        }
        userItemsConfig = YamlConfiguration.loadConfiguration(userItemsFile);

        base64File = new File(getDataFolder(), "base64.yml");
        if (!base64File.exists()) {
            base64File.getParentFile().mkdirs();
            saveResource("base64.yml", false);
        }
        base64Config = YamlConfiguration.loadConfiguration(base64File);
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception ignored) {
            return;
        }
        try {
            NamespacedKey key = new NamespacedKey(this, getDescription().getName());

            Glow glow = new Glow(key);
            Enchantment.registerEnchantment(glow);
        } catch (Exception ignored) {}
    }
}
