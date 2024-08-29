package davide.customitems.itemCreation;

public enum SubType implements IType {
    //Foods
    POTION(Type.FOOD),
    SOUP(Type.FOOD),

    //Melee
    SWORD(Type.MELEE),
    LONGSWORD(Type.MELEE),
    GREATAXE(Type.MELEE),
    HAMMER(Type.MELEE),
    MACE(Type.MELEE),
    DAGGER(Type.MELEE),
    SHIELD(Type.MELEE),

    //Ranged
    BOW(Type.RANGED),
    CROSSBOW(Type.RANGED),
    STAFF(Type.RANGED),         // damage

    //Armors
    HELMET(Type.ARMOR),
    CHESTPLATE(Type.ARMOR),
    LEGGINGS(Type.ARMOR),
    BOOTS(Type.ARMOR),

    //Tools
    PICKAXE(Type.TOOL),
    AXE(Type.TOOL),
    SHOVEL(Type.TOOL),
    HOE(Type.TOOL),
    WAND(Type.TOOL),            // utility
    DEPLOYABLE(Type.TOOL),
    TALISMAN(Type.TOOL),        // consumable
    ACCESSORY(Type.TOOL);       // passive buff

    private final Type type;
    SubType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getDisplayableType() {
        return type.getDisplayableType();
    }
}
