package com.alessandro.astages.infrastructure.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
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
public record SyncItemTagS2C(String id, String stage, ResourceLocation tag, List<Item> ignoredItems, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<SyncItemTagS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("tag_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncItemTagS2C> STREAM_CODEC = ACodecs.composite(
        ByteBufCodecs.STRING_UTF8, SyncItemTagS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncItemTagS2C::stage,
        ResourceLocation.STREAM_CODEC, SyncItemTagS2C::tag,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), SyncItemTagS2C::ignoredItems,
        ByteBufCodecs.BOOL, SyncItemTagS2C::renderItemName,
        ByteBufCodecs.BOOL, SyncItemTagS2C::hideTooltip,
        ByteBufCodecs.BOOL, SyncItemTagS2C::hideInJei,
        SyncItemTagS2C::new
    );

    public SyncItemTagS2C(AItemTagRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getTag(), restriction.getIgnoredItems(), restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemTagRestriction(id, stage)
                .set(Attributes.RENDERING_NAME, renderItemName)
                .set(Attributes.HIDING_TOOLTIP, hideTooltip)
                .set(Attributes.HIDING_JEI, hideInJei)
                .restrict(tag)
                .ignoreItems(ignoredItems);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
