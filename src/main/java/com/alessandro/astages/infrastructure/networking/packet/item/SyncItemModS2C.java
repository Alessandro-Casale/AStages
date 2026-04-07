package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.api.network.ACodecs;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncItemModS2C(String id, String stage, List<String> modIds, List<Item> ignoredItems, List<ResourceLocation> ignoredTags,
                             boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<SyncItemModS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_item_mod_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemModS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemModS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemModS2C::stage,
        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SyncItemModS2C::modIds,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), SyncItemModS2C::ignoredItems,
        ACodecs.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), SyncItemModS2C::ignoredTags,
        ByteBufCodecs.BOOL, SyncItemModS2C::renderItemName,
        ByteBufCodecs.BOOL, SyncItemModS2C::hideTooltip,
        ByteBufCodecs.BOOL, SyncItemModS2C::hideInJei,
        SyncItemModS2C::new
    );

    public SyncItemModS2C(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModIds(), restriction.getIgnoredItems(), restriction.getIgnoredTags(), restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemModRestriction(id, stage)
                .set(Attributes.RENDERING_NAME, renderItemName)
                .set(Attributes.HIDING_TOOLTIP, hideTooltip)
                .set(Attributes.HIDING_JEI, hideInJei)
                .ignoreItems(ignoredItems)
                .ignoreTags(ignoredTags);

        for (var modId : modIds) { restriction.restrict(modId); }

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
