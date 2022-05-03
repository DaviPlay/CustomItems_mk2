package davide.customitems.API;

public enum SubType implements TypeInterface {
    //Foods
    POTION(Type.FOOD),
    SOUP(Type.FOOD),

    //Weapons
    SWORD(Type.WEAPON),
    GREATAXE(Type.WEAPON),
    MACE(Type.WEAPON),
    DAGGER(Type.WEAPON),
    BOW(Type.WEAPON),
    CROSSBOW(Type.WEAPON),
    //Staff = Damage
    STAFF(Type.WEAPON),
    //Wand = Utility
    WAND(Type.WEAPON),
    SHIELD(Type.WEAPON),

    //Armors
    HELMET(Type.ARMOR),
    CHESTPLATE(Type.ARMOR),
    LEGGINGS(Type.ARMOR),
    BOOTS(Type.ARMOR),

    //Tools
    PICKAXE(Type.TOOL),
    AXE(Type.TOOL),
    SHOVEL(Type.TOOL),
    HOE(Type.TOOL);

    private final Type type;
    SubType(Type type) {
        this.type = type;
    }

    @Override
    public String getDisplayableType() {
        return type.getDisplayableType();
    }
}