package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public record SyncItemPredicateS2C(String id, String stage, ResourceLocation modelId,
                                   boolean hideInRecipeViewer,
                                   boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) implements AStagesPacket {
    public static final Type<SyncItemPredicateS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("predicate_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemPredicateS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemPredicateS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemPredicateS2C::stage,
        ResourceLocation.STREAM_CODEC, SyncItemPredicateS2C::modelId,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::hideInRecipeViewer,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::showActionBarName,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::showTooltipName,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::showRecipeViewerName,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::showJadeItemName,
        ByteBufCodecs.BOOL, SyncItemPredicateS2C::showJadeBlockName,
        SyncItemPredicateS2C::new
    );

    public SyncItemPredicateS2C(AItemPredicateRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModelId(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER),
            restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_ITEM_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemPredicateRestriction(id, stage)
            .restrict(modelId)
            .set(Attributes.HIDING_RECIPE_VIEWER, hideInRecipeViewer)
            .set(Attributes.SHOW_ACTION_BAR_NAME, showActionBarName)
            .set(Attributes.SHOW_TOOLTIP_NAME, showTooltipName)
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, showRecipeViewerName)
            .set(Attributes.SHOW_JADE_ITEM_NAME, showJadeItemName)
            .set(Attributes.SHOW_JADE_BLOCK_NAME, showJadeBlockName);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
