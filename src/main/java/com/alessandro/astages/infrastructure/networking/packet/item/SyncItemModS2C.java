package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
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
public record SyncItemModS2C(String id, String stage, Set<String> modIds, Set<Item> ignoredItems, Set<TagKey<Item>> ignoredTags,
                             boolean hideInRecipeViewer,
                             boolean showActionBarName, boolean showTooltipName, boolean showRecipeViewerName, boolean showJadeItemName, boolean showJadeBlockName) implements AStagesPacket {
    public static final Type<SyncItemModS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_item_mod_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemModS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemModS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemModS2C::stage,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::modIds,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::ignoredItems,
        ACodecs.tagKey(Registries.ITEM).apply(ByteBufCodecs.collection(HashSet::new)), SyncItemModS2C::ignoredTags,
        ByteBufCodecs.BOOL, SyncItemModS2C::hideInRecipeViewer,
        ByteBufCodecs.BOOL, SyncItemModS2C::showActionBarName,
        ByteBufCodecs.BOOL, SyncItemModS2C::showTooltipName,
        ByteBufCodecs.BOOL, SyncItemModS2C::showRecipeViewerName,
        ByteBufCodecs.BOOL, SyncItemModS2C::showJadeItemName,
        ByteBufCodecs.BOOL, SyncItemModS2C::showJadeBlockName,
        SyncItemModS2C::new
    );

    public SyncItemModS2C(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModIds(), restriction.getIgnoredItems(), restriction.getIgnoredTags(),
            restriction.get(Attributes.HIDING_RECIPE_VIEWER),
            restriction.get(Attributes.SHOW_ACTION_BAR_NAME), restriction.get(Attributes.SHOW_TOOLTIP_NAME), restriction.get(Attributes.SHOW_RECIPE_VIEWER_NAME), restriction.get(Attributes.SHOW_JADE_ITEM_NAME), restriction.get(Attributes.SHOW_JADE_BLOCK_NAME));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemModRestriction(id, stage)
            .ignoreItems(ignoredItems)
            .ignoreTags(ignoredTags)
            .set(Attributes.HIDING_RECIPE_VIEWER, hideInRecipeViewer)
            .set(Attributes.SHOW_ACTION_BAR_NAME, showActionBarName)
            .set(Attributes.SHOW_TOOLTIP_NAME, showTooltipName)
            .set(Attributes.SHOW_RECIPE_VIEWER_NAME, showRecipeViewerName)
            .set(Attributes.SHOW_JADE_ITEM_NAME, showJadeItemName)
            .set(Attributes.SHOW_JADE_BLOCK_NAME, showJadeBlockName);

        for (var modId : modIds) { restriction.restrict(modId); }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
