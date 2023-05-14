package davide.customitems.itemCreation;

import java.util.ArrayList;
import java.util.List;

public enum Type implements IType {
        TOOL,
        MELEE,
        RANGED,
        ARMOR,
        FOOD,
        MATERIAL,
        ITEM;

        public static List<SubType> getSubTypes(Type type) {
                List<SubType> subs = new ArrayList<>();

                for (SubType s : SubType.values()) {
                        if (s.getType() == type)
                                subs.add(s);
                }

                return subs;
        }

        @Override
        public String getDisplayableType() {
                return this.name();
        }
}
