package com.alessandro.astages.util.underdevelopment;

import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.develop.NotYetImplemented;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.function.Consumer;

@UnderDevelopment
@NotNullParams
@NotYetImplemented
public class StageHelper {
    private final String id;
    private final String stage;
    private int ordinal = -1;

    private final HashMap<ARestrictionType, ARestriction<?, ?, ?>> RESTRICTIONS = new HashMap<>();

    public StageHelper(String id, String stage) {
        this.id = id;
        this.stage = stage;
    }

    public AItemRestriction item() {
        return new AItemRestriction(id + increaseOrdinal(), stage + getOrdinal());
    }

    public StageHelper item(Item item, Consumer<AItemRestriction> properties) {
        var restriction = new AItemRestriction(id + increaseOrdinal(), stage + getOrdinal())
            .restrict(item);

        properties.accept(restriction);

        RESTRICTIONS.put(ARestrictionTypes.ITEM, restriction);
        return this;
    }

    public void build() {
        RESTRICTIONS.forEach((type, restriction) -> {

        });
    }

    private int increaseOrdinal() {
        return ordinal++;
    }

    private int getOrdinal() {
        return ordinal;
    }

//    static {
//        new StageHelper("id", "stage")
//            .item(Items.ACACIA_BOAT, restriction -> restriction.setCanBeEquipped(false))
//            .build();
//    }
}
