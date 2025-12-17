package com.alessandro.astages.networking.packet.item;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.AClientItemModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
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
public record ItemModSyncerS2CPacket(String id, String stage, String modId, List<Item> ignoredItems, List<ResourceLocation> ignoredTags,
                                     boolean renderItemName, boolean hideTooltip, boolean hideInJei) implements AStagesPacket {
    public static final Type<ItemModSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("mod_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemModSyncerS2CPacket> STREAM_CODEC = ACodes.composite(
        ByteBufCodecs.STRING_UTF8, ItemModSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, ItemModSyncerS2CPacket::stage,
        ByteBufCodecs.STRING_UTF8, ItemModSyncerS2CPacket::modId,
        ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list()), ItemModSyncerS2CPacket::ignoredItems,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), ItemModSyncerS2CPacket::ignoredTags,
        ByteBufCodecs.BOOL, ItemModSyncerS2CPacket::renderItemName,
        ByteBufCodecs.BOOL, ItemModSyncerS2CPacket::hideTooltip,
        ByteBufCodecs.BOOL, ItemModSyncerS2CPacket::hideInJei,
        ItemModSyncerS2CPacket::new
    );

    public ItemModSyncerS2CPacket(AItemModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getModId(), restriction.getIgnoredItems(), restriction.getIgnoredTags(), restriction.get(Attributes.RENDERING_NAME), restriction.get(Attributes.HIDING_TOOLTIP), restriction.get(Attributes.HIDING_JEI));
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientItemModRestriction(id, stage)
                .set(Attributes.RENDERING_NAME, renderItemName)
                .set(Attributes.HIDING_TOOLTIP, hideTooltip)
                .set(Attributes.HIDING_JEI, hideInJei)
                .restrict(modId)
                .ignoreItems(ignoredItems)
                .ignoreTags(ignoredTags);

        AClientRestrictionManager.ITEM_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
