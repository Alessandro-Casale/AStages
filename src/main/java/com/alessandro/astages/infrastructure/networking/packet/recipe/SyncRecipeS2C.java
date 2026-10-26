package com.alessandro.astages.infrastructure.networking.packet.recipe;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.network.data.RestrictionData;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.recipe.AClientRecipeRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.infrastructure.networking.ARestrictionPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

@NotNullParamsAndMethodsReturn
public record SyncRecipeS2C(RestrictionData data, RecipeType<?> recipeType, List<ResourceLocation> recipes) implements ARestrictionPacket {
    public static final Type<SyncRecipeS2C> TYPE = new Type<>(AResourceLocation.fromNamespaceAndPath("sync_recipe_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRecipeS2C> STREAM_CODEC = StreamCodec.composite(
        RestrictionData.STREAM_CODEC, SyncRecipeS2C::data,
        ByteBufCodecs.fromCodecWithRegistries(BuiltInRegistries.RECIPE_TYPE.byNameCodec()), SyncRecipeS2C::recipeType,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncRecipeS2C::recipes,
        SyncRecipeS2C::new
    );

    public SyncRecipeS2C(ARecipeRestriction restriction) {
        this(RestrictionData.of(restriction), restriction.getType(), restriction.getRecipes());
    }

    @Override
    public void run(IPayloadContext context) {
        var restriction = new AClientRecipeRestriction(id(), stage(), priority());

        for (ResourceLocation recipe : recipes) {
            restriction.restrict(new RecipeWrapper(recipeType, recipe));
        }

        AClientRestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
