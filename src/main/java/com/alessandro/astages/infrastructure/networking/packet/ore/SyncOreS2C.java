package com.alessandro.astages.infrastructure.networking.packet.ore;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.data.CustomRestrictionData;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.AClientOreRestriction;
import com.alessandro.astages.engine.server.restriction.AOreRestriction;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@NotNullParams
public record SyncOreS2C(RestrictionData data, BlockState original, BlockState replacement) implements ARestrictionPacket {
    public static final Type<SyncOreS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_ore_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOreS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncOreS2C::data,
        ByteBufCodecs.fromCodec(BlockState.CODEC), SyncOreS2C::original,
        ByteBufCodecs.fromCodec(BlockState.CODEC), SyncOreS2C::replacement,
        SyncOreS2C::new
    );

    public SyncOreS2C(AOreRestriction restriction) {
        this(CustomRestrictionData.ore(restriction), restriction.getOriginal(), restriction.getReplacement());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientOreRestriction(id(), stage(), priority())
                .restrict(new OreWrapper(original, replacement))
                .setFromData(data, Attributes.MATCH_ALL_BLOCK_STATES);

        AClientRestrictionManager.ORE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
