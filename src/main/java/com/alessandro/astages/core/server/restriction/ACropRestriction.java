package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.CropWrapper;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.level.block.Block;

@NotNullParamsAndMethodsReturn
public class ACropRestriction extends ARestriction<ACropRestriction, Block, CropWrapper> {
    private Block crop;

    public ACropRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ACropRestriction newBuilder() {
        return new ACropRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.COMPARE_CONDITION, true)
            .addAttribute(Attributes.AGE, true);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ACropRestriction.class)
            .build();
    }

    @Override
    public ACropRestriction restrict(Block crop) {
        this.crop = crop;

        return this;
    }

    @Override
    public boolean isRestricted(CropWrapper wrapper) {
        if (!isValueNull(Attributes.COMPARE_CONDITION) && !isValueNull(Attributes.AGE)) {
            return wrapper.crop().is(crop) && elaborateRestriction(wrapper.age());
        } else {
            return wrapper.crop().is(crop);
        }
    }

    private boolean elaborateRestriction(int age) {
        var thisAge = get(Attributes.AGE);

        return switch (get(Attributes.COMPARE_CONDITION)) {
            case EQUAL -> thisAge == age;
            case LESS -> age < thisAge;
            case LESS_EQUAL -> age <= thisAge;
            case GREAT -> age > thisAge;
            case GREAT_EQUAL -> age >= thisAge;
        };
    }
}
