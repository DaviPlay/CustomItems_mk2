package davide.customitems.API;

public enum Ability {
        RIGHT_CLICK("RIGHT CLICK"),
        SHIFT_RIGHT_CLICK("SHIFT-RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SHIFT_LEFT_CLICK("SHIFT-LEFT CLICK"),
        FULL_SET("FULL SET"),
        GENERIC("");

        private final String prefix;

        Ability(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
}