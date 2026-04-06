package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public class ADimensionRestriction extends ARestriction<ADimensionRestriction, ResourceLocation, ResourceLocation> {
    private final List<ResourceLocation> dimensions = new ArrayList<>();

    private static final String nbtId = "astages_dimension_timer_";
    private static final String nbtAccess = "astages_dimension_access_";
    private ATime maxStayTimer = null;

    public ADimensionRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ADimensionRestriction newBuilder() {
        return new ADimensionRestriction("null", "null");
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.ALLOW_ACCESS)
            .addAttribute(Attributes.BIDIRECTIONAL)

            .addAttribute(Attributes.MAX_ACCESS)

            .addAttribute(Attributes.CHAT_FORMATTING)

            .addAttribute(Attributes.Dimension.ENTER_MESSAGE)
            .addAttribute(Attributes.Dimension.LEAVE_MESSAGE)
            .addAttribute(Attributes.Dimension.EXPIRED_MESSAGE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ADimensionRestriction.class)
            .build();
    }

    @Override
    public ADimensionRestriction restrict(ResourceLocation dimension) {
        dimensions.add(dimension);

        return this;
    }

    @Override
    public boolean isRestricted(ResourceLocation dimension) {
        for (ResourceLocation dim : dimensions) {
            if (dim.equals(dimension)) {
                return true;
            }
        }

        return false;
    }

    public List<ResourceLocation> getDimensions() {
        return dimensions;
    }

    @Contract(pure = true)
    public static String getNbtIdForRestrictionId(String restrictionId) {
        return nbtId + restrictionId;
    }

    @Contract(pure = true)
    public static String getNbtAccessForRestrictionId(String restrictionId) {
        return nbtAccess + restrictionId;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setBidirectional(boolean value) {
        set(Attributes.BIDIRECTIONAL, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setMaxStayTimer(ATime timer) {
        maxStayTimer = timer;
        return this;
    }

    public @Nullable Integer getMaxStayTimer() {
        if (maxStayTimer == null) {
            return null;
        }

        return maxStayTimer.getTicks();
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setMaxAccess(int value) {
        set(Attributes.MAX_ACCESS, value);
        return this;
    }

    public String getNbtId() {
        return nbtId + this.getId();
    }

    public String getNbtAccess() {
        return nbtAccess + this.getId();
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setTimerFormatting(ChatFormatting value) {
        set(Attributes.CHAT_FORMATTING, value);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setDimensionMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Dimension.ENTER_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setLeaveDimensionMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Dimension.LEAVE_MESSAGE, message);
        return this;
    }

    @SuppressWarnings("unused")
    public ADimensionRestriction setExpiredDimensionMessage(Function<ResourceLocation, Component> message) {
        set(Attributes.Dimension.EXPIRED_MESSAGE, message);
        return this;
    }
}
