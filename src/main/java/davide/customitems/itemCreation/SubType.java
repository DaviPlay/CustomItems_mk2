package davide.customitems.itemCreation;

public enum SubType implements IType {
    //Foods
    POTION(Type.FOOD),
    SOUP(Type.FOOD),

    //Melee
    SWORD(Type.MELEE),
    GREATAXE(Type.MELEE),
    HAMMER(Type.MELEE),
    MACE(Type.MELEE),
    DAGGER(Type.MELEE),
    SHIELD(Type.MELEE),

    //Ranged
    BOW(Type.RANGED),
    CROSSBOW(Type.RANGED),
    //Staff = Damage
    STAFF(Type.RANGED),
    //Wand = Utility
    WAND(Type.RANGED),

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
    TALISMAN(Type.TOOL);

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
