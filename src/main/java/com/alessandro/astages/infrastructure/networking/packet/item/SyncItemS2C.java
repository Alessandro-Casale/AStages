package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncItemS2C(String id, String stage, List<Item> items,
                          boolean hideInRecipeViewer,
                          boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) implements AStagesPacket {
    public static final Type<SyncItemS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("item_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemS2C::stage,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), SyncItemS2C::items,
        ByteBufCodecs.BOOL, SyncItemS2C::hideInRecipeViewer,
        ByteBufCodecs.BOOL, SyncItemS2C::showActionBarName,
        ByteBufCodecs.BOOL, SyncItemS2C::showTooltipName,
        ByteBufCodecs.BOOL, SyncItemS2C::showRecipeViewerName,
        ByteBufCodecs.BOOL, SyncItemS2C::showJadeItemName,
        ByteBufCodecs.BOOL, SyncItemS2C::showJadeBlockName,
        SyncItemS2C::new
    );

    public SyncItemS2C(AItemRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getItems(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER),
            restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_ITEM_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemRestriction(id, stage)
            .set(Attributes.HIDING_RECIPE_VIEWER, hideInRecipeViewer)
            .set(Attributes.SHOW_ACTION_BAR_NAME, showActionBarName)
            .set(Attributes.SHOW_TOOLTIP_NAME, showTooltipName)
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, showRecipeViewerName)
            .set(Attributes.SHOW_JADE_ITEM_NAME, showJadeItemName)
            .set(Attributes.SHOW_JADE_BLOCK_NAME, showJadeBlockName);

        for (var item : items) {
            restriction.restrict(item);
        }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
