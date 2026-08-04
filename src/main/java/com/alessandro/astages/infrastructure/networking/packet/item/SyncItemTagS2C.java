package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public record SyncItemTagS2C(String id, String stage, TagKey<Item> tag, Set<Item> ignoredItems,
                             boolean hideInRecipeViewer,
                             boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) implements AStagesPacket {
    public static final Type<SyncItemTagS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("tag_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemTagS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemTagS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemTagS2C::stage,
        ACodecs.tagKey(Registries.ITEM), SyncItemTagS2C::tag,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemTagS2C::ignoredItems,
        ByteBufCodecs.BOOL, SyncItemTagS2C::hideInRecipeViewer,
        ByteBufCodecs.BOOL, SyncItemTagS2C::showActionBarName,
        ByteBufCodecs.BOOL, SyncItemTagS2C::showTooltipName,
        ByteBufCodecs.BOOL, SyncItemTagS2C::showRecipeViewerName,
        ByteBufCodecs.BOOL, SyncItemTagS2C::showJadeItemName,
        ByteBufCodecs.BOOL, SyncItemTagS2C::showJadeBlockName,
        SyncItemTagS2C::new
    );

    public SyncItemTagS2C(AItemTagRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getTag(), restriction.getIgnoredItems(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER),
            restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_ITEM_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemTagRestriction(id, stage)
            .restrict(tag)
            .ignoreItems(ignoredItems)
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
