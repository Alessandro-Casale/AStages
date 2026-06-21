package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullMethodsReturn;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.infrastructure.networking.AStagesPacket;
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
public record SyncRecipeModS2C(String id, String stage, int priority, String modId, List<ResourceLocation> ignoredRecipeIds) implements AStagesPacket {
    public static final Type<SyncRecipeModS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_recipe_mod_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecipeModS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncRecipeModS2C::id,
        ByteBufCodecs.STRING_UTF8, SyncRecipeModS2C::stage,
        ByteBufCodecs.INT, SyncRecipeModS2C::priority,
        ByteBufCodecs.STRING_UTF8, SyncRecipeModS2C::modId,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncRecipeModS2C::ignoredRecipeIds,
        SyncRecipeModS2C::new
    );

    public SyncRecipeModS2C(@NotNull ARecipeModRestriction restriction) {
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
