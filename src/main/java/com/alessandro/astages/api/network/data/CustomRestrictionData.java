package com.alessandro.astages.api.network.data;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.engine.store.Attributes;

@NotNullParamsAndMethodsReturn
public class CustomRestrictionData {
    public static RestrictionData item(ARestriction<?, ?, ?> restriction) {
        return RestrictionData.of(restriction, Attributes.HIDING_RECIPE_VIEWER, Attributes.SHOW_ACTION_BAR_NAME, Attributes.SHOW_TOOLTIP_NAME, Attributes.SHOW_RECIPE_VIEWER_NAME, Attributes.SHOW_JADE_ITEM_NAME, Attributes.SHOW_JADE_BLOCK_NAME);
    }

    public static RestrictionData ore(ARestriction<?, ?, ?> restriction) {
        return RestrictionData.of(restriction, Attributes.MATCH_ALL_BLOCK_STATES);
    }

    public static RestrictionData structure(ARestriction<?, ?, ?> restriction) {
        return RestrictionData.of(restriction, Attributes.ENTERING);
    }
}
