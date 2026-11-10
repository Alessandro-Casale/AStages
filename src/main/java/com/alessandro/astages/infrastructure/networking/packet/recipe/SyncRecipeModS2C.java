package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.infrastructure.networking.ASyncRestrictionPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncRecipeModS2C(RestrictionData data, String modId, List<ResourceLocation> ignoredRecipeIds) implements ASyncRestrictionPacket<AClientRecipeModRestriction> {
    public static final Type<SyncRecipeModS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_recipe_mod_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecipeModS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncRecipeModS2C::data,
        ByteBufCodecs.STRING_UTF8, SyncRecipeModS2C::modId,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncRecipeModS2C::ignoredRecipeIds,
        SyncRecipeModS2C::new
    );

    public SyncRecipeModS2C(ARecipeModRestriction restriction) {
        this(RestrictionData.of(restriction), restriction.getModId(), restriction.getIgnoredRecipeIds());
    }

    @Override
    public AClientMinimalManager<?, ?> manager() {
        return AClientRestrictionManager.RECIPE_INSTANCE;
    }

    @Override
    public Class<AClientRecipeModRestriction> restrictionClass() {
        return AClientRecipeModRestriction.class;
    }

    @Override
    public AClientRecipeModRestriction instantiateRestriction() {
        return new AClientRecipeModRestriction(id(), stage(), priority());
    }

    @Override
    public void fillContents(AClientRecipeModRestriction restriction) {
        restriction
            .restrict(new RecipeModWrapper(modId))
            .ignoreRecipes(ignoredRecipeIds);
    }

    @Override
    public void register(AClientRecipeModRestriction restriction) {
        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
