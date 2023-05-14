package davide.customitems.itemCreation;

public record Ability(AbilityType type, String name, int cooldown, String... description) {
    public Ability(AbilityType type, String name, String... description) {
        this(type, name, 0, description);
    }
}
