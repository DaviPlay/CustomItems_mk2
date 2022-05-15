package davide.customitems.ItemCreation;

public enum Type implements TypeInterface {
        TOOL,
        WEAPON,
        ARMOR,
        FOOD,
        MATERIAL,
        ITEM;

        @Override
        public String getDisplayableType() {
                return this.name();
        }
}
