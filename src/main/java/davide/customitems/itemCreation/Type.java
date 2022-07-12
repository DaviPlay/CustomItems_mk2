package davide.customitems.itemCreation;

public enum Type implements TypeInterface {
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
