package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemTagRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import com.alessandro.astages.store.Attributes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ItemTagSyncerS2CPacket(String id, String stage, ResourceLocation tag, List<Item> ignoredItems, boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<ItemTagSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("tag_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemTagSyncerS2CPacket> STREAM_CODEC = ACodes.composite(
        ByteBufCodecs.STRING_UTF8, ItemTagSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemTagSyncerS2CPacket::stage,
        ACodes.RESOURCE_LOCATION, ItemTagSyncerS2CPacket::tag,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), ItemTagSyncerS2CPacket::ignoredItems,
        ByteBufCodecs.BOOL, ItemTagSyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemTagSyncerS2CPacket::hideTooltip,
        ByteBufCodecs.BOOL, ItemTagSyncerS2CPacket::hideInJei,
        ItemTagSyncerS2CPacket::new
    );

    public ItemTagSyncerS2CPacket(AItemTagRestriction restriction) {
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
