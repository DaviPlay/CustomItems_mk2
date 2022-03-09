package davide.customitems.API;

public class AbilityType {

    public enum Ability {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
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
}
