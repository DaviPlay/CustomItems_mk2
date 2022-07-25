package davide.customitems.itemCreation;

public enum Type implements IType {
        TOOL,
        MELEE,
        RANGED,
        ARMOR,
        FOOD,
        MATERIAL,
        ITEM;

        @Override
        public String getDisplayableType() {
                return this.name();
        }
}
