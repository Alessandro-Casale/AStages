package com.alessandro.astages.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.wrapper.RecipeModWrapper;
import com.alessandro.astages.networking.ACodes;
import com.alessandro.astages.networking.AStagesPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@NotNullMethodsReturn
@Info("For now, required only by JEI.")
public record RecipeModSyncerS2CPacket(String id, String stage, int priority, String modId, List<ResourceLocation> ignoredRecipeIds) implements AStagesPacket {
    public static final Type<RecipeModSyncerS2CPacket> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("recipe_mod_syncer_s2c_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeModSyncerS2CPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::id,
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::stage,
        ByteBufCodecs.INT, RecipeModSyncerS2CPacket::priority,
        ByteBufCodecs.STRING_UTF8, RecipeModSyncerS2CPacket::modId,
        ACodes.RESOURCE_LOCATION.apply(ByteBufCodecs.list()), RecipeModSyncerS2CPacket::ignoredRecipeIds,
        RecipeModSyncerS2CPacket::new
    );

    public RecipeModSyncerS2CPacket(@NotNull ARecipeModRestriction restriction) {
        this(restriction.getId(), restriction.getStage(), restriction.getPriority(), restriction.getModId(), restriction.getIgnoredRecipeIds());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientRecipeModRestriction(id, stage)
            .restrict(new RecipeModWrapper(modId))
            .ignoreItems(ignoredRecipeIds);

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
