package com.alessandro.astages.infrastructure.networking;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.networking.packet.structure.SyncRestrictedStructuresS2C;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.infrastructure.networking.packet.dimension.SyncDimensionIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.item.*;
import com.alessandro.astages.infrastructure.networking.packet.mob.SyncMobS2C;
import com.alessandro.astages.infrastructure.networking.packet.ore.SyncOreS2C;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeModS2C;
import com.alessandro.astages.infrastructure.networking.packet.recipe.SyncRecipeS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestReloadS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import com.alessandro.astages.infrastructure.networking.packet.simple.SyncSimpleIdsS2C;
import com.alessandro.astages.infrastructure.networking.packet.stages.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class Networking {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);

        // STAGES
        registrar.playToClient(SyncPlayerStagesS2C.TYPE, SyncPlayerStagesS2C.STREAM_CODEC, SyncPlayerStagesS2C::handle);
        registrar.playToClient(SyncKnownStagesS2C.TYPE, SyncKnownStagesS2C.STREAM_CODEC, SyncKnownStagesS2C::handle);
        registrar.playToClient(SyncPermanentStageS2C.TYPE, SyncPermanentStageS2C.STREAM_CODEC, SyncPermanentStageS2C::handle);
        registrar.playToClient(SyncTemporaryStageS2C.TYPE, SyncTemporaryStageS2C.STREAM_CODEC, SyncTemporaryStageS2C::handle);
        registrar.playToClient(RequestClientStagesS2C.TYPE, RequestClientStagesS2C.STREAM_CODEC, RequestClientStagesS2C::handle);
        registrar.playToServer(ReplyClientStagesC2S.TYPE, ReplyClientStagesC2S.STREAM_CODEC, ReplyClientStagesC2S::handle);

        // ITEMS
        registrar.playToClient(SyncItemS2C.TYPE, SyncItemS2C.STREAM_CODEC, SyncItemS2C::handle);
        registrar.playToClient(SyncItemTagS2C.TYPE, SyncItemTagS2C.STREAM_CODEC, SyncItemTagS2C::handle);
        registrar.playToClient(SyncItemModS2C.TYPE, SyncItemModS2C.STREAM_CODEC, SyncItemModS2C::handle);
        registrar.playToClient(SyncItemPredicateS2C.TYPE, SyncItemPredicateS2C.STREAM_CODEC, SyncItemPredicateS2C::handle);
        registrar.playToClient(ReplyItemPropertyS2C.TYPE, ReplyItemPropertyS2C.STREAM_CODEC, ReplyItemPropertyS2C::handle);
        registrar.playToServer(RequestItemPropertyC2S.TYPE, RequestItemPropertyC2S.STREAM_CODEC, RequestItemPropertyC2S::handle);

        // RECIPE
        registrar.playToClient(SyncRecipeS2C.TYPE, SyncRecipeS2C.STREAM_CODEC, SyncRecipeS2C::handle);
        registrar.playToClient(SyncRecipeModS2C.TYPE, SyncRecipeModS2C.STREAM_CODEC, SyncRecipeModS2C::handle);

        // ORES
        registrar.playToClient(SyncOreS2C.TYPE, SyncOreS2C.STREAM_CODEC, SyncOreS2C::handle);

        // MOB
        registrar.playToClient(SyncMobS2C.TYPE, SyncMobS2C.STREAM_CODEC, SyncMobS2C::handle);

        // DIMENSION
        registrar.playToClient(SyncDimensionIdsS2C.TYPE, SyncDimensionIdsS2C.STREAM_CODEC, SyncDimensionIdsS2C::handle);

        // STRUCTURE
        registrar.playToClient(SyncRestrictedStructuresS2C.TYPE, SyncRestrictedStructuresS2C.STREAM_CODEC, SyncRestrictedStructuresS2C::handle);

        // SERVER
        registrar.playToClient(SyncServerStagesS2C.TYPE, SyncServerStagesS2C.STREAM_CODEC, SyncServerStagesS2C::handle);

        // SIMPLE
        registrar.playToClient(SyncSimpleIdsS2C.TYPE, SyncSimpleIdsS2C.STREAM_CODEC, SyncSimpleIdsS2C::handle);

        // RELOADING
        registrar.playToClient(RequestReloadS2C.TYPE, RequestReloadS2C.STREAM_CODEC, RequestReloadS2C::handle);
        registrar.playToClient(RequestRestrictionDeleteS2C.TYPE, RequestRestrictionDeleteS2C.STREAM_CODEC, RequestRestrictionDeleteS2C::handle);
    }

    @Info("Send to server!")
    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Info("Send to client!")
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Info("Send to client!")
    public static void sendToAllPlayers(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    @Info("Send to client!")
    public static void sendTo(@Nullable ServerPlayer player, CustomPacketPayload payload) {
        if (player == null) { // If Null -> Whole Server!
            PacketDistributor.sendToAllPlayers(payload);
        } else {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
