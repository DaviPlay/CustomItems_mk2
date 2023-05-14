package davide.customitems.itemCreation;

public enum AbilityType {
        CLICK("CLICK"),
        RIGHT_CLICK("RIGHT CLICK"),
        SHIFT_RIGHT_CLICK("SHIFT-RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SHIFT_LEFT_CLICK("SHIFT-LEFT CLICK"),
        HIT("HIT"),
        SHIFT("SNEAK"),
        FULL_SET("FULL SET"),
        PASSIVE("");

        private final String prefix;

        AbilityType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
}
