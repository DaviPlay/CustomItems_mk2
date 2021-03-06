package davide.customitems.itemCreation;

public enum Ability {
        RIGHT_CLICK("RIGHT CLICK"),
        SHIFT_RIGHT_CLICK("SHIFT-RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SHIFT_LEFT_CLICK("SHIFT-LEFT CLICK"),
        SHIFT("SHIFT"),
        FULL_SET("FULL SET"),
        PASSIVE("");

        private final String prefix;

        Ability(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
}
