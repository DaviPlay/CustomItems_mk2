package davide.customitems.API;

import org.bukkit.Material;

public enum VanillaItems {
    NETHERITE_SWORD(8, Material.NETHERITE_SWORD),
    DIAMOND_SWORD(7, Material.DIAMOND_SWORD),
    IRON_SWORD(6, Material.IRON_SWORD),
    STONE_SWORD(5, Material.STONE_SWORD),
    GOLDEN_SWORD(4, Material.GOLDEN_SWORD),
    WOODEN_SWORD(4, Material.WOODEN_SWORD),

    TRIDENT(9, Material.TRIDENT),

    NETHERITE_SHOVEL(6.5f, Material.NETHERITE_SHOVEL),
    DIAMOND_SHOVEL(5.5f, Material.DIAMOND_SHOVEL),
    IRON_SHOVEL(4.5f, Material.IRON_SHOVEL),
    STONE_SHOVEL(3.5f, Material.STONE_SHOVEL),
    GOLDEN_SHOVEL(2.5f, Material.GOLDEN_SHOVEL),
    WOODEN_SHOVEL(2.5f, Material.WOODEN_SHOVEL),

    NETHERITE_PICKAXE(6, Material.NETHERITE_PICKAXE),
    DIAMOND_PICKAXE(5, Material.DIAMOND_PICKAXE),
    IRON_PICKAXE(4, Material.IRON_PICKAXE),
    STONE_PICKAXE(3, Material.STONE_PICKAXE),
    GOLDEN_PICKAXE(2, Material.GOLDEN_PICKAXE),
    WOODEN_PICKAXE(2, Material.WOODEN_PICKAXE),

    NETHERITE_AXE(10, Material.NETHERITE_AXE),
    DIAMOND_AXE(9, Material.DIAMOND_AXE),
    IRON_AXE(9, Material.IRON_AXE),
    STONE_AXE(9, Material.STONE_AXE),
    GOLDEN_AXE(7, Material.GOLDEN_AXE),
    WOODEN_AXE(7, Material.WOODEN_AXE),

    NETHERITE_HOE(1, Material.NETHERITE_HOE),
    DIAMOND_HOE(1, Material.DIAMOND_HOE),
    IRON_HOE(1, Material.IRON_HOE),
    STONE_HOE(1, Material.STONE_HOE),
    GOLDEN_HOE(1, Material.GOLDEN_HOE),
    WOODEN_HOE(1, Material.WOODEN_HOE),

    OTHER(1);

    private final float expectedDamage;
    private Material type;

    VanillaItems(float expectedDamage, Material type) {
        this.expectedDamage = expectedDamage;
        this.type = type;
    }

    VanillaItems(float expectedDamage) {
        this.expectedDamage = expectedDamage;
    }

    public float getExpectedDamage() {
        return expectedDamage;
    }

    public Material getType() {
        return type;
    }
}
